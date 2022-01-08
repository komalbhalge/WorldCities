package com.kb.worldcities.data

import android.content.Context
import com.google.gson.Gson
import com.kb.worldcities.data.entities.City
import com.kb.worldcities.utils.Constants.Companion.DATA_FILE_ID
import com.kb.worldcities.utils.Resource
import java.util.*

class CitiesDataSourceImpl(private val context: Context) : CitiesDataSource {
    private var cityMap: TreeMap<String, City>? = null

    override fun getCityData(fileResID: Int): Resource<List<City>> {
        return try {
            val assetsReader = context.resources.openRawResource(fileResID).bufferedReader()
            val cityList = Gson().newBuilder().create()
                .fromJson(assetsReader, Array<City>::class.java).toList()
            Resource.success(
                data = cityList
            )
        } catch (exception: Exception) {
            Resource.error(
                msg = exception.message.toString(),
                data = null
            )
        }
    }

    override fun getCityMap(): Resource<TreeMap<String, City>> {
        return if (cityMap != null) {
            Resource.success(cityMap)
        } else {
            initCityMap()
        }
    }

    init {
        initCityMap()
    }

    private fun initCityMap(): Resource<TreeMap<String, City>> {
        val response = getCityData(DATA_FILE_ID)
        response.data?.let { cityList ->
            cityMap = TreeMap()
            for (city in cityList) {
                cityMap!![city.mapKey.lowercase()] = city
            }
        }
        return Resource.error(
            msg = "Unknown error occurred!",
            data = null
        )
    }

}