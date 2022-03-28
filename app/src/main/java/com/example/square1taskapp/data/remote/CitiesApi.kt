package com.example.square1taskapp.data.remote

import com.example.square1taskapp.data.models.CityResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CitiesApi {

    @GET("city")
    suspend fun fetchCities(@Query("page") page:Int) : CityResponse
}