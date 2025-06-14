package com.reringuy.marvelcharacterviewer.models

data class MarvelResponse(
    val code: Int,
    val status: String,
    val data: MarvelData
)