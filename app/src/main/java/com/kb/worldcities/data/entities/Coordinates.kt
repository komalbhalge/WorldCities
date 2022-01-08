package com.kb.worldcities.data.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Coordinates(
    val lon: Double,
    val lat: Double
    ) : Parcelable