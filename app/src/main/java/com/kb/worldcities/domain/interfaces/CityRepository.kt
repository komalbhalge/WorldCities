package com.kb.worldcities.domain.interfaces

import com.kb.worldcities.data.entities.City
import com.kb.worldcities.utils.Resource

interface CityRepository {
    suspend fun getAllCities(): Resource<List<City>>
    suspend fun getCitiesStartingWith(prefix: String): Resource<List<City>>
}