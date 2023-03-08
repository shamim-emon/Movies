package bd.emon.domain.dataMapper

import bd.emon.data.dataMapper.SearchMovieMapper
import bd.emon.domain.entity.common.MovieEntity
import bd.emon.domain.entity.search.Result
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchMovieMapperTest {

    val mapper = SearchMovieMapper()
    val searchMovieItems = mutableListOf(
        Result(
            adult = false,
            backdrop_path = "/jsoz1HlxczSuTx0mDl2h0lxy36l.jpg",
            genre_ids = listOf(14, 28, 35),
            id = 616037,
            title = "Thor: Love and Thunder",
            original_language = "en",
            original_title = "Thor: Love and Thunder",
            overview = "After his retirement is interrupted by Gorr the God Butcher, a galactic killer who seeks the extinction of the gods, Thor Odinson enlists the help of King Valkyrie, Korg, and ex-girlfriend Jane Foster, who now wields Mjolnir as the Mighty Thor. Together they embark upon a harrowing cosmic adventure to uncover the mystery of the God Butcher’s vengeance and stop him before it’s too late.",
            popularity = 1404.107,
            video = false,
            vote_average = 6.7,
            vote_count = 4625,
            poster_path = "/pIkRyD18kl4FhoCNQuWxWu5cBLM.jpg",
            release_date = "2022-07-06"
        ),
        Result(
            adult = false,
            backdrop_path = "/lD8dFIk9wDEvOwZw0RB47e346io.jpg",
            genre_ids = listOf(10749, 18, 36),
            id = 698508,
            title = "Redeeming Love",
            original_language = "en",
            original_title = "Redeeming Love",
            overview = "A retelling of the biblical book of Hosea set against the backdrop of the California Gold Rush of 1850.",
            popularity = 133.84,
            video = false,
            vote_average = 7.9,
            vote_count = 168,
            poster_path = "/pDc2HxQtC0MlKD4QfRvmKREEyhc.jpg",
            release_date = "2022-01-21"
        )
    )

    val movieEntities = mutableListOf(
        MovieEntity(
            id = 616037,
            poster_path = "/pIkRyD18kl4FhoCNQuWxWu5cBLM.jpg",
            title = "Thor: Love and Thunder"
        ),
        MovieEntity(
            id = 698508,
            poster_path = "/pDc2HxQtC0MlKD4QfRvmKREEyhc.jpg",
            title = "Redeeming Love"
        )
    )

    @Test
    fun mapFromSingleItem_success_MovieEntityReturned() {
        val movieEntity = mapper.mapFrom(searchMovieItems[0])
        MatcherAssert.assertThat(movieEntity.id, CoreMatchers.`is`(searchMovieItems[0].id))
        MatcherAssert.assertThat(movieEntity.title, CoreMatchers.`is`(searchMovieItems[0].title))
        MatcherAssert.assertThat(
            movieEntity.poster_path,
            CoreMatchers.`is`(searchMovieItems[0].poster_path)
        )
    }

    @Test
    fun mapFromList_success_MovieEntityListReturned() {
        val movieEntityList = mapper.mapFrom(searchMovieItems)
        MatcherAssert.assertThat(movieEntityList == movieEntities, CoreMatchers.`is`(true))
    }
}
