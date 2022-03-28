package com.flowz.paging3withflow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.square1taskapp.data.models.Item
import com.example.square1taskapp.databinding.CitiesItemBinding

class CitiesPagingAdapter(private val listener: OnitemClickListener): PagingDataAdapter<Item, CitiesPagingAdapter.MyViewHolder>(diffCallback) {


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = getItem(position)

        holder.binding.apply {

            holder.itemView.apply {

                citiName.text =  " City Name: ${currentItem?.name} "
                cityLocalName.text = " City Local Name:  ${currentItem?.local_name} "
                cityLongitude.text = " City Longitude:  ${currentItem?.lng} "
                cityLatitude.text = " City Latitude:  ${currentItem?.lat} "


//            loading image here with COIL libarary
              /*  rnmImage.load(imageLink){
                    error(R.drawable.ic_baseline_error_24)
                    placeholder(R.drawable.ic_baseline_error_24)
                    crossfade(true)
                    crossfade(1000)*/
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(CitiesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    interface OnitemClickListener {
        fun onItemClick(ricknMorty: Item)

    }

    inner class MyViewHolder(val binding: CitiesItemBinding):RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position!= RecyclerView.NO_POSITION){
                    val item = getItem(position)
                    if (item!= null){
                        listener.onItemClick(item)
                    }
                }
            }
        }
    }



    companion object{
        val diffCallback = object :DiffUtil.ItemCallback<Item>(){
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }

        }
    }

}


