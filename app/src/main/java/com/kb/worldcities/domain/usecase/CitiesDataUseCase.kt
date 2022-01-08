package com.kb.worldcities.domain.usecase

import com.kb.worldcities.data.entities.City
import com.kb.worldcities.domain.interfaces.CityRepository
import com.kb.worldcities.utils.Resource
import javax.inject.Inject

class CitiesDataUseCase @Inject constructor(
    private val cityRepository: CityRepository
) {

   suspend fun getAllCities(): Resource<List<City>> {
        return cityRepository.getAllCities()
    }

   suspend fun getCitiesStartingWith(prefix: String): Resource<List<City>> {
        return cityRepository.getCitiesStartingWith(prefix)
    }
}