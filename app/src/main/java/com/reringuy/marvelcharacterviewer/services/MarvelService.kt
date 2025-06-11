package com.reringuy.marvelcharacterviewer.services

import com.reringuy.marvelcharacterviewer.models.MarvelResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelService {
    @GET("characters")
    suspend fun getCharacters(
        @Query("ts") timestamp: String,
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): MarvelResponse
}