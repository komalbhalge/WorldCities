package com.kb.worldcities.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.kb.worldcities.ui.adaptor.CityListAdaptor
import com.kb.worldcities.ui.home.HomeFragment
import com.kb.worldcities.ui.map.DetailMapFragment
import javax.inject.Inject

class CityFragmentFactory @Inject constructor(
    private val cityListAdaptor: CityListAdaptor
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            HomeFragment::class.java.name -> HomeFragment(cityListAdaptor)
            DetailMapFragment::class.java.name -> DetailMapFragment()
            else -> super.instantiate(classLoader, className)
        }
    }
}