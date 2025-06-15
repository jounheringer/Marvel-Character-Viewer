package com.reringuy.marvelcharacterviewer.presentation.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import coil3.compose.AsyncImage
import com.reringuy.marvelcharacterviewer.models.MarvelComic
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.reringuy.marvelcharacterviewer.models.MarvelCreator
import java.util.Locale

@Composable
fun MarvelComicDetailsScreen(
    modifier: Modifier,
    comic: MarvelComic,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(state = rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MarvelComicInfo(comic)

        MarvelComicCreatorsList(Modifier.align(Alignment.Start), comic.creators.items)
    }
}

@Composable
fun MarvelComicInfo(comic: MarvelComic) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncImage(
            model = comic.thumbnail.getFullPath().replace("http://", "https://"),
            contentDescription = "${comic.title} thumbnail"
        )
        Text(
            text = comic.title,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = comic.description,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun MarvelComicCreatorsList(modifier: Modifier, creator: List<MarvelCreator>) {
    Text(
        text = "Creators:",
        modifier = modifier,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.headlineSmall
    )

    creator.forEach { auxCreator ->
        val role = auxCreator.role.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
        Text(text = "${auxCreator.name} - $role")

    }
}