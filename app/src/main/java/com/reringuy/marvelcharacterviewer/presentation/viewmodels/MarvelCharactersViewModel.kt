package com.reringuy.marvelcharacterviewer.presentation.viewmodels

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reringuy.marvelcharacterviewer.auth.TokenManager
import com.reringuy.marvelcharacterviewer.models.MarvelCharacter
import com.reringuy.marvelcharacterviewer.presentation.glance.GlanceWidget
import com.reringuy.marvelcharacterviewer.repositories.MarvelRepository
import com.reringuy.marvelcharacterviewer.utils.OperationHandler
import com.reringuy.marvelcharacterviewer.utils.refreshWidget
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarvelCharactersViewModel @Inject constructor(
    private val marvelRepository: MarvelRepository,
    private val tokenManager: TokenManager,
) : ViewModel() {

    private val _characters =
        MutableStateFlow<OperationHandler<List<MarvelCharacter>>>(OperationHandler.Waiting)

    val characters get() = _characters.asStateFlow()

    private val _currentCharacter =
        MutableStateFlow<OperationHandler<MarvelCharacter>>(OperationHandler.Waiting)

    val currentCharacter get() = _currentCharacter.asStateFlow()

    init {
        getCharacters()
        getCurrentCharacter()
    }

    private fun getCharacters() {
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

    fun setCurrentCharacter(character: MarvelCharacter, context: Context) {
        _currentCharacter.value = OperationHandler.Success(character)
        viewModelScope.launch {
            tokenManager.saveCharacter(character)
            val glanceId = GlanceAppWidgetManager(context)
                .getGlanceIds(GlanceWidget::class.java)
                .firstOrNull()
            if (glanceId != null)
                refreshWidget(context, tokenManager, glanceId)
        }
    }

    private fun getCurrentCharacter() {
        _currentCharacter.value = OperationHandler.Loading
        viewModelScope.launch {
            try {
                tokenManager.collectCharacter().collect {
                    if (it != null)
                        _currentCharacter.value = OperationHandler.Success(it)
                    else
                        _currentCharacter.value = OperationHandler.Error("No character found")
                }
            } catch (e: Exception) {
                _currentCharacter.value = OperationHandler.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun setCharactersByName(name: String?) {
        _characters.value = OperationHandler.Loading
        viewModelScope.launch {
            val characters = marvelRepository.getCharacters(name)
            _characters.value = OperationHandler.Success(characters)
        }
    }

}