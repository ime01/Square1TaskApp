package com.example.square1taskapp.data.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.square1taskapp.util.Constants
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = Constants.CITIES_TABLE)
data class Item(
    @Embedded(prefix = "cou_")
    val country: Country?,
    val country_id: Int?,
    val created_at: String?,
    @PrimaryKey(autoGenerate = false)
    val id: Int?,
    val lat: Double?,
    val lng: Double?,
    val local_name: String?,
    val name: String?,
    val updated_at: String?
): Parcelable{

    @Parcelize
    data class Country(
        val code: String?,
        val continent_id: Int?,
        val created_at: String?,
        val id: Int?,
        val name: String?,
        val updated_at: String?
    ): Parcelable
}