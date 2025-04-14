package thai.phph48495.asm.address

data class Address(
    val id: String,
    val address: String = "",
    val country: String = "",
    val city: String = "",
    val district: String = "",
    var isDefault : Boolean = false
)