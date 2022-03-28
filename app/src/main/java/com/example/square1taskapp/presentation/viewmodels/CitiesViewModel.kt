package com.example.square1taskapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.square1taskapp.data.paging.CitiesPagingSource
import com.example.square1taskapp.data.remote.CitiesApi
import com.example.square1taskapp.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@ExperimentalPagingApi
@HiltViewModel()
class CitiesViewModel @Inject constructor(repository: Repository, citiesApi: CitiesApi): ViewModel() {

    val getAllCities = repository.getAllCityItems()
    val searchAllCities = repository.searchCities("ka")

    val citiesDataFromNetwork = Pager(PagingConfig(pageSize = 1)){
        CitiesPagingSource(citiesApi, "ka")
    }.flow.cachedIn(viewModelScope)

}