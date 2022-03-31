package com.example.square1taskapp.ui.mapscreen

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.filter
import androidx.paging.map
import com.example.square1taskapp.R
import com.example.square1taskapp.data.models.Item
import com.example.square1taskapp.databinding.FragmentMapBinding
import com.example.square1taskapp.presentation.viewmodels.CitiesViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@OptIn(ExperimentalPagingApi::class)
@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mMap: GoogleMap
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CitiesViewModel by viewModels()
    val listOfCities = mutableListOf<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMapBinding.bind(view)
        loadData()


        val mapFragment = childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    fun loadData(){
        lifecycleScope.launch {
            viewModel.getAllCitiesFromRemoteMediator.collect {
                it.map{
                    listOfCities.add(it)
                }
            }
        }
    }

    override fun onMapReady(gMap: GoogleMap?) {
        if (gMap != null) {
            mMap = gMap
        }
        val listOfCordinnates = mutableListOf<LatLng>()
        //adding default city
        listOfCordinnates.add(LatLng(34.5166667, 69.1833344))

        //adding city coordinates from listof items from database
        if (listOfCities.isNullOrEmpty()){
            listOfCities.forEach { item ->
                listOfCordinnates.add(LatLng(item.lat!!, item.lng!!))
            }
        }

            listOfCordinnates.forEach {
                mMap.addMarker(MarkerOptions()
                    .position(LatLng(it.longitude, it.latitude))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.minibmw)))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 12.5F), 4000, null)

            }
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu1_layout, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){

            R.id.list_View ->{
                findNavController().navigate(R.id.action_mapFragment_to_listFragment)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MapFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}