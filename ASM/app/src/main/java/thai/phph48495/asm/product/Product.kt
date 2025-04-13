package thai.phph48495.asm.product

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id")
    val id: String,
    val name: String,
    val price: Double,
    val stars: Double,
    val reviews: Int,
    val description: String,
    val image: String,
    val category: String = ""
)
