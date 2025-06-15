package com.reringuy.marvelcharacterviewer.presentation.viewmodels

import androidx.paging.PagingData
import com.reringuy.marvelcharacterviewer.MainDispatcherRule
import com.reringuy.marvelcharacterviewer.auth.TokenManager
import com.reringuy.marvelcharacterviewer.models.MarvelCharacter
import com.reringuy.marvelcharacterviewer.models.MarvelComic
import com.reringuy.marvelcharacterviewer.models.MarvelCreators
import com.reringuy.marvelcharacterviewer.models.MarvelThumbnail
import com.reringuy.marvelcharacterviewer.repositories.MarvelRepository
import com.reringuy.marvelcharacterviewer.utils.OperationHandler
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
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
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.flow.firstOrNull

class MarvelComicsViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var marvelRepository: MarvelRepository

    @MockK
    private lateinit var tokenManager: TokenManager

    private lateinit var viewmodel: MarvelComicsViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun getComicsList() {
        val fakeCharacter = MarvelCharacter(
            id = 1009368,
            name = "Iron Man",
            description = "Wounded, captured and forced to build a weapon by his enemies, billionaire industrialist Tony Stark instead created an advanced suit of armor to save his life and escape captivity. Now with a new outlook on life, Tony uses his money and intelligence to make the world a safer, better place as Iron Man.",
            thumbnail = MarvelThumbnail(
                "http://i.annihil.us/u/prod/marvel/i/mg/9/c0/527bb7b37ff55",
                "jpg"
            )
        )
        val marvelComics = listOf(
            MarvelComic(
                id = 1,
                title = "Hulk (2008) #55",
                description = "The Red Hulk and Doctor Octopus have formed an unlikely alliance. What could these two powerhouses be up to?",
                issueNumber = 55.0f,
                thumbnail = MarvelThumbnail(
                    path = "http://i.annihil.us/u/prod/marvel/i/mg/6/a0/515f1dd4c8a6f",
                    extension = "jpg"
                ),
                creators = MarvelCreators(emptyList())
            ),
            MarvelComic(
                id = 2,
                title = "Avengers (1998) #71",
                description = "The Wrecking Crew is back and wreaking havoc! Can the Avengers stop them before they demolish the entire city?",
                issueNumber = 71.0f,
                thumbnail = MarvelThumbnail(
                    path = "http://i.annihil.us/u/prod/marvel/i/mg/c/b0/4bc640a43a08d",
                    extension = "jpg"
                ),
                creators = MarvelCreators(emptyList())
            ),
            MarvelComic(
                id = 3,
                title = "Spider-Man (2016) #1",
                description = "A new era for Spider-Man begins! Peter Parker is back, but he's not the only Spider-Man in town.",
                issueNumber = 1.0f,
                thumbnail = MarvelThumbnail(
                    path = "http://i.annihil.us/u/prod/marvel/i/mg/6/80/57700e0506c0d",
                    extension = "jpg"
                ),
                creators = MarvelCreators(emptyList())
            )
        )

        coEvery { marvelRepository.getCharacterComics(any()) } returns flowOf(
            PagingData.from(
                marvelComics
            )
        )

        coEvery { tokenManager.collectCharacter() } returns flowOf(fakeCharacter)

        viewmodel = MarvelComicsViewModel(tokenManager, marvelRepository)

        assert(viewmodel.comicsList != null)
    }

    @Test
    fun getComicsList_whenCharacterIsNull_returnsNull() {
        coEvery { marvelRepository.getCharacterComics(any()) } returns flowOf(
            PagingData.from(
                emptyList<MarvelComic>()
            )
        )
        coEvery { tokenManager.collectCharacter() } returns flowOf(null)

        viewmodel = MarvelComicsViewModel(tokenManager, marvelRepository)

        assertNull(viewmodel.comicsList)
    }
    @Test
    fun setCurrentComic() = runTest {
        val fakeComic = MarvelComic(
            id = 1,
            title = "Hulk (2008) #55",
            description = "The Red Hulk and Doctor Octopus have formed an unlikely alliance. What could these two powerhouses be up to?",
            issueNumber = 55.0f,
            thumbnail = MarvelThumbnail(
                path = "http://i.annihil.us/u/prod/marvel/i/mg/6/a0/515f1dd4c8a6f",
                extension = "jpg"
            ),
            creators = MarvelCreators(emptyList())
        )

        coEvery { tokenManager.saveComic(fakeComic) } just Runs
        coEvery { tokenManager.collectCharacter() } returns flowOf(null)

        viewmodel = MarvelComicsViewModel(tokenManager, marvelRepository)

        viewmodel.setCurrentComic(fakeComic)

        assert(viewmodel.effect.first() == fakeComic)
    }

    @Test
    fun setCurrentComic_whenSaveComicFails_effectIsNotEmitted() = runTest {
        val fakeComic = MarvelComic(
            id = 1,
            title = "Hulk (2008) #55",
            description = "The Red Hulk and Doctor Octopus have formed an unlikely alliance.",
            issueNumber = 55.0f,
            thumbnail = MarvelThumbnail(
                path = "http://i.annihil.us/u/prod/marvel/i/mg/6/a0/515f1dd4c8a6f",
                extension = "jpg"
            ),
            creators = MarvelCreators(emptyList())
        )

        coEvery { tokenManager.saveComic(fakeComic) } throws Exception("Save comic failed")
        coEvery { tokenManager.collectCharacter() } returns flowOf(null)

        viewmodel = MarvelComicsViewModel(tokenManager, marvelRepository)

        viewmodel.setCurrentComic(fakeComic)

        assertNull(viewmodel.effect.firstOrNull { true })
        coVerify { tokenManager.saveComic(fakeComic) }
    }

    @Test
    fun getCurrentCharacter() = runTest {
        val fakeCharacter = MarvelCharacter(
            id = 1009368,
            name = "Iron Man",
            description = "Wounded, captured and forced to build a weapon by his enemies, billionaire industrialist Tony Stark instead created an advanced suit of armor to save his life and escape captivity. Now with a new outlook on life, Tony uses his money and intelligence to make the world a safer, better place as Iron Man.",
            thumbnail = MarvelThumbnail(
                "http://i.annihil.us/u/prod/marvel/i/mg/9/c0/527bb7b37ff55",
                "jpg"
            )
        )

        coEvery { tokenManager.collectCharacter() } returns flowOf(fakeCharacter)
        coEvery { marvelRepository.getCharacterComics(any()) } returns flowOf(
            PagingData.from(emptyList())
        )

        viewmodel = MarvelComicsViewModel(tokenManager, marvelRepository)
        val result = viewmodel.currentCharacter.first {
            it !in listOf(
                OperationHandler.Loading,
                OperationHandler.Waiting
            )
        }
        assertEquals(OperationHandler.Success(fakeCharacter), result)
        coVerify { tokenManager.collectCharacter() }
    }

    @Test
    fun getCurrentCharacter_whenCollectCharacterIsNull_returnsWaiting() = runTest {
        coEvery { tokenManager.collectCharacter() } returns flowOf(null)

        viewmodel = MarvelComicsViewModel(tokenManager, marvelRepository)
        val result = viewmodel.currentCharacter.first {
            it !in listOf(
                OperationHandler.Loading,
                OperationHandler.Waiting
            )
        }
        assertEquals(OperationHandler.Error("No character found"), result)
        coVerify { tokenManager.collectCharacter() }
        coVerify(exactly = 0) { marvelRepository.getCharacterComics(any()) }
    }

}