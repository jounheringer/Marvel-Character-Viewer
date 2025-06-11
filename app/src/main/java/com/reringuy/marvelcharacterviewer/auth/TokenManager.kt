package com.reringuy.marvelcharacterviewer.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TokenManager(
    private val context: Context,
) {
    companion object {
        val TOKEN_KEY = stringPreferencesKey("token")
    }

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data_store")


    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }

    fun collectToken(): Flow<String?> {
        return context.dataStore.data.map { preferences -> preferences[TOKEN_KEY] }
    }
}