package thai.phph48495.asm.notification

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import thai.phph48495.asm.contants.Contants

interface NotificationService {
    @GET("notifications")
    suspend fun getUserNotifications(@Query("userId") userId: String): Response<List<Notify>>
    
    @POST("notifications")
    suspend fun createNotification(@Body notification: Notify): Response<Notify>
    
    @PUT("notifications/{id}")
    suspend fun markAsRead(@Path("id") id: String): Response<Notify>
    
    @DELETE("notifications/{id}")
    suspend fun deleteNotification(@Path("id") id: String): Response<Void>
    
    companion object {
        private var retrofitService: NotificationService? = null
        fun getInstance(): NotificationService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(Contants.DOMAIN)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(NotificationService::class.java)
            }
            return retrofitService!!
        }
    }
} 