package thai.phph48495.asm.paymentMethod

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import thai.phph48495.asm.contants.Contants

interface PaymentService {
    @GET("paymentMethods")
    suspend fun getUserPaymentMethods(@Query("userId") userId: String): Response<List<PaymentMethod>>

    @GET("paymentMethods/{id}")
    suspend fun getPaymentMethod(@Path("id") id: String): Response<PaymentMethod>

    @POST("paymentMethods")
    suspend fun addPaymentMethod(@Body request: AddPaymentMethodRequest): Response<PaymentMethod>

    @PUT("paymentMethods/{id}")
    suspend fun updatePaymentMethod(
        @Path("id") id: String,
        @Body request: UpdatePaymentMethodRequest
    ): Response<PaymentMethod>

    @DELETE("paymentMethods/{id}")
    suspend fun deletePaymentMethod(@Path("id") id: String): Response<Void>

    @PUT("paymentMethods/{id}/setDefault")
    suspend fun setDefaultPaymentMethod(@Path("id") id: String): Response<PaymentMethod>

    companion object {
        private var retrofitService: PaymentService? = null
        fun getInstance(): PaymentService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(Contants.DOMAIN)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(PaymentService::class.java)
            }
            return retrofitService!!
        }
    }
}