package com.reringuy.marvelcharacterviewer.models

data class MarvelCharacter(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: MarvelThumbnail
)