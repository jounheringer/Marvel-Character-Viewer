package com.reringuy.marvelcharacterviewer.presentation.views

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reringuy.marvelcharacterviewer.presentation.viewmodels.MarvelComicsViewModel
import com.reringuy.marvelcharacterviewer.ui.theme.MarvelCharacterViewerTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.reringuy.marvelcharacterviewer.models.MarvelCharacter
import com.reringuy.marvelcharacterviewer.models.MarvelComic
import com.reringuy.marvelcharacterviewer.models.MarvelCreators
import com.reringuy.marvelcharacterviewer.models.MarvelThumbnail
import com.reringuy.marvelcharacterviewer.presentation.components.CharacterInfo
import com.reringuy.marvelcharacterviewer.utils.OperationHandler
import kotlinx.coroutines.flow.flowOf

@Composable
fun MarvelComicsWrapper(
    modifier: Modifier,
    viewModel: MarvelComicsViewModel = hiltViewModel(),
    onCharacterNameLoaded: (String) -> Unit,
    onComicSaved: (MarvelComic) -> Unit,
) {
    val comics = viewModel.comicsList?.collectAsLazyPagingItems()
    val currentCharacter by viewModel.currentCharacter.collectAsStateWithLifecycle()
    val savedEffect = viewModel.effect

    LaunchedEffect(savedEffect) {
        savedEffect.collect {
            if (it != null){
                Log.d("MarvelCharacter.Effect", it.title)
                onComicSaved(it)
            } else
                Log.d("MarvelCharacter.Effect", "Error")
        }
    }

    MarvelComicsScreen(
        modifier,
        comics,
        currentCharacter,
        onCharacterNameLoaded,
        viewModel::setCurrentComic
    )
}

@Composable
fun MarvelComicsScreen(
    modifier: Modifier,
    comics: LazyPagingItems<MarvelComic>?,
    currentCharacter: OperationHandler<MarvelCharacter>,
    onCharacterNameLoaded: (String) -> Unit,
    onComicSelected: (MarvelComic) -> Unit,
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
            MarvelComicsList(comics, onComicSelected)
    }
}

@Composable
fun MarvelComicsList(comics: LazyPagingItems<MarvelComic>, onComicSelected: (MarvelComic) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(comics.itemCount) { index ->
            val comic = comics[index]
            if (comic != null)
                MarvelComicOption(comic, onComicSelected)

        }
        when (comics.loadState.refresh) {
            is LoadState.Loading -> item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Loading...")
                }
            }

            is LoadState.Error -> item { Text("Error") }
            else -> Unit
        }
    }
}

@Composable
fun MarvelComicOption(comic: MarvelComic, onComicSelected: (MarvelComic) -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onComicSelected(comic) },
        shape = CutCornerShape(0.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                modifier = Modifier.size(128.dp),
                model = comic.thumbnail.getFullPath().replace("http://", "https://"),
                contentDescription = "${comic.title} thumbnail"
            )
            Column {
                Text(text = comic.title)
                Text(text = comic.description, maxLines = 3, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MarvelComicsWrapperPreview() {
    MarvelCharacterViewerTheme {
        val dummyComics = flowOf(
            PagingData.from(
                listOf(
                    MarvelComic(
                        1, "Comic 1", "Description 1", 1f, MarvelThumbnail("path1", "jpg"),
                        MarvelCreators(emptyList())
                    ),
                    MarvelComic(
                        2, "Comic 2", "Description 2", 2f, MarvelThumbnail("path2", "jpg"),
                        MarvelCreators(emptyList())
                    ),
                    MarvelComic(
                        3, "Comic 3", "Description 3", 3f, MarvelThumbnail("path3", "jpg"),
                        MarvelCreators(emptyList())
                    ),
                    MarvelComic(
                        1, "Comic 1", "Description 1", 1f, MarvelThumbnail("path1", "jpg"),
                        MarvelCreators(emptyList())
                    ),
                )
            )
        ).collectAsLazyPagingItems()

        val dummyCharacter = MarvelCharacter(
            id = 1,
            name = "Spider-Man (Peter Parker)",
            description = "Bitten by a radioactive spider, Peter Parker’s arachnid abilities give him amazing powers he uses to help others, while his personal life continues to offerPlenty of obstacles.",
            thumbnail = MarvelThumbnail(
                path = "http://i.annihil.us/u/prod/marvel/i/mg/3/50/526548a343e4b",
                extension = "jpg"
            )
        )

        MarvelComicsScreen(
            modifier = Modifier.fillMaxSize(),
            comics = dummyComics,
            currentCharacter = OperationHandler.Success(dummyCharacter),
            onCharacterNameLoaded = {
            }
        ) {}
    }
}
