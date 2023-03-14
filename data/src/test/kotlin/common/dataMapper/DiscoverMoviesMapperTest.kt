package common.dataMapper

import bd.emon.data.dataMapper.DiscoverMovieMapper
import bd.emon.domain.entity.common.MovieEntity
import bd.emon.domain.entity.discover.Result
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DiscoverMoviesMapperTest {

    val mapper = DiscoverMovieMapper()

    val discoverMovieItems = mutableListOf(
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
    )
    val movieEntities = mutableListOf(
        MovieEntity(
            id = 960704,
            poster_path = "/AeyiuQUUs78bPkz18FY3AzNFF8b.jpg",
            title = "Fullmetal Alchemist: The Final Alchemy"
        ),
        MovieEntity(
            id = 791155,
            poster_path = "/8PsHogUfvjWPGdWAI5uslDhHDx7.jpg",
            title = "Secret Headquarters"
        )
    )

    @Test
    fun mapFromSingleItem_success_MovieEntityReturned() {
        val movieEntity = mapper.mapFrom(discoverMovieItems[0])
        assertThat(movieEntity.id, `is`(discoverMovieItems[0].id))
        assertThat(movieEntity.title, `is`(discoverMovieItems[0].title))
        assertThat(movieEntity.poster_path, `is`(discoverMovieItems[0].poster_path))
    }

    @Test
    fun mapFromList_success_MovieEntityListReturned() {
        val movieEntityList = mapper.mapFrom(discoverMovieItems)
        assertThat(movieEntityList == movieEntities, `is`(true))
    }
}
