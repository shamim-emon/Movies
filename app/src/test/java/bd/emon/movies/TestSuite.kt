package bd.emon.movies

import bd.emon.movies.common.dataMapper.DiscoverMovieMapperTest
import bd.emon.movies.rest.MovieRestRepositoryTest
import bd.emon.movies.viewModels.HomeViewModelTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    HomeViewModelTest::class,
    MovieRestRepositoryTest::class,
    DiscoverMovieMapperTest::class
)
class TestSuite
