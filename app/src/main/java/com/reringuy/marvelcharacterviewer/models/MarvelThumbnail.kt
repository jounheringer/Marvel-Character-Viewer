package com.reringuy.marvelcharacterviewer.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarvelThumbnail(
    val path: String,
    val extension: String,
) : Parcelable {
    fun getFullPath(): String = "$path.$extension"
}