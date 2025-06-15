package com.reringuy.marvelcharacterviewer.utils

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.appwidget.state.updateAppWidgetState
import com.reringuy.marvelcharacterviewer.auth.TokenManager
import com.reringuy.marvelcharacterviewer.presentation.glance.GlanceWidget
import kotlinx.coroutines.flow.first

suspend fun refreshWidget(context: Context, tokenManager: TokenManager, widgetId: GlanceId) {
    val character = tokenManager.collectCharacter().first()
    updateAppWidgetState(context, widgetId) { prefs ->
        prefs[stringPreferencesKey("character_name")] = character?.name ?: ""
        prefs[stringPreferencesKey("character_url")] =
            character?.thumbnail?.getFullPath().toString()
    }
    GlanceWidget().update(context, widgetId)
}