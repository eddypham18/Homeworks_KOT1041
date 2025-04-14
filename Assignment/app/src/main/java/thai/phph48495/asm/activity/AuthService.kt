package thai.phph48495.asm.activity

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import thai.phph48495.asm.contants.Contants
import thai.phph48495.asm.profile.User

interface AuthService {
    @GET("users")
    suspend fun getUsers(): List<User>
    
    @GET("users")
    suspend fun getUserByEmail(@Query("email") email: String): List<User>
    
    @POST("users")
    suspend fun registerUser(@Body user: User): User

    companion object {
        private var retrofitService: AuthService? = null
        fun getInstance(): AuthService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(Contants.DOMAIN)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(AuthService::class.java)
            }
            return retrofitService!!
        }
    }
}