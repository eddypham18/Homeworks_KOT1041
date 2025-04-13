package thai.phph48495.asm.profile

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import thai.phph48495.asm.contants.Contants
import thai.phph48495.asm.address.Address
import thai.phph48495.asm.paymentMethod.PaymentMethod

interface ProfileService {
    @GET("users/{id}")
    suspend fun getUserProfile(@Path("id") id: String): Response<User>
    
    @PUT("users/{id}")
    suspend fun updateUserProfile(
        @Path("id") id: String,
        @Body user: User
    ): Response<User>
    
    @GET("users/{id}/addresses")
    suspend fun getUserAddresses(@Path("id") id: String): Response<List<Address>>
    
    @GET("users/{id}/paymentMethods")
    suspend fun getUserPaymentMethods(@Path("id") id: String): Response<List<PaymentMethod>>
    
    companion object {
        private var retrofitService: ProfileService? = null
        fun getInstance(): ProfileService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(Contants.DOMAIN)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(ProfileService::class.java)
            }
            return retrofitService!!
        }
    }
} 