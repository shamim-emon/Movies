package bd.emon.movies.home

import java.util.Calendar

class MovieReleaseYearsProviderImpl(private val calendar: Calendar) : MovieReleaseYearsProvider {

    override fun getReleaseYearsList(): List<Int> {
        val currentYear = calendar.get(Calendar.YEAR)
        val mutableList = mutableListOf<Int>()
        for (year in currentYear downTo 1911) {
            mutableList.add(year)
        }
        return mutableList.toList()
    }
}
