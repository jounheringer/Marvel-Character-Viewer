package com.reringuy.marvelcharacterviewer.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reringuy.marvelcharacterviewer.models.MarvelCharacter
import com.reringuy.marvelcharacterviewer.repositories.MarvelRepository
import com.reringuy.marvelcharacterviewer.utils.OperationHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarvelCharactersViewModel @Inject constructor(
    private val marvelRepository: MarvelRepository,
) : ViewModel() {

    private val _characters = MutableStateFlow<OperationHandler<List<MarvelCharacter>>>(
        OperationHandler.Waiting
    )

    val characters = _characters.asStateFlow()

    init {
        getCharacters()
    }

    fun getCharacters() {
        _characters.value = OperationHandler.Loading
        viewModelScope.launch {
            try {
                val characters = marvelRepository.getCharacters()
                _characters.value = OperationHandler.Success(characters)
            } catch (e: Exception) {
                _characters.value = OperationHandler.Error(e.message ?: "Unknown error")
            }
        }
    }

}