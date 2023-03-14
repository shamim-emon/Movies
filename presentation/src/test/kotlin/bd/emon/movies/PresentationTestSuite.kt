package bd.emon.movies

import bd.emon.movies.viewModels.DetailsViewModelTest
import bd.emon.movies.viewModels.FavouriteViewModelTest
import bd.emon.movies.viewModels.HomeViewModelTest
import bd.emon.movies.viewModels.SearchViewModelTest
import bd.emon.movies.viewModels.TrendingViewModelTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    HomeViewModelTest::class,
    TrendingViewModelTest::class,
    SearchViewModelTest::class,
    DetailsViewModelTest::class,
    FavouriteViewModelTest::class
)
class PresentationTestSuite
