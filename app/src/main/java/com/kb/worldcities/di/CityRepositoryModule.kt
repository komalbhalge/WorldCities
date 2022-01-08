package com.kb.worldcities.di

import android.content.Context
import androidx.fragment.app.FragmentFactory
import com.kb.worldcities.data.CitiesDataSource
import com.kb.worldcities.data.CitiesDataSourceImpl
import com.kb.worldcities.data.CityRepositoryImpl
import com.kb.worldcities.domain.interfaces.CityRepository
import com.kb.worldcities.ui.CityFragmentFactory
import com.kb.worldcities.ui.adaptor.CityListAdaptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CityRepositoryModule {

    @Provides
    @Singleton
    fun provideCitiesDataSource(@ApplicationContext context: Context): CitiesDataSource =
        CitiesDataSourceImpl(context)

    @Provides
    @Singleton
    fun provideCityRepository(citiesDataSource: CitiesDataSource): CityRepository =
        CityRepositoryImpl(citiesDataSource)

    @Provides
    @Singleton
    fun provideAdapter(): CityListAdaptor = CityListAdaptor()

    @Provides
    fun provideFragmentFactory(cityListAdaptor: CityListAdaptor): FragmentFactory =
        CityFragmentFactory(cityListAdaptor)

}