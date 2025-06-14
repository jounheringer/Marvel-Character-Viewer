package com.reringuy.marvelcharacterviewer.models

data class MarvelComicsWrapper(
    val code: Int,
    val status: String,
    val data: MarvelComicsData
)
