package com.reringuy.marvelcharacterviewer.presentation.views

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reringuy.marvelcharacterviewer.models.MarvelCharacter
import com.reringuy.marvelcharacterviewer.presentation.components.Loading
import com.reringuy.marvelcharacterviewer.presentation.viewmodels.MarvelCharactersViewModel
import com.reringuy.marvelcharacterviewer.ui.theme.MarvelCharacterViewerTheme
import com.reringuy.marvelcharacterviewer.utils.OperationHandler

@Composable
fun MarvelCharactersWrapper(
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
            MarvelCharactersScreen(data) {

            }
        }

        else -> {
            Loading()
        }
    }
}

@Composable
fun MarvelCharactersScreen(
    characters: List<MarvelCharacter>,
    onCharacterClick: (MarvelCharacter) -> Unit,
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize(),
    ) {
        val (header, dropDown) = createRefs()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(header) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(12.dp, 0.dp)

        ) {
            Text(
                text = "Bem vindo ao Marvel Character Viewer",
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

        MarvelCharacterDropDown(modifier = Modifier.constrainAs(dropDown) {
            top.linkTo(header.bottom, margin = 16.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarvelCharacterDropDown(modifier: Modifier) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ){

    }
}

@Preview(showBackground = true)
@Composable
fun MarvelCharactersScreenPreview() {
    MarvelCharacterViewerTheme {
        MarvelCharactersScreen(emptyList()){

        }
    }
}


