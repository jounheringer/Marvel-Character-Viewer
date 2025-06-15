package com.reringuy.marvelcharacterviewer.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarvelCreator(
    val name: String,
    val role: String
): Parcelable