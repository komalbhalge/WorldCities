package com.kb.worldcities.ui.map

import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.UiSelector
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.kb.worldcities.FakeCityData
import com.kb.worldcities.R
import com.kb.worldcities.launchFragmentInHiltContainer
import com.kb.worldcities.ui.CityFragmentFactory
import com.kb.worldcities.ui.home.HomeFragmentDirections
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class DetailMapFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    val LIST_ITEM_IN_TEST = 3
    val CITY_IN_TEST = FakeCityData.cities[LIST_ITEM_IN_TEST]

    @Inject
    lateinit var cityFragmentFactory: CityFragmentFactory

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun test_isDetailMapFragmentVisible() {
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<DetailMapFragment>(
            fragmentFactory = cityFragmentFactory,
            fragmentArgs = HomeFragmentDirections.actionHomeToDetailMap(CITY_IN_TEST).arguments
        ) {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.map)).check(matches(isDisplayed()))
    }

    @Test
    fun testMapMarker() {
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<DetailMapFragment>(
            fragmentFactory = cityFragmentFactory,
            fragmentArgs = HomeFragmentDirections.actionHomeToDetailMap(CITY_IN_TEST).arguments
        ) {
            Navigation.setViewNavController(requireView(), navController)
        }

        val twoSeconds = 2000
        val fiveSeconds = 5000

        // Wait for the Map View to load
        runBlocking {
            delay(fiveSeconds.toLong())
        }

        // First of all, click on the Marker, using UIAutomator
        val device = UiDevice.getInstance(getInstrumentation())
        val marker = device.findObject(UiSelector().descriptionContains(CITY_IN_TEST.displayName))
        marker.click()

        // After a marker is clicked, the MapView will automatically move the map
        // to position the Marker at the center of the screen
        // So wait for the animation to finish first.

        runBlocking {
            delay(twoSeconds.toLong())
        }
    }
}