package com.reringuy.marvelcharacterviewer.models

data class MarvelComicsData(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<MarvelComic>
)
