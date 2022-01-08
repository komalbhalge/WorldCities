package com.kb.worldcities.ui.home

import android.view.KeyEvent
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.kb.worldcities.FakeCityData
import com.kb.worldcities.R
import com.kb.worldcities.launchFragmentInHiltContainer
import com.kb.worldcities.ui.CityFragmentFactory
import com.kb.worldcities.ui.adaptor.CityViewHolder
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class HomeFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    val LIST_ITEM_IN_TEST = 3
    val CITY_IN_TEST = FakeCityData.cities[LIST_ITEM_IN_TEST]

    private var itemCount = 0

    @Inject
    lateinit var cityFragmentFactory: CityFragmentFactory


    @Before
    fun setup() {
        hiltRule.inject()
    }

    /*
    * Recyclerview comes into view
    * */
    @Test
    fun test_isHomeFragmentVisible_onAppLaunch() {
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<HomeFragment>(fragmentFactory = cityFragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
        }
        onView(withId(R.id.citiesRv)).check(matches(isDisplayed()))
    }


    /*
    * Select list item nav to DetailMapFragment
    * pressBack
    * */
    @Test
    fun test_navigateBackToHomeFragmentFromDetailFragment() {
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<HomeFragment>(fragmentFactory = cityFragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)

        }

        onView(withId(R.id.citiesRv)).perform(
            actionOnItemAtPosition<CityViewHolder>(
                LIST_ITEM_IN_TEST, click()
            )
        )
        pressBack()
        onView(withId(R.id.citiesRv)).check(matches(isDisplayed()))
    }

    /*
       * Select list item nav to DetailMapFragment
       * Correct City is in view?
       * */
    @Test
    fun testClickCityListItem_navigateToMapView() {
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<HomeFragment>(fragmentFactory = cityFragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
            listAdaptor.setCityList(FakeCityData.cities)
        }

        onView(withId(R.id.citiesRv)).perform(
            actionOnItemAtPosition<CityViewHolder>(
                LIST_ITEM_IN_TEST, click()
            )
        )
        //verify(navController).navigate(HomeFragmentDirections.actionHomeToDetailMap(CITY_IN_TEST))
    }

    @Test
    fun test_searchCorrectCityName() {
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<HomeFragment>(fragmentFactory = cityFragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.citySearchView)).perform(click()) //open the searchView

        onView(withId(R.id.search_src_text)).perform(typeText("Sydney"))// the text was input

        onView(withId(R.id.citySearchView)).perform(pressKey(KeyEvent.KEYCODE_ENTER))  // starting the object  search

        onView(withId(R.id.citiesRv))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(3, scrollTo()))
            .check(matches(hasDescendant(withText("Sydney, AU"))))
    }

    @Test
    fun test_NoSearchResultText_VisibilityWIthExactText() {
        val navController = mock(NavController::class.java)
        val search_prefix = "7878"

        launchFragmentInHiltContainer<HomeFragment>(fragmentFactory = cityFragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
        }
        onView(withId(R.id.citySearchView)).perform(click()) //open the searchView

        onView(withId(R.id.search_src_text)).perform(typeText(search_prefix))// the text was input

        onView(withId(R.id.citySearchView)).perform(pressKey(KeyEvent.KEYCODE_ENTER))  // starting the object  search

        val res = getInstrumentation().getTargetContext().getResources()
        val expectedMessage =
            res.getString(R.string.message_no_search_result).plus("\n" + search_prefix)

        onView(withId(R.id.tvEmptyList)).check(matches(withText(expectedMessage)))

    }

    @Test
    fun test_ResultCount_afterSearch() {
        val navController = mock(NavController::class.java)
        val search_prefix = "6"
        launchFragmentInHiltContainer<HomeFragment>(fragmentFactory = cityFragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.citySearchView)).perform(click()) //open the searchView

        onView(withId(R.id.search_src_text)).perform(typeText(search_prefix))// the text was input

        onView(withId(R.id.citySearchView)).perform(pressKey(KeyEvent.KEYCODE_ENTER))  // starting the object  search


        onView(withId(R.id.citiesRv)).check(matches(isDisplayed()))
        onView(withId(R.id.citiesRv)).check(matches(hasChildCount(1)))
    }
}
