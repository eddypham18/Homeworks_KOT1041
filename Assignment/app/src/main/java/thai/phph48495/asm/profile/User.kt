package thai.phph48495.asm.profile

import thai.phph48495.asm.address.Address
import thai.phph48495.asm.paymentMethod.PaymentMethod

data class User(
    val id: String,
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val phone: String? = "",
    var addresses: List<Address> = listOf(),
    var paymentMethods: List<PaymentMethod> = listOf()
)