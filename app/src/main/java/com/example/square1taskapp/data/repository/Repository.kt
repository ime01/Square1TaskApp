package com.example.square1taskapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.square1taskapp.data.local.CitiesDatabase
import com.example.square1taskapp.data.models.Item
import com.example.square1taskapp.data.paging.CitieRemoteMediator
import com.example.square1taskapp.data.paging.CityPagingSource2
import com.example.square1taskapp.data.remote.CitiesApi
import com.example.square1taskapp.util.Constants.ITEMS_PER_PAGE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalPagingApi
class Repository @Inject constructor(private val citiesApi: CitiesApi, private val citiesDatabase: CitiesDatabase) {

    //data from remote mediator(local+api)
    fun getAllCityItems(query: String): Flow<PagingData<Item>>{

        val pagingSourceFactory = { citiesDatabase.CitiesDao().getAllCityItems() }

        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),

            remoteMediator = CitieRemoteMediator(citiesApi = citiesApi, citiesDatabase =citiesDatabase, query),

            pagingSourceFactory = pagingSourceFactory
        ).flow
    }


    //data from remote api only
    fun searchCities(query: String): Flow<PagingData<Item>>{

        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            pagingSourceFactory = {
                CityPagingSource2( apiService = citiesApi, query = query)
            }
        ).flow
    }


}
