package com.kb.worldcities.ui

import com.kb.worldcities.data.entities.City

interface CityClickEvent {
    fun onItemClick(city: City)
}