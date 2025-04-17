package thai.phph48495.test1vid.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import thai.phph48495.test1vid.models.Cat

interface ApiService {

    @GET("/api/cats")
    suspend fun getAllCats():List<Cat>

    companion object {
        private var retrofitService: ApiService? = null
        private val BASE_URL = "https://cataas.com/"

        fun getInstance(): ApiService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(ApiService::class.java)
            }
            return retrofitService!!
        }
    }
}