package com.kb.worldcities.data

import com.kb.worldcities.FakeCityData
import com.kb.worldcities.data.entities.City
import com.kb.worldcities.domain.interfaces.CityRepository
import com.kb.worldcities.utils.Resource
import com.kb.worldcities.utils.Status
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeCityRepositoryImpl : CityRepository {
    override suspend fun getAllCities(): Resource<List<City>> {
        val response = Resource.success(data = mockCityMap(FakeCityData.cities))
        return if (response.status == Status.SUCCESS) {
            response.data?.values?.let { Resource.success(data = it.toTypedArray().toList()) }
                ?: Resource.error(response.message ?: "City list is empty!", null)
        } else {
            Resource.error(response.message ?: "Error loading cities!", null)
        }
    }

    override suspend fun getCitiesStartingWith(prefix: String): Resource<List<City>> {
        val response = Resource.success(data = mockCityMap(FakeCityData.cities))
        return if (response.status == Status.SUCCESS) {
            try {
                response.data?.subMap(
                    prefix.lowercase(), prefix.lowercase() + Character.MAX_VALUE
                )?.values?.let {
                    Resource.success(data = it.toTypedArray().toList())
                } ?: Resource.error(response.message ?: "City list is empty!", null)

            } catch (exception: Exception) {
                Resource.error(msg = response.message ?: "City list is empty!", data = null)
            }

        } else {
            Resource.error(response.message ?: "Error loading cities!", null)
        }

    }
     private fun mockCityMap(cityList: List<City>): TreeMap<String, City> {
        val map = TreeMap<String, City>()
        for (city in cityList) {
            map[city.mapKey.lowercase()] = city
        }
        return map
    }

}