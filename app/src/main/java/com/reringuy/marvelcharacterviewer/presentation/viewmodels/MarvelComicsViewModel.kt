package com.reringuy.marvelcharacterviewer.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.reringuy.marvelcharacterviewer.auth.TokenManager
import com.reringuy.marvelcharacterviewer.models.MarvelCharacter
import com.reringuy.marvelcharacterviewer.models.MarvelComic
import com.reringuy.marvelcharacterviewer.repositories.MarvelRepository
import com.reringuy.marvelcharacterviewer.utils.OperationHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarvelComicsViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val marvelRepository: MarvelRepository,
) : ViewModel() {
    private var _comicsList: Flow<PagingData<MarvelComic>>? = null
    val comicsList get() = _comicsList

    private val _currentCharacter =
        MutableStateFlow<OperationHandler<MarvelCharacter>>(OperationHandler.Waiting)

    val currentCharacter get() = _currentCharacter.asStateFlow()

    init {
        getCurrenCharacter()
    }

    fun getCharacterComics(characterId: Int) {
        viewModelScope.launch {
            try {
                val response = marvelRepository.getCharacterComics(characterId)
                _comicsList = response.cachedIn(viewModelScope)
            } catch (e: Exception) {
                Log.e("MarvelComicsViewModel", "Error fetching comics: ${e.message}")
                _comicsList = null
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