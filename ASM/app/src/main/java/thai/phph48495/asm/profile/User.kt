package thai.phph48495.asm.profile

import thai.phph48495.asm.address.Address
import thai.phph48495.asm.paymentMethod.PaymentMethod

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String? = "",
    var addresses: List<Address> = listOf(),
    var paymentMethods: List<PaymentMethod> = listOf()
)