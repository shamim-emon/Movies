package bd.emon.movies.common.navigation

import androidx.fragment.app.Fragment
import bd.emon.movies.home.HomeFragment
import java.util.Stack
import javax.inject.Inject
import kotlin.collections.HashMap

class FragmentStackManager @Inject constructor() {
    private lateinit var stackMap: HashMap<FragStackTitle, Stack<Fragment>>
    private var currentStack: FragStackTitle

    init {
        initStackMap()
        currentStack = FragStackTitle.HOME_STACK
    }

    private fun initStackMap() {
        stackMap = HashMap()
        stackMap[FragStackTitle.HOME_STACK] = Stack()
        stackMap[FragStackTitle.TRENDING_STACK] = Stack()
        stackMap[FragStackTitle.SEARCH_STACK] = Stack()
        stackMap[FragStackTitle.FAVOURITE_STACK] = Stack()
    }

    fun addToStack(fragment: Fragment) {
        when (fragment) {
            is HomeFragment -> {
                currentStack = FragStackTitle.HOME_STACK
            }
        }
        stackMap[currentStack]?.push(fragment)
    }

    fun peek(): Fragment {
        return stackMap[currentStack]!!.peek()
    }

    fun pop() {
        stackMap[currentStack]?.pop()
    }

    fun currentStackSize() = stackMap[currentStack]!!.size
}
