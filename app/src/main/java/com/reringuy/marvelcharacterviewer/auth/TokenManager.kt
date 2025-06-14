package com.reringuy.marvelcharacterviewer.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.reringuy.marvelcharacterviewer.models.MarvelCharacter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TokenManager(
    private val context: Context,
) {
    companion object {
        val CURRENT_CHARACTER_KEY = stringPreferencesKey("current_character")
    }

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data_store")


    suspend fun saveCharacter(character: MarvelCharacter) {
        context.dataStore.edit { preferences ->
            preferences[CURRENT_CHARACTER_KEY] = Gson().toJson(character)
        }
    }

    fun collectCharacter(): Flow<MarvelCharacter?> {
        return context.dataStore.data.map { preferences ->
            Gson().fromJson(preferences[CURRENT_CHARACTER_KEY], MarvelCharacter::class.java)
        }
    }
}