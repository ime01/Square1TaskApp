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
import java.util.concurrent.TimeUnit


@ExperimentalPagingApi
class CitieRemoteMediator (private val citiesApi: CitiesApi, private val citiesDatabase: CitiesDatabase, private val query:String) : RemoteMediator<Int, Item>(){

    private val citiesDao = citiesDatabase.CitiesDao()
    private val citiesRemoteKeysDao = citiesDatabase.CitiesRemoteKeysDao()

    override suspend fun initialize(): InitializeAction {
            // Need to refresh cached data from network; returning
            // LAUNCH_INITIAL_REFRESH here will also block RemoteMediator's
            // APPEND and PREPEND from running until REFRESH succeeds.
           return InitializeAction.LAUNCH_INITIAL_REFRESH
        }


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


            val response = citiesApi.searchCities(ITEMS_PER_PAGE, query)

            val endOfPaginationReached = response.body()?.data?.items?.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached!!) null else currentPage + 1

            citiesDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    citiesDao.deleteAllCityItems()
                    citiesRemoteKeysDao.deleteAllRemoteKeys()
                }
                val keys = response.body()?.data?.items?.map { cityItem ->
                    CitiesRemoteKeys(
                        id = cityItem.id.toString(),
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }

                    citiesRemoteKeysDao.addAllRemoteKeys(remoteKeys = keys!!)

                citiesDao.addCityItems(response.body()?.data?.items!!)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
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
