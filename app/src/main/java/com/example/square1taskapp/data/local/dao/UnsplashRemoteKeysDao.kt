package com.example.square1taskapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.square1taskapp.data.models.CitiesRemoteKeys


@Dao
interface CitieRemoteKeysDao {

    @Query("SELECT * FROM city_remote_keys_table WHERE id =:id")
    suspend fun getRemoteKeys(id: String): CitiesRemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<CitiesRemoteKeys>)

    @Query("DELETE FROM city_remote_keys_table")
    suspend fun deleteAllRemoteKeys()

}