package thai.phph48495.asm.paymentMethod

data class PaymentMethod(
    val idMethod: String = "",
    val cardNumber: String = "",
    val cvv: String = "",
    val expirationDate: String = "",
    var isDefault: Boolean = false,
)