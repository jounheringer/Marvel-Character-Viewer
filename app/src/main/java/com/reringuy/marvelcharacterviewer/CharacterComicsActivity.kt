package com.reringuy.marvelcharacterviewer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.reringuy.marvelcharacterviewer.models.MarvelComic
import com.reringuy.marvelcharacterviewer.presentation.views.MarvelComicsWrapper
import com.reringuy.marvelcharacterviewer.ui.theme.MarvelCharacterViewerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterComicsActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var topBarTitle by remember { mutableStateOf("Character Comics") }
            MarvelCharacterViewerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(title = { Text(topBarTitle) }, navigationIcon = {
                            IconButton(onClick = { goBack() }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        })
                    }
                ) { innerPadding ->
                    MarvelComicsWrapper(
                        modifier = Modifier.padding(innerPadding),
                        onCharacterNameLoaded = { topBarTitle = "$it Comics" },
                        onComicSaved = { goToComicsDetail(it) }
                    )
                }
            }
        }
    }

    private fun goToComicsDetail(comic: MarvelComic) {
        val intent = Intent(this, ComicDetailsActivity::class.java)
        intent.putExtra("comic", comic)
        startActivity(intent)
        finish()
    }

    private fun goBack() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}