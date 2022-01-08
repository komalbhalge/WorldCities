package com.kb.worldcities.ui.home

import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.kb.worldcities.BaseViewModelTests
import com.kb.worldcities.data.CitiesDataSourceImpl
import com.kb.worldcities.data.CityRepositoryImpl
import com.kb.worldcities.data.entities.City
import com.kb.worldcities.data.entities.Coordinates
import com.kb.worldcities.domain.interfaces.CityRepository
import com.kb.worldcities.domain.usecase.CitiesDataUseCase
import com.kb.worldcities.getOrAwaitValue
import com.kb.worldcities.utils.Alert
import com.kb.worldcities.utils.Resource
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verify
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
class HomeViewModelTest : BaseViewModelTests<HomeViewModel>() {

    @Mock
    private lateinit var citiesDataUseCase: CitiesDataUseCase

    @Mock
    private lateinit var dataSource: CitiesDataSourceImpl

    @Mock
    private lateinit var cityRepository: CityRepository

    @Mock
    private lateinit var allCitiesResultObserver: Observer<List<City>>


    override fun createViewModel(): HomeViewModel {
        return HomeViewModel(citiesDataUseCase = citiesDataUseCase)
    }

    @Before
    fun setUp() {
        cityRepository = CityRepositoryImpl(dataSource)
        with(vm) {
            allCitiesResult.observeForever(allCitiesResultObserver)
        }
    }

    //Happy Path//
    @Test
    fun `get all cities success`() = runBlockingTest {
        whenever(citiesDataUseCase.getAllCities()).thenReturn(Resource.success(mockedCityList))
        with(vm) {
            getCities(EMPTY_SEARCH_INPUT)

            val expectedResultSize = mockedCityList.size
            val actualCitiesResultSize = allCitiesResult.getOrAwaitValue().size
            assertThat(actualCitiesResultSize == expectedResultSize)
        }
    }

    @Test
    fun `get all cities with prefix`() = runBlockingTest {
        val prefix = "a"

        whenever(dataSource.getCityMap()).thenReturn(
            Resource.success(data = mockCityMap(mockedCityList))
        )
        val data = cityRepository.getCitiesStartingWith(prefix).data

        whenever(citiesDataUseCase.getCitiesStartingWith(prefix)).thenReturn(Resource.success(data))
        with(vm) {
            getCities(prefix)
            val actualCitiesResult = allCitiesResult.getOrAwaitValue()
            //Mocked list has 2 city items with city name starting with 'k'
            assertThat(actualCitiesResult.size).isEqualTo(2)
        }
    }

    @Test
    fun `should return search result in correct alphabetical order`() = runBlockingTest {
        val prefix = "al"
        val testCityObj = City(234, "Arizona", "US", Coordinates(2.0, 9.0))
        //Add special test object to a base mockList
        val testList = merge(mockedCityList, listOf(testCityObj))

        whenever(dataSource.getCityMap()).thenReturn(
            Resource.success(data = mockCityMap(testList))
        )
        val data = cityRepository.getCitiesStartingWith(prefix).data

        whenever(citiesDataUseCase.getCitiesStartingWith(prefix)).thenReturn(Resource.success(data))
        with(vm) {
            getCities(prefix)
            val actualCitiesResult = allCitiesResult.getOrAwaitValue()
            //Mocked list only has two cities matching 'al' :Albuquerque, Alabama
            assertThat(actualCitiesResult.size).isEqualTo(2)

            /*Addition check*/
            // check if actual result contains the test object
            val isFound = actualCitiesResult.any { it._id == testCityObj._id }
            assertThat(isFound).isFalse()
        }
    }

    @Test
    fun `should not return a city with prefix matching to country name`() = runBlockingTest {
        val prefix = "A"
        val testCityObj = City(234, "Sydney", "AU", Coordinates(2.0, 9.0))
        //Add special test object to a base mockList
        val testList = merge(mockedCityList, listOf(testCityObj))

        whenever(dataSource.getCityMap()).thenReturn(
            Resource.success(data = mockCityMap(testList))
        )
        val data = cityRepository.getCitiesStartingWith(prefix).data

        whenever(citiesDataUseCase.getCitiesStartingWith(prefix)).thenReturn(
            Resource.success(data)
        )
        with(vm) {
            getCities(prefix)
            val actualCitiesResult = allCitiesResult.getOrAwaitValue()
            //check if actual result contains the test object
            val isCityPresent = actualCitiesResult.contains(testCityObj)
            assertThat(isCityPresent).isFalse()
        }
    }

    @Test
    fun `should only return a city where prefix matches to a city name`() = runBlockingTest {
        val prefix = "S"
        val testCityObj = City(234, "Sydney", "AU", Coordinates(2.0, 9.0))
        //Add special test object to a base mockList
        val testList = merge(mockedCityList, listOf(testCityObj))

        whenever(dataSource.getCityMap()).thenReturn(
            Resource.success(data = mockCityMap(testList))
        )
        val data = cityRepository.getCitiesStartingWith(prefix).data

        whenever(citiesDataUseCase.getCitiesStartingWith(prefix)).thenReturn(
            Resource.success(data)
        )
        with(vm) {
            getCities(prefix)
            val actualCitiesResult = allCitiesResult.getOrAwaitValue()
            //Mocked list has a city items with COUNRTY name starting with 'S'
            val isFound = actualCitiesResult.any { it._id == testCityObj._id }
            assertThat(isFound).isTrue()
        }
    }

    //Negative Path//
    @Test
    fun `should display error popup when city list returns Error`() = runBlockingTest {
        val popup = argumentCaptor<Alert>()
        whenever(citiesDataUseCase.getAllCities()).thenReturn(
            Resource.error(
                "Error loading cities!",
                null
            )
        )
        vm.getCities("")

        verify(onAlertEventObserver).onChanged(popup.capture())
        assertThat(popup.firstValue).isInstanceOf(Alert.Popup::class.java)
        with(popup.firstValue as Alert.Popup) {
            assertThat(message).isEqualTo("Error loading cities!")
        }
    }

    @Test
    fun `should display error popup when search city returns Error`() = runBlockingTest {
        val popup = argumentCaptor<Alert>()
        val prefix = "random search"

        whenever(citiesDataUseCase.getCitiesStartingWith(prefix)).thenReturn(
            Resource.error(
                "Error loading cities!",
                null
            )
        )
        vm.getCities(prefix)

        verify(onAlertEventObserver).onChanged(popup.capture())
        assertThat(popup.firstValue).isInstanceOf(Alert.Popup::class.java)
        with(popup.firstValue as Alert.Popup) {
            assertThat(message).isEqualTo("Error loading cities!")
        }
    }

    private fun mockCityMap(cityList: List<City>): TreeMap<String, City> {
        val map = TreeMap<String, City>()
        for (city in cityList) {
            map[city.mapKey.lowercase()] = city
        }
        return map
    }

    companion object {
        private const val EMPTY_SEARCH_INPUT = ""
    }

}