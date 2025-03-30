package thai.phph48495.asm.order.order

data class Order(
    val _id: String,
    val OrderID: Int,
    val userId: String,
    val cartId: String,
    val totalMoney: Double,
    val totalQuantity: Int,
    val nameUser: String,
    val addressUser:String,
    val paymentUser: String,
    val date: String,
    val items: List<OrderItem>
)
data class OrderItem(
    val nameProduct: String,
    val quantity: String,
)
