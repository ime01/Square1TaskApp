package com.example.square1taskapp.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.square1taskapp.data.models.Item
import com.example.square1taskapp.data.remote.CitiesApi

class CityPagingSource2(private val apiService: CitiesApi): PagingSource<Int, Item>() {

    override fun getRefreshKey(state: PagingState<Int,  Item>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int,  Item> {

        return try {
            val currentPage = params.key ?:1

            val response = apiService.fetchCities(currentPage)
//            Log.e(TAG, "$response")

            val data = response.body()?.data?.items?: emptyList()

            val responseData = mutableListOf<Item>()
            responseData.addAll(data)

            Log.e(TAG, "$responseData")

            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage==1) null else -1,
                nextKey = currentPage.plus(1)
            )

        }catch (e:Exception){
            LoadResult.Error(e)
        }
    }

    companion object{
        const val TAG = "Paging Source"
    }

}