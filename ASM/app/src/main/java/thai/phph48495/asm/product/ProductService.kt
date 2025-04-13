package thai.phph48495.asm.product

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import thai.phph48495.asm.contants.Contants


interface ProductService {
    @GET("products")
    suspend fun getProducts(): List<Product>
    
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: String): Response<Product>
    
    @GET("products")
    suspend fun getProductsByCategory(@Query("category") category: String): List<Product>

    companion object {
        private var retrofitService: ProductService? = null
        fun getInstance(): ProductService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(Contants.DOMAIN)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(ProductService::class.java)
            }
            return retrofitService!!
        }
    }
}

