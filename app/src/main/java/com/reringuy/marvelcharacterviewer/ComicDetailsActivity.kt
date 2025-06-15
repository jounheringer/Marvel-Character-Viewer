package com.reringuy.marvelcharacterviewer

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.Modifier
import com.reringuy.marvelcharacterviewer.models.MarvelComic
import com.reringuy.marvelcharacterviewer.presentation.views.MarvelComicDetailsScreen
import com.reringuy.marvelcharacterviewer.ui.theme.MarvelCharacterViewerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComicDetailsActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val comic = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                intent.getParcelableExtra("comic", MarvelComic::class.java)
            else{
                @Suppress("DEPRECATION")
                intent.getParcelableExtra<MarvelComic>("comic")
            }

            if (comic == null){
                Log.e("ComicDetailsActivity", "Comic is null")
                Toast.makeText(this, "Comic not found", Toast.LENGTH_SHORT).show()
                goBack()
            }

            var topBarTitle = "#${comic!!.issueNumber} ${comic.title.substringBefore(" #")}"
            Log.d("ComicDetailsActivity", "Comic: $topBarTitle")
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
                    MarvelComicDetailsScreen(
                        modifier = Modifier.padding(innerPadding),
                        comic = comic
                    )
                }
            }
        }
    }

    private fun goBack() {
        val intent = Intent(this, CharacterComicsActivity::class.java)
        startActivity(intent)
        finish()
    }
}