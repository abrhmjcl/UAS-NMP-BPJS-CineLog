package com.bpjs.cinelog

object MovieData {
    var movies: ArrayList<MovieItem> = arrayListOf(
        MovieItem(
            id = 1,
            title = "Interstellar",
            genre = "Sci-Fi",
            platform = "Cinema XXI",
            ageRating = "13+",
            releaseYear = 2014,
            rating = 8.7,
            url = "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
            trailerUrl = "https://www.youtube.com/watch?v=zSWdZVtXT7E",
            review = "Absolute Cinema Christoper Nolan",
            isAvailable = true,
            hasSpoilers = false,
            isFavorite = true
        ),
        MovieItem(
            id = 2,
            title = "Oppenheimer",
            genre = "Drama",
            platform = "Cinema XXI",
            ageRating = "17+",
            releaseYear = 2023,
            rating = 8.9,
            url = "https://image.tmdb.org/t/p/w500/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg",
            trailerUrl = "https://www.youtube.com/watch?v=uYPbbksJxIg",
            review = "Awal mula bom atom",
            isAvailable = true,
            hasSpoilers = true,
            isFavorite = true
        ),
        MovieItem(
            id = 3,
            title = "Dune: Part Two",
            genre = "Sci-Fi",
            platform = "Cinema XXI",
            ageRating = "13+",
            releaseYear = 2024,
            rating = 8.6,
            url = "https://image.tmdb.org/t/p/w500/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg",
            trailerUrl = "https://www.youtube.com/watch?v=Way9Dexny3w",
            review = "Sekuel terbaru dari nue part 1",
            isAvailable = false,
            hasSpoilers = false,
            isFavorite = false
        ),
        MovieItem(
            id = 4,
            title = "Spirited Away",
            genre = "Anime",
            platform = "Netflix",
            ageRating = "SU",
            releaseYear = 2001,
            rating = 8.6,
            url = "https://image.tmdb.org/t/p/w500/39wmItIWsg5sZMyRUHLkWBcuVCM.jpg",
            trailerUrl = "https://www.youtube.com/watch?v=ByXuk9QqQkk",
            review = "Animasi terbaru dari Studio Gibli",
            isAvailable = true,
            hasSpoilers = false,
            isFavorite = true
        )
    )

    val genres = arrayOf("Action", "Sci-Fi", "Drama", "Horror", "Comedy", "Anime")
    val platforms = arrayOf("Cinema XXI", "Netflix", "Disney+ Hotstar", "Prime Video")
    val ageRatings = arrayOf("SU", "13+", "17+")
}