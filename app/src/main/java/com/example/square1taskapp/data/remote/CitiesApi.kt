package com.example.square1taskapp.data.remote

import com.example.square1taskapp.data.models.CityResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CitiesApi {

    @GET("city")
    suspend fun fetchCities(@Query("page") page:Int) : Response<CityResponse>

    @GET("city")
    suspend fun searchCities(@Query("page") page:Int, @Query("filter[0][name][contains]") cityName:String?) : Response<CityResponse>
}