package com.reringuy.marvelcharacterviewer.presentation.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
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
    val comics = viewModel.comicsList?.collectAsLazyPagingItems()
    val currentCharacter by viewModel.currentCharacter.collectAsStateWithLifecycle()

    MarvelComicsScreen(modifier, comics, currentCharacter, onCharacterNameLoaded)
}

@Composable
fun MarvelComicsScreen(
    modifier: Modifier,
    comics: LazyPagingItems<MarvelComic>?,
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

        if (comics != null)
            LazyColumn {
                items(comics.itemCount) { index ->
                    val comic = comics[index]
                    if (comic != null)
                        Text(text = comic.title)
                }
                when (comics.loadState.refresh) {
                    is LoadState.Loading -> item { Text("Loading...") }
                    is LoadState.Error -> item { Text("Error") }
                    else -> Unit
                }
            }
    }
}


@Preview(showBackground = true)
@Composable
fun MarvelComicsWrapperPreview() {
    MarvelCharacterViewerTheme {
        MarvelComicsWrapper(Modifier) {}
    }
}