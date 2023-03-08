package bd.emon.data.rest

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RestApiServiceProvider(
    private val converterFactory: GsonConverterFactory,
    private val rxAdapter: RxJava3CallAdapterFactory
) {
    private val BASE_URL = "https://api.themoviedb.org/3/"

    private fun getRestAdapter(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(rxAdapter)
            .build()
    }

    private var restAdapter = getRestAdapter(BASE_URL)
    fun providerTMDBApiService() = restAdapter.create(MovieRestApiInterface::class.java)
}
