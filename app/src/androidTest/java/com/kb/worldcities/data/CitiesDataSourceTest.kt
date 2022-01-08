package com.kb.worldcities.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.google.common.truth.Truth.assertThat
import com.kb.worldcities.test.R
import com.kb.worldcities.utils.Status
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class CitiesDataSourceTest {
    private lateinit var dataSource: CitiesDataSource

    @Before
    fun setUp() {
        dataSource = CitiesDataSourceImpl(getInstrumentation().context)
    }

    @Test
    fun fetchCitiesData() {
        val data = dataSource.getCityData(R.raw.cities_test).data
        assertThat(data?.size).isEqualTo(10)
    }

    @Test
    fun throwExceptionWhileRetrievingDataFromFile() {
        /*
            To test 'any' kind of Exception  while retrieving file data,
            added a faulty json containing string in place of double,
            resulting in NumberFormatException thus returns Error status
         */
        val result = dataSource.getCityData(R.raw.faulty_data)
        assertThat(result.status).isEqualTo(Status.ERROR)
    }

}