package thai.phph48495.asm.favorite

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import thai.phph48495.asm.contants.Contants

interface FavoriteService {
    @GET("favorites")
    suspend fun getFavorites(@Query("userId") userId: String): Response<List<Favorite>>
    
    @POST("favorites")
    suspend fun addToFavorite(@Body request: AddFavoriteRequest): Response<Favorite>
    
    @DELETE("favorites/{favoriteId}")
    suspend fun deleteFavorite(@Path("favoriteId") favoriteId: String): Response<Void>
    
    @GET("favorites/check")
    suspend fun checkFavorite(
        @Query("userId") userId: String,
        @Query("productId") productId: String
    ): Response<Boolean>

    companion object {
        private var retrofitService: FavoriteService? = null
        fun getInstance(): FavoriteService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(Contants.DOMAIN)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(FavoriteService::class.java)
            }
            return retrofitService!!
        }
    }
}