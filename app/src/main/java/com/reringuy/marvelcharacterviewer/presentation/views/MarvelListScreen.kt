package com.reringuy.marvelcharacterviewer.presentation.views

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.reringuy.marvelcharacterviewer.models.MarvelCharacter
import com.reringuy.marvelcharacterviewer.models.MarvelThumbnail
import com.reringuy.marvelcharacterviewer.presentation.components.Loading
import com.reringuy.marvelcharacterviewer.presentation.viewmodels.MarvelCharactersViewModel
import com.reringuy.marvelcharacterviewer.ui.theme.MarvelCharacterViewerTheme
import com.reringuy.marvelcharacterviewer.utils.OperationHandler

@Composable
fun MarvelCharactersWrapper(
    modifier: Modifier,
    viewModel: MarvelCharactersViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val charactersState by viewModel.characters.collectAsStateWithLifecycle()
    when (charactersState) {
        is OperationHandler.Error -> {
            val message = (charactersState as OperationHandler.Error).message
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        is OperationHandler.Success<*> -> {
            val data = (charactersState as OperationHandler.Success<List<MarvelCharacter>>).data
            MarvelCharactersScreen(modifier, data)
        }

        else -> {
            Loading()
        }
    }
}

@Composable
fun MarvelCharactersScreen(
    modifier: Modifier,
    characters: List<MarvelCharacter>,
) {
    var currentCharacter: MarvelCharacter? by remember { mutableStateOf(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.zIndex(2f)) {
            MarvelCharacterHeader()

            Spacer(Modifier.height(32.dp))

            MarvelCharacterDropDown(
                characters = characters,
                onCharacterClick = { currentCharacter = it }
            )
        }

        MarvelCharacterInfo(modifier = Modifier.align(Alignment.Center), currentCharacter)
    }
}

@Composable
fun MarvelCharacterInfo(modifier: Modifier, character: MarvelCharacter?) {
    character?.let {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = Modifier.size(128.dp),
                model = it.thumbnail.getFullPath().replace("http://", "https://"),
                contentScale = ContentScale.Crop,
                contentDescription = "${it.name} thumbnail"
            )
            Text(text = it.name, style = MaterialTheme.typography.titleLarge)
            Text(
                text = it.description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarvelCharacterDropDown(
    characters: List<MarvelCharacter>,
    onCharacterClick: (MarvelCharacter) -> Unit,
) {
    val commonInCardModifier = Modifier
        .fillMaxWidth()
        .padding(8.dp, 0.dp)
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("Selecione seu personagem") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(12.dp)
            ),
    ) {
        Card(
            shape = if (!expanded) CardDefaults.shape else RoundedCornerShape(
                topStart = 12.dp,
                topEnd = 12.dp
            ),
            onClick = { expanded = !expanded },
            colors = CardDefaults.cardColors().copy(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Row(
                modifier = commonInCardModifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = selectedText)
                IconButton(onClick = { expanded = !expanded }) {
                    if (!expanded)
                        Icon(Icons.Filled.KeyboardArrowUp, "Fechado")
                    else
                        Icon(Icons.Filled.KeyboardArrowDown, "Aberto")
                }
            }
        }

        if (expanded)
            LazyColumn(
                modifier = commonInCardModifier
                    .heightIn(max = 500.dp)
            ) {
                items(characters) {
                    MarvelCharacterOption(it) {
                        expanded = false
                        selectedText = it.name
                        onCharacterClick(it)
                    }
                }
            }
    }
}

@Composable
fun MarvelCharacterOption(character: MarvelCharacter, onCharacterClick: (MarvelCharacter) -> Unit) {
    TextButton(onClick = {
        onCharacterClick(character)
        Log.d("CharacterScreen.selected", character.name)
    }) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AsyncImage(
                modifier = Modifier.size(64.dp),
                model = character.thumbnail.getFullPath().replace("http://", "https://"),
                contentScale = ContentScale.Crop,
                contentDescription = "${character.name} thumbnail"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = character.name)
                Text(text = character.description, maxLines = 3, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
fun MarvelCharacterHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Bem-vindo ao Marvel Character Viewer",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Selecione seu personagem favorito e descubra tudo sobre ele.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MarvelCharactersScreenPreview() {
    val marvelCharacters = listOf<MarvelCharacter>(
        MarvelCharacter(
            id = 1,
            name = "Spider-Man",
            description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
            thumbnail = MarvelThumbnail(
                path = "https://i.annihil.us/u/prod/marvel/i/mg/c/20/52602f21f29ec",
                extension = "jpg"
            )
        ),
        MarvelCharacter(
            id = 2,
            name = "Iron Man",
            description = "Wounded, captured and forced to build a weapon...",
            thumbnail = MarvelThumbnail(
                path = "http://i.annihil.us/u/prod/marvel/i/mg/9/c0/527bb7b37ff55",
                extension = "jpg"
            )
        ),
        MarvelCharacter(
            id = 1,
            name = "Spider-Man",
            description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
            thumbnail = MarvelThumbnail(
                path = "https://i.annihil.us/u/prod/marvel/i/mg/c/20/52602f21f29ec",
                extension = "jpg"
            )
        ),
        MarvelCharacter(
            id = 2,
            name = "Iron Man",
            description = "Wounded, captured and forced to build a weapon...",
            thumbnail = MarvelThumbnail(
                path = "http://i.annihil.us/u/prod/marvel/i/mg/9/c0/527bb7b37ff55",
                extension = "jpg"
            )
        ),
        MarvelCharacter(
            id = 1,
            name = "Spider-Man",
            description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
            thumbnail = MarvelThumbnail(
                path = "https://i.annihil.us/u/prod/marvel/i/mg/c/20/52602f21f29ec",
                extension = "jpg"
            )
        ),
        MarvelCharacter(
            id = 2,
            name = "Iron Man",
            description = "Wounded, captured and forced to build a weapon...",
            thumbnail = MarvelThumbnail(
                path = "http://i.annihil.us/u/prod/marvel/i/mg/9/c0/527bb7b37ff55",
                extension = "jpg"
            )
        ),
        MarvelCharacter(
            id = 1,
            name = "Spider-Man",
            description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
            thumbnail = MarvelThumbnail(
                path = "https://i.annihil.us/u/prod/marvel/i/mg/c/20/52602f21f29ec",
                extension = "jpg"
            )
        ),
        MarvelCharacter(
            id = 2,
            name = "Iron Man",
            description = "Wounded, captured and forced to build a weapon...",
            thumbnail = MarvelThumbnail(
                path = "http://i.annihil.us/u/prod/marvel/i/mg/9/c0/527bb7b37ff55",
                extension = "jpg"
            )
        ),
    )
    MarvelCharacterViewerTheme {
        MarvelCharactersScreen(Modifier, marvelCharacters)
    }
}


@Preview(showBackground = true, name = "Marvel Character Info")
@Composable
fun MarvelCharacterInfoPreview() {
    val currentCharacter = MarvelCharacter(
        id = 1,
        name = "Spider-Man",
        description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
        thumbnail = MarvelThumbnail(
            path = "https://i.annihil.us/u/prod/marvel/i/mg/c/20/52602f21f29ec",
            extension = "jpg"
        )
    )
    MarvelCharacterViewerTheme {
        MarvelCharacterInfo(Modifier, currentCharacter)
    }
}

