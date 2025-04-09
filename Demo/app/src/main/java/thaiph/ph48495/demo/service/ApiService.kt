package thaiph.ph48495.demo.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import thaiph.ph48495.demo.models.Food

interface ApiService {
    @GET("foods")
    suspend fun getAllFoods(): List<Food>

    @DELETE("foods/{id}")
    suspend fun deleteFood(id: Int): Food
}

object RetrofitInstance{
    private const val BASE_URL="http://10.0.2.2:3000/"
    val  api :ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}