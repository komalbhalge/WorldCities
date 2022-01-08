package com.kb.worldcities.di

import com.kb.worldcities.ui.CityFragmentFactory
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@EntryPoint
@InstallIn(ActivityComponent::class)
interface ListFragmentFactoryEntryPoint {
    fun getFragmentFactory(): CityFragmentFactory
}