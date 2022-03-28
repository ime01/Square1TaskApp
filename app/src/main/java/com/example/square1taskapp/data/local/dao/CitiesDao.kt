package com.example.square1taskapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.square1taskapp.data.models.Item


@Dao
interface CitiesDao {

    @Query("SELECT * FROM city_table")
    fun getAllCityItems(): PagingSource<Int, Item>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCityItems(images: List<Item>)

    @Query("DELETE FROM city_table")
    suspend fun deleteAllCityItems()


}