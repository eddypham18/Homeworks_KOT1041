package thai.phph48495.asm.cart

data class Cart(
    val _id : String,
    val productId: String,
    val userId: String,
    val quantity: Int,
    val totalCartItem: Double,
    val nameProduct: String,
    val priceProduct: Double,
    val imageProduct: String
)

data class AddToCart(
    val userId: String,
    val productId: String,
    val quantity: Int
)

data class TotalMoneyResponse(
    val totalMoney : Double
)