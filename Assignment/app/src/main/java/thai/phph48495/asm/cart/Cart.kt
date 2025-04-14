package thai.phph48495.asm.cart

import com.google.gson.annotations.SerializedName

data class Cart(
    @SerializedName("id")
    val id: String,
    val productId: String,
    val userId: String,
    val quantity: Int,
    val totalCartItem: Double,
    val nameProduct: String,
    val priceProduct: Double,
    val imageProduct: String
)

data class AddToCartRequest(
    val userId: String,
    val productId: String,
    val quantity: Int,
    val totalCartItem: Double,
    val nameProduct: String,
    val priceProduct: Double,
    val imageProduct: String
)

data class UpdateCartQuantityRequest(
    val quantity: Int,
    val totalCartItem: Double? = null
)

data class TotalCartResponse(
    val totalMoney: Double
)