package com.reringuy.marvelcharacterviewer.presentation.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.reringuy.marvelcharacterviewer.BuildConfig
import com.reringuy.marvelcharacterviewer.models.MarvelComic
import com.reringuy.marvelcharacterviewer.services.MarvelService
import com.reringuy.marvelcharacterviewer.utils.generateMarvelHash

class ComicsPagingSource(
    private val marvelService: MarvelService,
    private val characterId: Int
) : PagingSource<Int, MarvelComic>() {
    override fun getRefreshKey(state: PagingState<Int, MarvelComic>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MarvelComic> {
        val page = params.key ?: 0
        val limit = 100
        val offset = page * limit
        val timeStamp = System.currentTimeMillis().toString()
        val privateKey = BuildConfig.MARVEL_PRIVATE_KEY
        val publicKey = BuildConfig.MARVEL_PUBLIC_KEY
        val hash = generateMarvelHash(timeStamp, privateKey, publicKey)

        return try {
            val response = marvelService.getCharacterComics(
                characterId = characterId,
                timestamp = timeStamp,
                apiKey = publicKey,
                hash = hash,
                limit = limit,
                offset = offset
            )
            val result = response.data.results
            Log.d("ComicsPagingSource.responseSize", result.size.toString())
            LoadResult.Page(
                data = result,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (result.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            Log.e("ComicsPagingSource", "Error loading comics: ${e.message}")
            LoadResult.Error(e)
        }
    }
}