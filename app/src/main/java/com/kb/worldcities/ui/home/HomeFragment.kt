package com.kb.worldcities.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.kb.worldcities.R
import com.kb.worldcities.data.entities.City
import com.kb.worldcities.databinding.HomeFragmentBinding
import com.kb.worldcities.ui.CityClickEvent
import com.kb.worldcities.ui.adaptor.CityListAdaptor
import com.kb.worldcities.ui.base.BaseFragment
import com.kb.worldcities.utils.extensions.bindViewModel
import javax.inject.Inject

class HomeFragment @Inject constructor(
    val listAdaptor: CityListAdaptor
) : BaseFragment(), CityClickEvent {

    private val vm: HomeViewModel by activityViewModels()
    private val viewModel by bindViewModel {
        vm
    }

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var cities: List<City>
    private var searchedInput = ""
    private var isSearching = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listAdaptor.setOnItemClickListener(this)
        setUpUI()
    }

    private fun setUpUI() {
        initSearchView()
        initCitiesRV()
        initCityList()
        observeData()
    }

    private fun observeData() {
        with(viewModel) {
            allCitiesResult.observe(viewLifecycleOwner) { cityList ->
                if (cityList.isNotEmpty()) {
                    onChangeEmptyLayoutVisibility(false)
                    cities = cityList
                    listAdaptor.setCityList(cities)
                } else {
                    onChangeEmptyLayoutVisibility(true)
                }
                binding.progressbar.visibility = View.GONE
            }

            isSearchingList.observe(viewLifecycleOwner) {
                isSearching = it
            }

            onAlertEvent.observe(viewLifecycleOwner) {
                binding.progressbar.visibility = View.GONE
                onChangeEmptyLayoutVisibility(true)
            }
        }
    }

    private fun initSearchView() {
        binding.citySearchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    return this.onQueryTextSubmit(newText)
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        searchedInput = it
                        viewModel.getCities(searchedInput)
                    }
                    return true
                }
            })
            setOnSearchClickListener {
                viewModel.getCities(searchedInput)
            }
            isIconified = false
        }
    }

    private fun initCitiesRV() {
        binding.citiesRv.apply {
            setHasFixedSize(true)
            adapter = listAdaptor
        }
    }

    private fun initCityList() {
        binding.progressbar.visibility = View.VISIBLE
        viewModel.getCities(searchedInput)
    }

    private fun onChangeEmptyLayoutVisibility(isVisible: Boolean) {
        binding.citiesRv.isVisible = !isVisible
        binding.tvEmptyList.isVisible = isVisible
        //If search result is empty update the message with dynamic searched text
        if (isSearching) {
            binding.tvEmptyList.setText(
                getString(R.string.message_no_search_result)
                    .plus("\n").plus(searchedInput)
            )
        }
    }

    override fun onItemClick(city: City) {
        val directions = HomeFragmentDirections.actionHomeToDetailMap(city)
        findNavController().navigate(directions)
    }
}