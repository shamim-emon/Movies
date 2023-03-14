package bd.emon.domain.dataMapper

import bd.emon.data.dataMapper.TrendingMovieMapper
import bd.emon.domain.entity.common.MovieEntity
import bd.emon.domain.entity.trending.Result
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TendingMoviesMapperTest {

    val mapper = TrendingMovieMapper()

    val TrendingMovieItems = mutableListOf(
        Result(
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
        Result(
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
    )
    val movieEntities = mutableListOf(
        MovieEntity(
            id = 765869,
            poster_path = "/vf9Ex4EwSPlwbva4ZJdljsQQOML.jpg",
            title = "Black Friday"
        ),
        MovieEntity(
            id = 675,
            poster_path = "/5aOyriWkPec0zUDxmHFP9qMmBaj.jpg",
            title = "Harry Potter and the Order of the Phoenix"
        )
    )

    @Test
    fun mapFromSingleItem_success_MovieEntityReturned() {
        val movieEntity = mapper.mapFrom(TrendingMovieItems[0])
        assertThat(movieEntity.id, `is`(TrendingMovieItems[0].id))
        assertThat(movieEntity.title, `is`(TrendingMovieItems[0].title))
        assertThat(movieEntity.poster_path, `is`(TrendingMovieItems[0].poster_path))
    }

    @Test
    fun mapFromList_success_MovieEntityListReturned() {
        val movieEntityList = mapper.mapFrom(TrendingMovieItems)
        assertThat(movieEntityList == movieEntities, `is`(true))
    }
}
