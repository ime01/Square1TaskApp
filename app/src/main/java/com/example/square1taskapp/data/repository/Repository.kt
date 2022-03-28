package com.example.square1taskapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.square1taskapp.data.local.CitiesDatabase
import com.example.square1taskapp.data.models.Item
import com.example.square1taskapp.data.paging.CitieRemoteMediator
import com.example.square1taskapp.data.paging.CitiesPagingSource
import com.example.square1taskapp.data.remote.CitiesApi
import com.example.square1taskapp.util.Constants.ITEMS_PER_PAGE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalPagingApi
class Repository @Inject constructor(private val citiesApi: CitiesApi, private val citiesDatabase: CitiesDatabase) {

    fun getAllCityItems(): Flow<PagingData<Item>>{

        val pagingSourceFactory = { citiesDatabase.CitiesDao().getAllCityItems() }

        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),

            remoteMediator = CitieRemoteMediator(citiesApi = citiesApi, citiesDatabase =citiesDatabase),

            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun searchCities(query: String): Flow<PagingData<Item>>{

        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            pagingSourceFactory = {
                CitiesPagingSource(citiesApi = citiesApi, query = query)
            }
        ).flow
    }


}