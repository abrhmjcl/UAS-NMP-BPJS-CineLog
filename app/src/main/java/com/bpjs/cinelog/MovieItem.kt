package com.bpjs.cinelog

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieItem(
    val id: Int,
    var title: String,
    var genre: String,
    var platform: String,
    var ageRating: String,
    var releaseYear: Int,
    var rating: Double,
    var imageId: Int = 0,
    var url: String = "",
    var trailerUrl: String = "https://www.youtube.com",
    var review: String = "",
    var isAvailable: Boolean = true,
    var hasSpoilers: Boolean = false,
    var isFavorite: Boolean = false
) : Parcelable {
    init {
        title = title.trim()
    }

    override fun toString(): String = title

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
    }
}