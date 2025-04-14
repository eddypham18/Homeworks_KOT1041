package thai.phph48495.asm.paymentMethod

import com.google.gson.annotations.SerializedName

data class PaymentMethod(
    val id: String,
    val userId: String = "",
    val cardNumber: String = "",
    val cvv: String = "",
    val expirationDate: String = "",
    var isDefault: Boolean = false
)

data class AddPaymentMethodRequest(
    val userId: String,
    val cardNumber: String,
    val cvv: String,
    val expirationDate: String,
    val isDefault: Boolean
)

data class UpdatePaymentMethodRequest(
    val userId: String? = null,
    val cardNumber: String? = null,
    val cvv: String? = null,
    val expirationDate: String? = null,
    val isDefault: Boolean? = null
)