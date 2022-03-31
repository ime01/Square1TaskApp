package com.example.square1taskapp.ui.listscreen

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.square1taskapp.R
import com.example.square1taskapp.data.models.Item
import com.example.square1taskapp.databinding.FragmentListBinding
import com.example.square1taskapp.presentation.adapter.CitiesLoadStateAdapter
import com.example.square1taskapp.presentation.viewmodels.CitiesViewModel
import com.example.square1taskapp.util.showSnackbar
import com.flowz.paging3withflow.adapter.CitiesPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagingApi::class)
@InternalCoroutinesApi
@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list), CitiesPagingAdapter.OnitemClickListener {

    private  var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var citiesAdapter: CitiesPagingAdapter
    private val viewModel: CitiesViewModel by viewModels()
    lateinit var cityName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentListBinding.bind(view)


        showWelcomeMarqueeText()
        loadReclyclerView()
        observeSearchedCities()

        lifecycleScope.launch {
            val getAllCities = viewModel.getAllCitiesFromRemoteMediator.collect {
                citiesAdapter.submitData(it)
            }
        }



       citiesAdapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                rvCities.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                errorText.isVisible = loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && citiesAdapter.itemCount < 1) {
                    rvCities.isVisible = false
                    errorText.isVisible = true

                } else {
                    errorText.isVisible = false
                }
            }
        }


        binding.buttonRetry.setOnClickListener {
            citiesAdapter.retry()
        }

        binding.fetchCities.setOnClickListener {

            if (TextUtils.isEmpty(binding.cityName.text.toString())) {
                binding.cityName.setError(getString(R.string.enter_valid_input))
                return@setOnClickListener
            } else {
                cityName = binding.cityName.text.toString().trim()

               // viewModel.updateSearchQuery(cityName)

                viewModel.searchCity(cityName)

            }


        }

    }


    fun showWelcomeMarqueeText() {
        binding.welcomeTextMarquee.apply {
            setSingleLine()
            ellipsize = TextUtils.TruncateAt.MARQUEE
            marqueeRepeatLimit = -1
            isSelected = true
        }
    }

    @InternalCoroutinesApi
    fun observeSearchedCities(){
        lifecycleScope.launch{
            viewModel.searchedCities.collect {
                Log.d("TVALUE", "$it")
                citiesAdapter.submitData(it)
            }
        }
    }

    private fun loadReclyclerView() {

        binding.shimmerFrameLayout.startShimmer()
        citiesAdapter = CitiesPagingAdapter(this@ListFragment)

        binding.rvCities.apply {

            layoutManager = LinearLayoutManager(requireContext())

            adapter =  citiesAdapter.withLoadStateHeaderAndFooter(
                header = CitiesLoadStateAdapter{ citiesAdapter.retry()},
                footer = CitiesLoadStateAdapter{ citiesAdapter.retry()}
            )
            setHasFixedSize(true)
            binding.shimmerFrameLayout.stopShimmer()

        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_layout, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){

            R.id.map_view ->{
                findNavController().navigate(R.id.action_listFragment_to_mapFragment)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(item: Item) {
        showSnackbar(binding.cityName, "Yea! You Clicked ${item.name} City!")
    }

}