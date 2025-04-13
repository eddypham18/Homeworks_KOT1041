package thai.ph48495.asm.services

import thai.ph48495.asm.product.Product
import retrofit2.Response
import retrofit2.http.GET

interface ProductService {
    @GET("/products")
    suspend fun getProducts(): Response<List<Product>>

}