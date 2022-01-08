package com.kb.worldcities

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.kb.worldcities.data.entities.City
import com.kb.worldcities.data.entities.Coordinates
import com.kb.worldcities.ui.base.BaseViewModel
import com.kb.worldcities.utils.Alert
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Base class for testing ViewModels.
 *
 * The ViewModel under test will be accessible via the property [vm].
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
abstract class BaseViewModelTests<VM : BaseViewModel> {

    protected lateinit var vm: VM

    @ExperimentalCoroutinesApi
    protected val testDispatcher = TestCoroutineDispatcher()

    /**
     * This bypasses the main thread check, and immediately runs any tasks on your test thread,
     * allowing for immediate and predictable calls and therefore assertions
     */
    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var onAlertEventObserver: Observer<Alert>

    @ExperimentalCoroutinesApi
    @Rule
    @JvmField
    var coroutineDispatcherRule = CoroutineDispatcherRule(testDispatcher)

    /**
     * Initialize and return the view model under test here.
     */
    abstract fun createViewModel(): VM

    @Before
    fun setupViewModel() {
        vm = createViewModel()
        vm.onAlertEvent.observeForever(onAlertEventObserver)
    }

    /* Common test data */
    val mockedCityList = mutableListOf(
        City(
            _id = 123,
            name = "Albuquerque",
            country = "US",
            Coordinates(lon = 1.1, lat = 1.2)
        ), City(
            _id = 1234,
            name = "Putrajaya",
            country = "MY",
            Coordinates(lon = 1.1, lat = 1.2)
        ),
        City(
            _id = 12345,
            name = "Alabama",
            country = "TH",
            Coordinates(lon = 10.4, lat = 3.2)
        ),
        City(
            _id = 12345,
            name = "Cape Town",
            country = "SA",
            Coordinates(lon = 10.4, lat = 3.2)
        )
    )

    fun <T> merge(first: List<T>, second: List<T>): List<T> {
        return first.plus(second)
    }

}
