package com.kb.worldcities.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kb.worldcities.data.entities.City
import com.kb.worldcities.domain.usecase.CitiesDataUseCase
import com.kb.worldcities.ui.base.BaseViewModel
import com.kb.worldcities.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val citiesDataUseCase: CitiesDataUseCase
) : BaseViewModel() {
    private val _allCitiesResult = MutableLiveData<List<City>>()
    private val _isSearchingList = MutableLiveData<Boolean>()
    val allCitiesResult: LiveData<List<City>> = _allCitiesResult
    val isSearchingList: LiveData<Boolean> = _isSearchingList

    fun getCities(input: String) {
        viewModelScope.launch {
            if (input.isBlank()) {
                val response = citiesDataUseCase.getAllCities()
                if (response.status == Status.SUCCESS) {
                    response.data?.let { _allCitiesResult.postValue(it) }
                } else {
                    showPopup(message = response.message.toString())
                }
            } else {
                _isSearchingList.postValue(true)
                val response = citiesDataUseCase.getCitiesStartingWith(input)
                if (response.status == Status.SUCCESS) {
                    response.data?.let { _allCitiesResult.postValue(it) }
                } else {
                    showPopup(message = response.message.toString())
                }
            }
        }
    }

}