package com.reringuy.marvelcharacterviewer.models

data class MarvelThumbnail(
    val path: String,
    val extension: String
) {
    fun getFullPath(): String = "$path.$extension"
}