package com.reringuy.marvelcharacterviewer.modules

import com.google.gson.Gson
import com.reringuy.marvelcharacterviewer.auth.AuthInterceptor
import com.reringuy.marvelcharacterviewer.services.MarvelService
import com.reringuy.marvelcharacterviewer.utils.CustomEnviroment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataServiceModule {
    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(45, TimeUnit.SECONDS)
        .readTimeout(45, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun provideRetrofitBuilder(gson: Gson, okHttpClient: OkHttpClient): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(CustomEnviroment.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))

    @Provides
    fun provideMarvelService(retrofit: Retrofit.Builder): MarvelService =
        retrofit.build().create(MarvelService::class.java)
}