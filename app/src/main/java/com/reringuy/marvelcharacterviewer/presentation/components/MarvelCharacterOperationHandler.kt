package com.reringuy.marvelcharacterviewer.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.reringuy.marvelcharacterviewer.utils.OperationHandler

@Composable
fun <T> MarvelCharacterOperationHandler(
    entity: OperationHandler<T>,
    modifier: Modifier,
    content: @Composable (OperationHandler.Success<T>) -> Unit,
) {
    when (entity) {
        is OperationHandler.Error -> {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = entity.message,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
            }
        }

        OperationHandler.Loading -> {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                Loading()
            }
        }

        is OperationHandler.Success<*> -> {
            content(entity as OperationHandler.Success<T>)
        }

        OperationHandler.Waiting -> {}
    }
}