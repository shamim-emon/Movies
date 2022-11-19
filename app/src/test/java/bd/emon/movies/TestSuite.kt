package bd.emon.movies

import bd.emon.movies.common.dataMapper.DiscoverMoviesMapperTest
import bd.emon.movies.common.dataMapper.SearchMovieMapperTest
import bd.emon.movies.common.dataMapper.TendingMoviesMapperTest
import bd.emon.movies.rest.MovieRestRepositoryTest
import bd.emon.movies.viewModels.HomeViewModelTest
import bd.emon.movies.viewModels.SearchViewModelTest
import bd.emon.movies.viewModels.TrendingViewModelTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    HomeViewModelTest::class,
    MovieRestRepositoryTest::class,
    DiscoverMoviesMapperTest::class,
    TrendingViewModelTest::class,
    TendingMoviesMapperTest::class,
    SearchMovieMapperTest::class,
    SearchViewModelTest::class
)
class TestSuite
