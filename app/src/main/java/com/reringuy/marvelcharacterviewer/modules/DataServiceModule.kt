package com.reringuy.marvelcharacterviewer.modules

import com.google.gson.Gson
import com.reringuy.marvelcharacterviewer.BuildConfig
import com.reringuy.marvelcharacterviewer.repositories.MarvelRepository
import com.reringuy.marvelcharacterviewer.services.MarvelService
import com.reringuy.marvelcharacterviewer.utils.CustomEnviroment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataServiceModule {

    @Singleton
    @Provides
    fun provideRetrofitBuilder(gson: Gson): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(CustomEnviroment.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))

    @Provides
    fun provideMarvelService(retrofit: Retrofit.Builder): MarvelService =
        retrofit.build().create(MarvelService::class.java)

    @Provides
    fun provideMarvelRepository(apiService: MarvelService): MarvelRepository = MarvelRepository(
        marvelService = apiService,
        publicKey = BuildConfig.MARVEL_PUBLIC_KEY,
        privateKey = BuildConfig.MARVEL_PRIVATE_KEY
    )
}