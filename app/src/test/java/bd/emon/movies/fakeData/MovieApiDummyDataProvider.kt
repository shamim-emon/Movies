package bd.emon.movies.fakeData

import bd.emon.movies.entity.discover.DiscoverMovie
import bd.emon.movies.entity.discover.Result
import bd.emon.movies.entity.genre.Genre
import bd.emon.movies.entity.genre.Genres

object MovieApiDummyDataProvider {
    val genreList = Genres(
        genres = listOf(
            Genre(
                id = 28,
                name = "Action"
            ),
            Genre(
                id = 12,
                name = "Adventure"
            )
        )
    )

    val disocoverMovies = DiscoverMovie(
        page = 1,
        results = listOf(
            Result(
                adult = false,
                backdrop_path = "/5hoS3nEkGGXUfmnu39yw1k52JX5.jpg",
                genre_ids = listOf(28, 12, 14),
                id = 960704,
                original_language = "ja",
                original_title = "鋼の錬金術師 完結編 最後の錬成",
                overview = "The Elric brothers’ long and winding journey comes to a close in this epic finale, where they must face off against an unworldly, nationwide threat.",
                popularity = 3949.104,
                poster_path = "/AeyiuQUUs78bPkz18FY3AzNFF8b.jpg",
                release_date = "2022-06-24",
                title = "Fullmetal Alchemist: The Final Alchemy",
                video = false,
                vote_average = 6.1,
                vote_count = 58
            ),
            Result(
                adult = false,
                backdrop_path = "/aIkG2V4UXrfkxMdJZmq30xO0QQr.jpg",
                genre_ids = listOf(878, 12, 28),
                id = 791155,
                original_language = "en",
                original_title = "Secret Headquarters",
                overview = "While hanging out after school, Charlie and his friends discover the headquarters of the world’s most powerful superhero hidden beneath his home. When villains attack, they must team up to defend the headquarters and save the world.",
                popularity = 2866.779,
                poster_path = "/8PsHogUfvjWPGdWAI5uslDhHDx7.jpg",
                release_date = "2022-08-12",
                title = "Secret Headquarters",
                video = false,
                vote_average = 6.9,
                vote_count = 83
            )
        ),
        total_pages = 10,
        total_results = 200,
        grp_genre_id = 28
    )
}
