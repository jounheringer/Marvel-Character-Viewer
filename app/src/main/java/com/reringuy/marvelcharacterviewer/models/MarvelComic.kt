package com.reringuy.marvelcharacterviewer.models

data class MarvelComic(
    val id: Int,
    val title: String,
    val description: String,
    val issueNumber: Int,
    val thumbnail: MarvelThumbnail
)
