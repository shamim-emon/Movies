package bd.emon.domain.entity.details

import bd.emon.domain.IMAGE_BASE_URL

data class MovieDetails(
    val adult: Boolean,
    val backdrop_path: String,
    val belongs_to_collection: BelongsToCollection?,
    val budget: Int,
    val genres: List<Genre>,
    val homepage: String,
    val id: Int,
    val imdb_id: String,
    val original_language: String,
    val original_title: String,
    var overview: String,
    val popularity: Double,
    val poster_path: String,
    val production_companies: List<ProductionCompany>,
    val production_countries: List<ProductionCountry>,
    val release_date: String,
    val revenue: Int,
    val runtime: Int,
    val spoken_languages: List<SpokenLanguage>,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
) {
    val backDropUrl: String
        get() = IMAGE_BASE_URL + backdrop_path

    val imageUrl: String
        get() = IMAGE_BASE_URL + poster_path

    val releaseYearPlusRunTime: String
        get() = "$release_date • $runtime mins"

    val voteAvgString: String
        get() = vote_average.toString()

    val genreLabels: String
        get() = GenreLablesProvider(genres).getGenres()

    private class GenreLablesProvider(val genres: List<Genre>) {
        fun getGenres(): String {
            var genreLabels = ""
            genres.forEachIndexed { index, genre ->
                if (index < genres.size - 1) {
                    genreLabels += "${genre.name} | "
                } else {
                    genreLabels += "${genre.name}"
                }
            }
            return genreLabels
        }
    }
}
