package com.example.square1taskapp.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.square1taskapp.data.models.Item
import com.example.square1taskapp.data.paging.CityPagingSource2
import com.example.square1taskapp.data.remote.CitiesApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(InternalCoroutinesApi::class)
@ExperimentalPagingApi
@HiltViewModel
class CitiesViewModel @Inject constructor( val citiesApi: CitiesApi): ViewModel() {

    /*val getAllCities = repository.getAllCityItems()
    private val _searchedCities = MutableStateFlow<PagingData<Item>>(PagingData.empty())
    val searchedCities = _searchedCities
*/

    val citiesDataFromNetwork = Pager(PagingConfig(pageSize = 1)){
        CityPagingSource2(citiesApi)
    }.flow.cachedIn(viewModelScope)



   /* fun searchCities(query: String){
        viewModelScope.launch{
            repository.searchCities(query).cachedIn(viewModelScope).collect{
                Log.d("SEARCH", "$it")
                _searchedCities.value = it
            }
        }
    }*/





   /* val citiesDataFromNetwork = Pager(PagingConfig(pageSize = 1)){
        CitiesPagingSource(citiesApi, "lagos")
    }.flow.cachedIn(viewModelScope)

    fun searchAllCities(query:String){
        repository.searchCities(query)
    }
*/
}