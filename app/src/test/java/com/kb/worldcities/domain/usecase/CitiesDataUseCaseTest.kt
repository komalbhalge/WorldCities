package com.kb.worldcities.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.kb.worldcities.data.CitiesDataSourceImpl
import com.kb.worldcities.data.CityRepositoryImpl
import com.kb.worldcities.data.entities.City
import com.kb.worldcities.data.entities.Coordinates
import com.kb.worldcities.domain.interfaces.CityRepository
import com.kb.worldcities.utils.Resource
import com.kb.worldcities.utils.Status
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class CitiesDataUseCaseTest {

    @Mock
    private lateinit var dataSource: CitiesDataSourceImpl

    @Mock
    private lateinit var cityRepository: CityRepository


    @Before
    fun setUp() {
        cityRepository = CityRepositoryImpl(dataSource)
    }

    //Happy Path//
    @Test
    fun `should return city list`() = runBlockingTest {
        whenever(dataSource.getCityMap()).thenReturn(Resource.success(data = mockCityMap()))
        val expectedSize = mockCityList().size
        val result = cityRepository.getAllCities().data
        assertThat(expectedSize).isEqualTo(result?.size)
    }

    @Test
    fun `should return city object for a searched query`() = runBlockingTest {
        val prefix = "k"

        whenever(dataSource.getCityMap()).thenReturn(Resource.success(data = mockCityMap()))
        val data = cityRepository.getCitiesStartingWith(prefix).data
        assertThat(data?.get(0)?._id).isEqualTo(12345)
    }

    @Test
    fun `should return empty city list when searched for non existent city`() = runBlockingTest {

        val prefix = "z"

        whenever(dataSource.getCityMap()).thenReturn(Resource.success(data = mockCityMap()))
        val data = cityRepository.getCitiesStartingWith(prefix).data
        assertThat(data).isEqualTo(listOf<City>())
    }

    //Negative Path//
    @Test
    fun `get cities failed with exception`() = runBlockingTest {
        whenever(dataSource.getCityMap()).thenReturn(Resource.error("", null))

        val result = cityRepository.getAllCities()
        assertThat(result.status).isEqualTo(Status.ERROR)
    }

    private fun mockCityMap(): TreeMap<String, City> {
        val map = TreeMap<String, City>()
        for (city in mockCityList()) {
            map[city.mapKey.lowercase()] = city
        }
        return map
    }

    private fun mockCityList() = listOf(
        City(
            _id = 123,
            name = "Kuala Lumpur",
            country = "MY",
            coord = Coordinates(lon = 1.1, lat = 1.2),

            ), City(
            _id = 1234,
            name = "Putrajaya",
            country = "MY",
            Coordinates(lon = 1.1, lat = 1.2)
        ),
        City(
            _id = 12345,
            name = "Ka Bang",
            country = "TH",
            Coordinates(lon = 6.4, lat = 101.2)
        )
    )
}