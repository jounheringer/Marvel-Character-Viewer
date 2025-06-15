package com.reringuy.marvelcharacterviewer.presentation.glance

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.padding
import androidx.glance.layout.size
import com.reringuy.marvelcharacterviewer.CharacterComicsActivity
import com.reringuy.marvelcharacterviewer.MainActivity
import com.reringuy.marvelcharacterviewer.R


class GlanceWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val prefs = currentState<Preferences>()
            val name = prefs[stringPreferencesKey("character_name")]
            GlanceWidgetScreen(name)
        }
    }


    @Composable
    fun GlanceWidgetScreen(
        name: String?,
    ) {
        Column(
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = GlanceModifier.padding(8.dp)
        ) {
            Image(
                modifier = GlanceModifier.size(128.dp).padding(0.dp, 12.dp),
                provider = ImageProvider(R.drawable.marvel_character_viewer_logo),
                contentDescription = "$name thumbnail"
            )
            if (name != null) {
                Button(
                    "Veja quadrinhos de $name",
                    actionStartActivity<CharacterComicsActivity>()

                )
            } else
                Button(
                    "Venha dizer qual é o seu personagem favorito.",
                    actionStartActivity<MainActivity>()
                )
        }

    }
}