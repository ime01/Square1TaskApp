package com.example.square1taskapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.square1taskapp.data.local.dao.CitieRemoteKeysDao
import com.example.square1taskapp.data.local.dao.CitiesDao
import com.example.square1taskapp.data.models.CitiesRemoteKeys
import com.example.square1taskapp.data.models.Item

@Database(entities = [Item::class, CitiesRemoteKeys::class], version = 1, exportSchema = false)
abstract class CitiesDatabase : RoomDatabase(){

    abstract fun CitiesDao(): CitiesDao
    abstract fun CitiesRemoteKeysDao(): CitieRemoteKeysDao


}