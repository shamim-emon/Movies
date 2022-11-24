package bd.emon.movies

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bd.emon.movies.common.navigation.ScreensNavigator
import bd.emon.movies.databinding.ActivityMainBinding
import bd.emon.movies.home.HomeFragment
import bd.emon.movies.movieEntity.APICallType
import bd.emon.movies.movieEntity.MovieEntityListFragment
import bd.emon.movies.search.SearchFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var screensNavigator: ScreensNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViewListeners()
        screensNavigator.goTo(HomeFragment.newInstance())
    }

    override fun onBackPressed() {
        screensNavigator.back()
    }

    private fun setUpViewListeners() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home_tab -> {
                    screensNavigator.goTo(HomeFragment.newInstance())
                    true
                }
                R.id.trending_tab -> {
                    screensNavigator.goTo(
                        MovieEntityListFragment.newInstance(
                            getString(R.string.trending_today),
                            APICallType.TRENDING_MOVIES
                        )
                    )
                    true
                }
                R.id.search_tab -> {
                    screensNavigator.goTo(
                        SearchFragment.newInstance()
                    )
                    true
                }
                R.id.favourite_tab -> {
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
}
