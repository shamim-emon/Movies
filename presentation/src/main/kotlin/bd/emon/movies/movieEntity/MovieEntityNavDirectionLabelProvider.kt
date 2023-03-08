package bd.emon.movies.movieEntity

import bd.emon.domain.navigation.NavDirectionLabel

class MovieEntityNavDirectionLabelProvider {

    fun getNavDirectionLabel(apiCallType: APICallType): NavDirectionLabel {
        return when (apiCallType) {
            APICallType.DISCOVER_PAGING -> NavDirectionLabel.HomeFragment
            APICallType.TRENDING_MOVIES -> NavDirectionLabel.TrendingFragment
            else -> throw RuntimeException("Invalid apiCallType")
        }
    }
}
