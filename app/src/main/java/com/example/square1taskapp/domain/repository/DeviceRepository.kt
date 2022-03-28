package com.example.square1taskapp.domain.repository

import androidx.lifecycle.LiveData
import com.example.square1taskapp.data.models.Item
import kotlinx.coroutines.flow.Flow


interface CitiesRepository {

    fun getCities() : Flow<List<Item>>

    /*suspend fun insertCities(item: Item)

    fun searchCities(searchQuery: String): Flow<List<Item>>

    suspend fun insertListCities(devices: List<Item>)

    fun observeAllCities(): LiveData<List<Item>>

    fun deleteAllCities()*/
}