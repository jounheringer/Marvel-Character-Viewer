package com.reringuy.marvelcharacterviewer.presentation.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reringuy.marvelcharacterviewer.presentation.viewmodels.MarvelComicsViewModel
import com.reringuy.marvelcharacterviewer.ui.theme.MarvelCharacterViewerTheme
import androidx.compose.runtime.getValue
import com.reringuy.marvelcharacterviewer.models.MarvelCharacter
import com.reringuy.marvelcharacterviewer.models.MarvelComic
import com.reringuy.marvelcharacterviewer.presentation.components.CharacterInfo
import com.reringuy.marvelcharacterviewer.utils.OperationHandler

@Composable
fun MarvelComicsWrapper(
    modifier: Modifier,
    viewModel: MarvelComicsViewModel = hiltViewModel(),
    onCharacterNameLoaded: (String) -> Unit,
) {
    val comics by viewModel.comicsList.collectAsStateWithLifecycle()
    val currentCharacter by viewModel.currentCharacter.collectAsStateWithLifecycle()

    MarvelComicsScreen(modifier, comics, currentCharacter, onCharacterNameLoaded)
}

@Composable
fun MarvelComicsScreen(
    modifier: Modifier,
    comics: OperationHandler<List<MarvelComic>>,
    currentCharacter: OperationHandler<MarvelCharacter>,
    onCharacterNameLoaded: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (currentCharacter is OperationHandler.Success) {
            val character = currentCharacter.data
            onCharacterNameLoaded(character.name.substringBefore(" ("))
            CharacterInfo(character)
        } else

            Text("asdfasd")
    }
}


@Preview(showBackground = true)
@Composable
fun MarvelComicsWrapperPreview() {
    MarvelCharacterViewerTheme {
        MarvelComicsWrapper(Modifier) {}
    }
}