package com.kb.worldcities.ui.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kb.worldcities.data.entities.City
import com.kb.worldcities.databinding.ItemCityBinding
import com.kb.worldcities.ui.CityClickEvent

class CityListAdaptor : RecyclerView.Adapter<CityViewHolder>() {

    private var cities = listOf<City>()

    fun setCityList(cities: List<City>) {
        this.cities = cities
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCityBinding.inflate(inflater, parent, false)
        return CityViewHolder(binding)
    }

    private lateinit var onItemClickListener: CityClickEvent

    fun setOnItemClickListener(listener: CityClickEvent) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = cities.get(position)
        holder.itemView.setOnClickListener { onItemClickListener.onItemClick(city) }
        with(holder.binding) {
            cityTitle.setText(city.displayName)
            citySubTitle.setText(
                (city.coord.lat).toString().plus(", ")
                    .plus(city.coord.lon)
            )
        }
    }

    override fun getItemCount(): Int {
        return cities.size
    }

}