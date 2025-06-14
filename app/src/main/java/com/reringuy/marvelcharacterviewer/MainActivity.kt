package com.reringuy.marvelcharacterviewer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.reringuy.marvelcharacterviewer.presentation.views.MarvelCharactersWrapper
import com.reringuy.marvelcharacterviewer.ui.theme.MarvelCharacterViewerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MarvelCharacterViewerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MarvelCharactersWrapper(Modifier.padding(innerPadding)) {
                        goToInfoActivity()
                    }
                }
            }
        }
    }

    private fun goToInfoActivity() {
        val intent = Intent(this, CharacterComicsActivity::class.java)
        startActivity(intent)
        finish()
    }

}