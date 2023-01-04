package bd.emon.movies.common.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import bd.emon.movies.home.HomeFragment
import java.io.Serializable
import javax.inject.Inject

class ScreensNavigator @Inject constructor(
    private val fragmentManager: FragmentManager,
//    @IdRes
//    private val containerId: Int,
    private val stackManager: FragmentStackManager

) : Serializable {

    private lateinit var homeFragment: HomeFragment

    fun goTo(fragment: Fragment) {
        if (fragment is HomeFragment) {
            homeFragment = fragment
        } else {
            stackManager.addToStack(fragment)
        }
        showFragment(fragment)
    }

    private fun showFragment(fragment: Fragment) {
        fragmentManager
            .beginTransaction()
            // .replace(containerId, fragment)
            .disallowAddToBackStack()
            .commit()
    }

    fun back() {
        stackManager.pop()
        if (stackManager.currentStackSize() == 0) {
            showFragment(homeFragment)
        } else {
            showFragment(stackManager.peek())
        }
    }
}
