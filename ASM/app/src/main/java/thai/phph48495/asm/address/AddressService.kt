package thai.phph48495.asm.address

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import thai.phph48495.asm.contants.Contants
import thai.phph48495.asm.profile.User

interface AddressService {
    @GET("users/{userId}")
    suspend fun getUserWithAddresses(@Path("userId") userId: String): Response<User>
    
    @PUT("users/{userId}")
    suspend fun updateUserAddresses(@Path("userId") userId: String, @Body user: User): Response<User>

    companion object {
        private var retrofitService: AddressService? = null
        fun getInstance(): AddressService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(Contants.DOMAIN)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(AddressService::class.java)
            }
            return retrofitService!!
        }
    }
}