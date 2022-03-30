/*
package com.example.square1taskapp.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.square1taskapp.util.Constants.ITEMS_PER_PAGE
import com.example.square1taskapp.data.models.Item
import com.example.square1taskapp.data.remote.CitiesApi


class CitiesPagingSource(private val citiesApi: CitiesApi, private val query: String) : PagingSource<Int, Item>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {

        val currentPage = params.key ?: 1
        return try {*/
/**//*

            //val response = unsplashApi.searchImages(query = query, per_page = ITEMS_PER_PAGE)
            val response = citiesApi.searchCities(ITEMS_PER_PAGE, query)
            Log.d("CRES", "$response")

            val endOfPaginationReached = response.data?.items?.isEmpty()
            Log.d("APIRES", "$response")

            if (response.data?.items?.isNotEmpty()!!) {
                LoadResult.Page(
                    data = response.data.items,
                    prevKey = if (currentPage == 1) null else currentPage - 1,
                    nextKey = if (endOfPaginationReached == true) null else currentPage + 1
                )
            } else {
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        return state.anchorPosition
    }

}
*/
