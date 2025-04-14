package thai.phph48495.asm.cart

import retrofit2.http.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import thai.phph48495.asm.contants.Contants

interface CartService {
    // Lấy giỏ hàng theo userId
    @GET("carts")
    suspend fun getCartsByUserId(@Query("userId") userId: String): List<Cart>

    // Thêm sản phẩm vào giỏ hàng
    @POST("carts")
    suspend fun addToCart(@Body request: AddToCartRequest): Cart

    // Lấy chi tiết giỏ hàng
    @GET("carts/{id}")
    suspend fun getCartById(@Path("id") id: String): Cart

    // Cập nhật số lượng
    @PATCH("carts/{id}")
    suspend fun updateCartQuantity(
        @Path("id") id: String,
        @Body request: UpdateCartQuantityRequest
    ): Cart

    // Xóa giỏ hàng
    @DELETE("carts/{id}")
    suspend fun deleteCart(@Path("id") id: String)

    // Lấy tổng tiền giỏ hàng theo userId
    @GET("carts")
    suspend fun getTotalCartByUserId(@Query("userId") userId: String): Double

    companion object {
        private var retrofitService: CartService? = null
        fun getInstance(): CartService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(Contants.DOMAIN)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(CartService::class.java)
            }
            return retrofitService!!
        }
    }
}