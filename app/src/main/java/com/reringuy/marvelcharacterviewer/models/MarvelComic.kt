package com.reringuy.marvelcharacterviewer.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarvelComic(
    val id: Int,
    val title: String,
    val description: String,
    val issueNumber: Float,
    val thumbnail: MarvelThumbnail,
    val creators: MarvelCreators
): Parcelable
