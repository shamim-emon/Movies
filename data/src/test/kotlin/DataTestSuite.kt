import bd.emon.domain.dataMapper.SearchMovieMapperTest
import bd.emon.domain.dataMapper.TendingMoviesMapperTest
import common.dataMapper.DiscoverMoviesMapperTest
import org.junit.runner.RunWith
import org.junit.runners.Suite
import rest.MovieRestRepositoryTest

@RunWith(Suite::class)
@Suite.SuiteClasses(
    MovieRestRepositoryTest::class,
    DiscoverMoviesMapperTest::class,
    TendingMoviesMapperTest::class,
    SearchMovieMapperTest::class
)
class DataTestSuite
