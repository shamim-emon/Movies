package bd.emon.movies

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import bd.emon.movies.common.navigation.ScreensNavigator
import bd.emon.movies.databinding.ActivityMainBinding
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
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_container
        ) as NavHostFragment
        binding.bottomNavigation.setupWithNavController(navHostFragment.navController)
        // setUpViewListeners()
        // screensNavigator.goTo(HomeFragment.newInstance())
    }

//    override fun onSupportNavigateUp() :Boolean{
//        return binding.navHostFragment.findNavController().navigateUp() || super.onSupportNavigateUp()
//    }

//    private fun setUpViewListeners() {
//        binding.bottomNavigation.setOnItemSelectedListener {
//            when (it.itemId) {
//                R.id.home_tab -> {
//                    screensNavigator.goTo(HomeFragment.newInstance())
//                    true
//                }
//                R.id.trending_tab -> {
//                    if (binding.bottomNavigation.selectedItemId != R.id.trending_tab) {
//                        val action =
//                            HomeFragmentDirections.actionHomeFragmentToMovieEntityListFragment(
//                                DEFAULT_GENRE_ID,
//                                getString(R.string.trending_today),
//                                APICallType.TRENDING_MOVIES
//                            )
//                        findNavController(R.id.nav_host_fragment).navigate(action)
//                        true
//                    } else {
//                        false
//                    }
//                }
//                R.id.search_tab -> {
//                    screensNavigator.goTo(
//                        SearchFragment.newInstance()
//                    )
//                    true
//                }
//                R.id.favourite_tab -> {
//                    true
//                }
//                else -> {
//                    false
//                }
//            }
//        }
//    }
}
