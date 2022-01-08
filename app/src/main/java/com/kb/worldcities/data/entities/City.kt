package com.kb.worldcities.data.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(
    var _id: Int,
    var name: String,
    var country: String,
    var coord: Coordinates) : Parcelable {
    val displayName: String
        get() = "$name, $country"
    val mapKey: String
        get() = "$name, $country, $_id"
}