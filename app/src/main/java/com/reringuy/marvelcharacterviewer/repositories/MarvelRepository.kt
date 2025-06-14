package com.reringuy.marvelcharacterviewer.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.reringuy.marvelcharacterviewer.models.MarvelCharacter
import com.reringuy.marvelcharacterviewer.presentation.paging.ComicsPagingSource
import com.reringuy.marvelcharacterviewer.services.MarvelService
import com.reringuy.marvelcharacterviewer.utils.generateMarvelHash
import javax.inject.Inject

class MarvelRepository @Inject constructor(
    private val marvelService: MarvelService,
    private val publicKey: String,
    private val privateKey: String
) {
    suspend fun getCharacters(): List<MarvelCharacter>{
        val timeStamp = System.currentTimeMillis().toString()
        val hash = generateMarvelHash(timeStamp, privateKey, publicKey)
        return marvelService.getCharacters(
            timestamp = timeStamp,
            apiKey = publicKey,
            hash = hash,
            limit = 100,
            offset = 0
        ).data.results
    }

    fun getCharacterComics(characterId: Int) = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { ComicsPagingSource(marvelService, characterId) }
    ).flow
}