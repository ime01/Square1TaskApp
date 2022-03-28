package com.example.square1taskapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.square1taskapp.util.Constants.CITIES_REMOTE_KEYS_TABLE


@Entity(tableName = CITIES_REMOTE_KEYS_TABLE)
data class CitiesRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val prevPage: Int?,
    val nextPage:Int?
)
