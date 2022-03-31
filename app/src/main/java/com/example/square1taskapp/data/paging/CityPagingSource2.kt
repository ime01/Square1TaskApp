package com.example.square1taskapp.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.square1taskapp.data.models.Item
import com.example.square1taskapp.data.remote.CitiesApi



class CityPagingSource2(private val apiService: CitiesApi, private val query:String): PagingSource<Int, Item>() {

  /*  override fun getRefreshKey(state: PagingState<Int,  Item>): Int? {
        return null
    }*/

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int,  Item> {

        return try {
            val currentPage = params.key ?:1

            //val response = apiService.fetchCities(currentPage)
            val response = apiService.searchCities(currentPage, query)
//            Log.e(TAG, "$response")

            val data = response.body()?.data?.items?: emptyList()

            val endOfPaginationReached = data.isEmpty()

            val responseData = mutableListOf<Item>()
            responseData.addAll(data)

            Log.e(TAG, "$responseData")

            if (data.isNotEmpty()) {
                LoadResult.Page(
                    data = data,
                    prevKey = if (currentPage == 1) null else currentPage - 1,
                    nextKey = if (endOfPaginationReached) null else currentPage + 1
                )
            } else {
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }

        }catch (e:Exception){
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        return state.anchorPosition
    }

    companion object{
        const val TAG = "Paging Source"
    }

}