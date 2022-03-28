package com.example.square1taskapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.square1taskapp.databinding.CitiesLoadStateFooterBinding


class CitiesLoadStateAdapter(private val retry: () -> Unit): LoadStateAdapter<CitiesLoadStateAdapter.LoadStateViewHolder> (){

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
       val binding = CitiesLoadStateFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
       holder.bind(loadState)
    }
    
  inner class LoadStateViewHolder(private val binding: CitiesLoadStateFooterBinding): RecyclerView.ViewHolder(binding.root){

        init {
            binding.loadStateFooterButtonRetry.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState){
            binding.apply {
               loadStateFooterProgressbar.isVisible = loadState is LoadState.Loading
               loadStateFooterButtonRetry.isVisible = loadState !is LoadState.Loading
               loadStateFooterErrorText.isVisible = loadState !is LoadState.Loading
            }
        }
    }

}