package com.reringuy.marvelcharacterviewer.models

data class MarvelResponse(
    val code: Int,
    val status: String,
    val data: MarvelData
)

data class MarvelData(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<MarvelCharacter>
)

data class MarvelCharacter(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: MarvelThumbnail
)

data class MarvelThumbnail(
    val path: String,
    val extension: String
) {
    fun getFullPath(): String = "$path.$extension"
}