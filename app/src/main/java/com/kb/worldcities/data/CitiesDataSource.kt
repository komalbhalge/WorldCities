package com.kb.worldcities.data

import com.kb.worldcities.data.entities.City
import com.kb.worldcities.utils.Resource
import java.util.*

interface CitiesDataSource {
    fun getCityData(fileResID: Int): Resource<List<City>>
    fun getCityMap(): Resource<TreeMap<String, City>>
}