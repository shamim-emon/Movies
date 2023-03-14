package bd.emon.movies.fakeData

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.mutablePreferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import bd.emon.domain.PARAM_INCLUDE_ADULT
import bd.emon.domain.PARAM_RELEASE_YEAR
import bd.emon.domain.PARAM_SORT_BY
import bd.emon.domain.PARAM_VOTE_COUNT_GREATER_THAN
import bd.emon.domain.entity.details.BelongsToCollection
import bd.emon.domain.entity.details.Genre
import bd.emon.domain.entity.details.MovieDetails
import bd.emon.domain.entity.details.MovieVideos
import bd.emon.domain.entity.details.ProductionCompany
import bd.emon.domain.entity.details.ProductionCountry
import bd.emon.domain.entity.details.Result
import bd.emon.domain.entity.details.SpokenLanguage
import bd.emon.domain.entity.discover.DiscoverMovies
import bd.emon.domain.entity.genre.Genres
import bd.emon.domain.entity.search.MovieSearch
import bd.emon.domain.entity.trending.TrendingMovies

object MovieApiDummyDataProvider {
    val genreList = Genres(
        genres = listOf(
            bd.emon.domain.entity.genre.Genre(
                id = 28,
                name = "Action"
            ),
            bd.emon.domain.entity.genre.Genre(
                id = 12,
                name = "Adventure"
            )
        )
    )

    val disocoverMovies = DiscoverMovies(
        page = 1,
        results = mutableListOf(
            bd.emon.domain.entity.discover.Result(
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
            bd.emon.domain.entity.discover.Result(
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

    val discoverFilters: MutablePreferences
        get() {
            val mp = mutablePreferencesOf()

            val PARAM_VOTE_COUNT_GREATER_THAN = intPreferencesKey(PARAM_VOTE_COUNT_GREATER_THAN)
            val PARAM_INCLUDE_ADULT = booleanPreferencesKey(PARAM_INCLUDE_ADULT)
            val PARAM_SORT_BY = stringPreferencesKey(PARAM_SORT_BY)
            val PARAM_RELEASE_YEAR = stringPreferencesKey(PARAM_RELEASE_YEAR)

            mp[PARAM_VOTE_COUNT_GREATER_THAN] = 10000
            mp[PARAM_INCLUDE_ADULT] = true
            mp[PARAM_SORT_BY] = "sort_by"
            mp[PARAM_RELEASE_YEAR] = "releaseYr"
            return mp
        }

    /**
     * Default filter when no param value is cached
     */
    val discoverDefaultFilters: MutablePreferences
        get() = mutablePreferencesOf()

    val trendingMovies = TrendingMovies(
        results = mutableListOf(
            bd.emon.domain.entity.trending.Result(
                adult = false,
                backdrop_path = "/pinPGZW5r9SOFNlSLugpdLwvdyD.jpg",
                id = 765869,
                title = "Black Friday",
                original_language = "en",
                original_title = "Black Friday",
                overview = "A group of toy store employees must protect each other from a horde of parasite infected shoppers.",
                poster_path = "/vf9Ex4EwSPlwbva4ZJdljsQQOML.jpg",
                media_type = "movie",
                genre_ids = listOf(27, 35, 878),
                popularity = 22.169,
                release_date = "2021-11-19",
                video = false,
                vote_average = 4.905,
                vote_count = 137
            ),
            bd.emon.domain.entity.trending.Result(
                adult = false,
                backdrop_path = "/sO2VWeJ8qagNtYoznyLRL8TeSkw.jpg",
                id = 675,
                title = "Harry Potter and the Order of the Phoenix",
                original_language = "en",
                original_title = "Harry Potter and the Order of the Phoenix",
                overview = "Returning for his fifth year of study at Hogwarts, Harry is stunned to find that his warnings about the return of Lord Voldemort have been ignored. Left with no choice, Harry takes matters into his own hands, training a small group of students to defend themselves against the dark arts.",
                poster_path = "/5aOyriWkPec0zUDxmHFP9qMmBaj.jpg",
                media_type = "movie",
                genre_ids = listOf(12, 14, 9648),
                popularity = 243.553,
                release_date = "2021-11-19",
                video = false,
                vote_average = 7.691,
                vote_count = 16929
            )

        ),
        page = 1,
        total_pages = 1,
        total_results = 2
    )

    val searchResults = MovieSearch(
        page = 1,
        total_pages = 1,
        total_results = 2,
        results = mutableListOf(
            bd.emon.domain.entity.search.Result(
                adult = false,
                backdrop_path = "/jsoz1HlxczSuTx0mDl2h0lxy36l.jpg",
                genre_ids = listOf(14, 28, 35),
                id = 616037,
                original_language = "en",
                original_title = "Thor: Love and Thunder",
                overview = "After his retirement is interrupted by Gorr the God Butcher, a galactic killer who seeks the extinction of the gods, Thor Odinson enlists the help of King Valkyrie, Korg, and ex-girlfriend Jane Foster, who now wields Mjolnir as the Mighty Thor. Together they embark upon a harrowing cosmic adventure to uncover the mystery of the God Butcher’s vengeance and stop him before it’s too late.",
                popularity = 819.638,
                poster_path = "/pIkRyD18kl4FhoCNQuWxWu5cBLM.jpg",
                release_date = "2022-07-06",
                title = "Thor: Love and Thunder",
                video = false,
                vote_average = 6.7,
                vote_count = 4677
            ),
            bd.emon.domain.entity.search.Result(
                adult = false,
                backdrop_path = "/lD8dFIk9wDEvOwZw0RB47e346io.jpg",
                genre_ids = listOf(18, 36),
                id = 698508,
                original_language = "en",
                original_title = "Redeeming Love",
                overview = "A retelling of the biblical book of Hosea set against the backdrop of the California Gold Rush of 1850.",
                popularity = 156.74,
                poster_path = "/pDc2HxQtC0MlKD4QfRvmKREEyhc.jpg",
                release_date = "2022-01-21",
                title = "Redeeming Love",
                video = false,
                vote_average = 7.9,
                vote_count = 173
            )
        )
    )

    val movieDetails = MovieDetails(
        adult = false,
        backdrop_path = "/3Au8rkDVsRK2AT4jq9dg6jH6buy.jpg",
        belongs_to_collection = BelongsToCollection(
            id = 295,
            name = "Pirates of the Caribbean Collection",
            poster_path = "/p9fXIvNppK21fCHAEkznSZb8hnv.jpg",
            backdrop_path = "/wxgD3fB5lQ2sGJLog0rvXW049Pf.jpg"
        ),
        budget = 300000000,
        genres = listOf(
            Genre(
                id = 12,
                name = "Adventure"
            ),
            Genre(
                id = 14,
                name = "Fantasy"
            ),
            Genre(
                id = 28,
                name = "Action"
            )
        ),
        homepage = "https://movies.disney.com/pirates-of-the-caribbean-at-worlds-end",
        id = 285,
        imdb_id = "tt0449088",
        original_language = "en",
        original_title = "Pirates of the Caribbean: At World's End",
        overview = "Captain Barbossa, long believed to be dead, has come back to life and is headed to the edge of the Earth with Will Turner and Elizabeth Swann. But nothing is quite as it seems.",
        popularity = 111.664,
        poster_path = "/2YMnBRh8F6fDGCCEIPk9Hb0cEyB.jpg",
        production_companies = listOf(
            ProductionCompany(
                id = 2,
                logo_path = "/wdrCwmRnLFJhEoH8GSfymY85KHT.png",
                name = "Walt Disney Pictures",
                origin_country = "US"
            ),
            ProductionCompany(
                id = 130,
                logo_path = "/c9dVHPOL3cqCr2593Ahk0nEKTEM.png",
                name = "Jerry Bruckheimer Films",
                origin_country = "US"
            ),
            ProductionCompany(
                id = 19936,
                logo_path = null,
                name = "Second Mate Productions",
                origin_country = "US"
            )
        ),
        production_countries = listOf(
            ProductionCountry(
                iso_3166_1 = "US",
                name = "United States of America"
            )
        ),
        release_date = "2007-05-19",
        revenue = 961000000,
        runtime = 168,
        spoken_languages = listOf(
            SpokenLanguage(
                english_name = "English",
                iso_639_1 = "en",
                name = "English"
            )
        ),
        status = "Released",
        tagline = "At the end of the world, the adventure begins.",
        title = "Pirates of the Caribbean: At World's End",
        video = false,
        vote_average = 7.233,
        vote_count = 12666
    )

    val movieVideos = MovieVideos(
        id = 616037,
        results = listOf(
            Result(
                id = "634e619e1089ba007d54910f",
                iso_639_1 = "en",
                iso_3166_1 = "US",
                key = "Gf4AXNthfVg",
                name = "Q&A Featurette",
                official = true,
                published_at = "2022-10-15T19:00:11.000Z",
                site = "YouTube",
                size = 1080,
                type = "Featurette"
            )
        )
    )
}
