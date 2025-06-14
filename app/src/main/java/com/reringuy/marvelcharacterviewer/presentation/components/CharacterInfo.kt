package com.reringuy.marvelcharacterviewer.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.reringuy.marvelcharacterviewer.models.MarvelCharacter

@Composable
fun CharacterInfo(
    character: MarvelCharacter
) {
    AsyncImage(
        modifier = Modifier.size(128.dp),
        model = character.thumbnail.getFullPath().replace("http://", "https://"),
        contentScale = ContentScale.Crop,
        contentDescription = "${character.name} thumbnail"
    )
    Text(text = character.name, style = MaterialTheme.typography.titleLarge)
    Text(
        text = character.description,
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center
    )
}