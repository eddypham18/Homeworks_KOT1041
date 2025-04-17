package thai.phph48495.dethithu02.service

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import thai.phph48495.dethithu02.models.Dog

interface ApiService {

    @GET("/dogs")
    suspend fun getAllDogs(): List<Dog>

    @POST("/dogs")
    suspend fun addDog(@Body dog: Dog): Response<Dog>

    @DELETE("/dogs/{id}")
    suspend fun deleleDog(@Path("id") id: String): Response<Dog>

    @PUT("/dogs/{id}")
    suspend fun updateDog(@Path("id") id: String, @Body dog: Dog): Response<Dog>

    companion object {
        private var retrofitService: ApiService? = null
        private val BASE_URL = "http://10.0.2.2:3000/"

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