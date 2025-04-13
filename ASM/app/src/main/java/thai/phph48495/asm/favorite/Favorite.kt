package thai.phph48495.asm.favorite

import thai.phph48495.asm.product.Product

data class Favorite(
    val id: String,
    val userId: String,
    val productId: String,
    val isFavorite: Boolean,
    val nameProduct: String,
    val priceProduct: Double,
    val imageProduct: String
)

data class AddFavoriteRequest(
    val userId: String,
    val productId: String
)
data class DeleteFavorite(
    val userId: String,
    val productId: String
)