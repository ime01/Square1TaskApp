package com.example.square1taskapp.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.square1taskapp.data.local.CitiesDatabase
import com.example.square1taskapp.util.Constants.ITEMS_PER_PAGE
import com.example.square1taskapp.data.models.CitiesRemoteKeys
import com.example.square1taskapp.data.models.Item
import com.example.square1taskapp.data.remote.CitiesApi


@ExperimentalPagingApi
class CitieRemoteMediator (private val citiesApi: CitiesApi, private val citiesDatabase: CitiesDatabase) : RemoteMediator<Int, Item>(){

    private val citiesDao = citiesDatabase.CitiesDao()
    private val citiesRemoteKeysDao = citiesDatabase.CitiesRemoteKeysDao()

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Item>): MediatorResult {

        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

           // val response = unsplashApi.getAllImages(page = currentPage, per_page = ITEMS_PER_PAGE)
            val response = citiesApi.fetchCities(ITEMS_PER_PAGE)

            val endOfPaginationReached = response.data?.items?.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached == true) null else currentPage + 1

            citiesDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    citiesDao.deleteAllCityItems()
                    citiesRemoteKeysDao.deleteAllRemoteKeys()
                }
                val keys = response.data?.items?.map { cityItem ->
                    CitiesRemoteKeys(
                        id = cityItem.id.toString(),
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }
                if (keys != null) {
                    citiesRemoteKeysDao.addAllRemoteKeys(remoteKeys = keys)
                }
                //unsplashImageDao.addImages(images = response)
                response.data?.items?.let { citiesDao.addCityItems(it) }
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached!!)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }


        }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Item>): CitiesRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                citiesRemoteKeysDao.getRemoteKeys(id = id.toString())
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Item>): CitiesRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { city ->
                citiesRemoteKeysDao.getRemoteKeys(id = city.id.toString())
            }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Item>): CitiesRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { city ->
                citiesRemoteKeysDao.getRemoteKeys(id = city.id.toString())
            }

    }

}