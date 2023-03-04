package bd.emon.movies.favourite

object FavouriteStatusChangeListener {
    private var subscriber: FavouriteStatusChangeSubscriber? = null

    fun addSubscriber(subscriber: FavouriteStatusChangeSubscriber) {
        this.subscriber = subscriber
    }

    fun removeSubcriber() {
        this.subscriber = null
    }

    fun onChange() {
        this.subscriber?.reloadFavourites()
    }
}
