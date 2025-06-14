package com.reringuy.marvelcharacterviewer.modules

import android.content.Context
import com.google.gson.Gson
import com.reringuy.marvelcharacterviewer.auth.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SingletonModules {
    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context) = TokenManager(context)

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()
}