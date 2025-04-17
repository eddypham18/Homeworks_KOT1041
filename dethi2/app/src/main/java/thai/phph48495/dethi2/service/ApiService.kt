package thai.phph48495.dethi2.service

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import thai.phph48495.dethi2.models.Car

interface ApiService {

    @GET("/cars")
    suspend fun getAllCars(): List<Car>

    @POST("/cars")
    suspend fun addCar(@Body car: Car): Response<Car>

    @DELETE("/cars/{id}")
    suspend fun deleteCar(@Path("id") id: String): Response<Unit>

    @PUT("/cars/{id}")
    suspend fun updateCar(@Path("id") id: String, @Body car: Car): Response<Car>

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