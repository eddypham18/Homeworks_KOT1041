package thai.phph48495.asm.order.order

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import thai.phph48495.asm.contants.Contants

interface OrderService {
    @GET("orders")
    suspend fun getUserOrders(@Query("userId") userId: String): Response<List<Order>>
    
    @GET("orders/{id}")
    suspend fun getOrderById(@Path("id") id: String): Response<Order>
    
    @POST("orders")
    suspend fun createOrder(@Body order: Order): Response<Order>
    
    @PUT("orders/{id}")
    suspend fun updateOrder(@Path("id") id: String, @Body order: Order): Response<Order>
    
    companion object {
        private var retrofitService: OrderService? = null
        fun getInstance(): OrderService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(Contants.DOMAIN)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(OrderService::class.java)
            }
            return retrofitService!!
        }
    }
} 