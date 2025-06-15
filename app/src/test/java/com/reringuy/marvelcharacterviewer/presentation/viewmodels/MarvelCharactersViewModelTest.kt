package com.reringuy.marvelcharacterviewer.presentation.viewmodels

import com.reringuy.marvelcharacterviewer.MainDispatcherRule
import com.reringuy.marvelcharacterviewer.auth.TokenManager
import com.reringuy.marvelcharacterviewer.models.MarvelCharacter
import com.reringuy.marvelcharacterviewer.models.MarvelThumbnail
import com.reringuy.marvelcharacterviewer.repositories.MarvelRepository
import com.reringuy.marvelcharacterviewer.utils.OperationHandler
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MarvelCharactersViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var marvelRepository: MarvelRepository

    @MockK
    private lateinit var tokenManager: TokenManager

    private lateinit var viewmodel: MarvelCharactersViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewmodel = MarvelCharactersViewModel(marvelRepository, tokenManager)
    }

    @Test
    fun `getCurrentCharacter should return character from token manager`() = runTest {
        val fakeCharacter = MarvelCharacter(
            id = 1009610,
            name = "Spider-Man (Peter Parker)",
            description = "Bitten by a radioactive spider, high school student Peter Parker gained the speed, strength and powers of a spider. Adopting the name Spider-Man, Peter hoped to start a career using his new abilities. Taught that with great power comes great responsibility, Spidey has vowed to use his powers to help people.",
            thumbnail = MarvelThumbnail(
                "http://i.annihil.us/u/prod/marvel/i/mg/3/50/526548a343e4b",
                "jpg"
            )
        )

        every { tokenManager.collectCharacter() } returns flowOf(fakeCharacter)
        viewmodel = MarvelCharactersViewModel(marvelRepository, tokenManager)

        val result = viewmodel.currentCharacter.first { it !is OperationHandler.Loading }
        assertEquals(OperationHandler.Success(fakeCharacter), result)
        coVerify { tokenManager.collectCharacter() }
    }

    @Test
    fun `getCurrentCharacter should return null from token manager`() = runTest {
        val fakeCharacter = null

        every { tokenManager.collectCharacter() } returns flowOf(fakeCharacter)
        viewmodel = MarvelCharactersViewModel(marvelRepository, tokenManager)

        val result = viewmodel.currentCharacter.first { it !is OperationHandler.Loading }
        assertEquals(OperationHandler.Error("No character found"), result)
        coVerify { tokenManager.collectCharacter() }
    }

    @Test
    fun setCurrentCharacter() = runTest {
        val fakeCharacter = MarvelCharacter(
            id = 1009368,
            name = "Iron Man",
            description = "Wounded, captured and forced to build a weapon by his enemies, billionaire industrialist Tony Stark instead created an advanced suit of armor to save his life and escape captivity. Now with a new outlook on life, Tony uses his money and intelligence to make the world a safer, better place as Iron Man.",
            thumbnail = MarvelThumbnail(
                "http://i.annihil.us/u/prod/marvel/i/mg/9/c0/527bb7b37ff55",
                "jpg"
            )
        )

        coEvery { tokenManager.saveCharacter(fakeCharacter) } just Runs

        viewmodel.setCurrentCharacter(fakeCharacter)

        assertEquals(OperationHandler.Success(fakeCharacter), viewmodel.currentCharacter.value)
        coVerify { tokenManager.saveCharacter(fakeCharacter) }
    }

    @Test
    fun `getCharacters should return success on getCharacter api`() = runTest {
        val fakeCharacters = listOf(
            MarvelCharacter(
                id = 1009610,
                name = "Spider-Man (Peter Parker)",
                description = "Bitten by a radioactive spider, high school student Peter Parker gained the speed, strength and powers of a spider. Adopting the name Spider-Man, Peter hoped to start a career using his new abilities. Taught that with great power comes great responsibility, Spidey has vowed to use his powers to help people.",
                thumbnail = MarvelThumbnail(
                    "http://i.annihil.us/u/prod/marvel/i/mg/3/50/526548a343e4b",
                    "jpg"
                )
            ),
            MarvelCharacter(
                id = 1009368,
                name = "Iron Man",
                description = "Wounded, captured and forced to build a weapon by his enemies, billionaire industrialist Tony Stark instead created an advanced suit of armor to save his life and escape captivity. Now with a new outlook on life, Tony uses his money and intelligence to make the world a safer, better place as Iron Man.",
                thumbnail = MarvelThumbnail(
                    "http://i.annihil.us/u/prod/marvel/i/mg/9/c0/527bb7b37ff55",
                    "jpg"
                )
            ),
            MarvelCharacter(
                id = 1009220,
                name = "Captain America",
                description = "Vowing to serve his country any way he could, young Steve Rogers took the super soldier serum to become America's one-man army. Fighting for the red, white and blue for over 60 years, Captain America is the living, breathing symbol of freedom and liberty.",
                thumbnail = MarvelThumbnail(
                    "http://i.annihil.us/u/prod/marvel/i/mg/3/50/537ba56d31087",
                    "jpg"
                )
            )
        )

        coEvery { marvelRepository.getCharacters() } returns fakeCharacters
        viewmodel = MarvelCharactersViewModel(marvelRepository, tokenManager)

        val result = viewmodel.characters.first {
            it !in listOf(
                OperationHandler.Loading,
                OperationHandler.Waiting
            )
        }
        assertEquals(OperationHandler.Success(fakeCharacters), result)
    }

    @Test
    fun `getCharacters should get an error on characters varible`() = runTest {
        coEvery { marvelRepository.getCharacters() } throws RuntimeException("Error collecting heroes")
        viewmodel = MarvelCharactersViewModel(marvelRepository, tokenManager)

        val result = viewmodel.characters.first {
            it !in listOf(
                OperationHandler.Loading,
                OperationHandler.Waiting
            )
        }
        assertEquals(OperationHandler.Error("Error collecting heroes"), result)
    }

}