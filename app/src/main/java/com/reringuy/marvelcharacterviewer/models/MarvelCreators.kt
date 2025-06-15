package com.reringuy.marvelcharacterviewer.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarvelCreators(
    val items: List<MarvelCreator>
): Parcelable
