package com.reringuy.marvelcharacterviewer.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reringuy.marvelcharacterviewer.auth.TokenManager
import com.reringuy.marvelcharacterviewer.models.MarvelCharacter
import com.reringuy.marvelcharacterviewer.models.MarvelComic
import com.reringuy.marvelcharacterviewer.repositories.MarvelRepository
import com.reringuy.marvelcharacterviewer.utils.OperationHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarvelComicsViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val marvelRepository: MarvelRepository,
) : ViewModel() {
    private val _currentCharacter =
        MutableStateFlow<OperationHandler<MarvelCharacter>>(OperationHandler.Waiting)
    private val _comicsList =
        MutableStateFlow<OperationHandler<List<MarvelComic>>>(OperationHandler.Waiting)
    val comicsList get() = _comicsList.asStateFlow()
    val currentCharacter get() = _currentCharacter.asStateFlow()

    init {
        getCurrenCharacter()
    }

    private fun getCharacterComics(characterId: Int) {
        _comicsList.value = OperationHandler.Loading
        viewModelScope.launch {
            try {
                val comics = marvelRepository.getCharacterComics(characterId)
                _comicsList.value = OperationHandler.Success(comics)
            } catch (e: Exception) {
                _comicsList.value = OperationHandler.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun getCurrenCharacter() {
        _currentCharacter.value = OperationHandler.Loading
        viewModelScope.launch {
            tokenManager.collectCharacter().collect {
                if (it != null) {
                    _currentCharacter.value = OperationHandler.Success(it)
                    getCharacterComics(it.id)
                } else
                    _currentCharacter.value = OperationHandler.Error("No character found")
            }
        }
    }
}