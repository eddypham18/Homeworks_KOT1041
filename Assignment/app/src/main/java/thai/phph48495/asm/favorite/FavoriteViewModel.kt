package thai.phph48495.asm.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import thai.phph48495.asm.product.Product
import thai.phph48495.asm.product.ProductService

class FavoriteViewModel: ViewModel() {
    private val favoriteService = FavoriteService.getInstance()

    // StateFlow cho danh sách sản phẩm yêu thích
    private val _favorites = MutableStateFlow<List<Favorite>>(emptyList())
    val favorites: StateFlow<List<Favorite>> = _favorites

    // StateFlow cho trạng thái loading
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    // StateFlow cho thông báo lỗi
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Lấy danh sách yêu thích của người dùng
    fun getFavorites(userId: String) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val response = favoriteService.getFavorites(userId)
                if (response.isSuccessful) {
                    val favoritesList = response.body() ?: emptyList()

                    // Nếu danh sách trống, cập nhật và kết thúc
                    if (favoritesList.isEmpty()) {
                        _favorites.value = emptyList()
                        _loading.value = false
                        return@launch
                    }

                    // Lấy thông tin chi tiết sản phẩm cho mỗi mục yêu thích
                    val productService = ProductService.getInstance()
                    val enhancedFavorites = mutableListOf<Favorite>()

                    favoritesList.forEach { favorite ->
                        try {
                            // Lấy thông tin chi tiết sản phẩm
                            val productResponse = productService.getProductById(favorite.productId)
                            if (productResponse.isSuccessful) {
                                val product = productResponse.body()
                                if (product != null) {
                                    // Tạo Favorite với đầy đủ thông tin sản phẩm
                                    val enhancedFavorite = Favorite(
                                        id = favorite.id,
                                        userId = favorite.userId,
                                        productId = favorite.productId,
                                        isFavorite = true,
                                        nameProduct = product.name,
                                        priceProduct = product.price.toDouble(),
                                        imageProduct = product.image ?: ""
                                    )
                                    enhancedFavorites.add(enhancedFavorite)
                                }
                            }
                        } catch (e: Exception) {
                            // Nếu không lấy được thông tin sản phẩm, vẫn giữ favorite với thông tin cơ bản
                            val basicFavorite = Favorite(
                                id = favorite.id,
                                userId = favorite.userId,
                                productId = favorite.productId,
                                isFavorite = true,
                                nameProduct = "Sản phẩm ${favorite.productId}",
                                priceProduct = 0.0,
                                imageProduct = ""
                            )
                            enhancedFavorites.add(basicFavorite)
                        }
                    }

                    _favorites.value = enhancedFavorites
                } else {
                    _error.value = "Không thể lấy danh sách yêu thích: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Lỗi: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    // Thêm sản phẩm vào danh sách yêu thích
    fun addToFavorite(userId: String, product: Product) {
        _loading.value = true
        viewModelScope.launch {
            try {
                // Tạo request chỉ với userId và productId
                val request = AddFavoriteRequest(
                    userId = userId,
                    productId = product.id
                )

                val response = favoriteService.addToFavorite(request)
                if (response.isSuccessful) {
                    // Tạo đối tượng Favorite đầy đủ từ thông tin sản phẩm
                    val newFavorite = response.body()
                    val favorite = Favorite(
                        id = newFavorite?.id ?: java.util.UUID.randomUUID().toString(),
                        userId = userId,
                        productId = product.id,
                        isFavorite = true,
                        nameProduct = product.name,
                        priceProduct = product.price,
                        imageProduct = product.image
                    )

                    // Cập nhật danh sách yêu thích trong UI
                    val currentList = _favorites.value.toMutableList()
                    val existingIndex = currentList.indexOfFirst { it.productId == product.id }
                    if (existingIndex >= 0) {
                        currentList[existingIndex] = favorite
                    } else {
                        currentList.add(favorite)
                    }
                    _favorites.value = currentList
                } else {
                    _error.value = "Không thể thêm vào yêu thích: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Lỗi: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    // Xóa sản phẩm khỏi danh sách yêu thích
    fun deleteFavorite(favoriteId: String) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val response = favoriteService.deleteFavorite(favoriteId)
                if (response.isSuccessful) {
                    // Xóa khỏi danh sách hiện tại
                    _favorites.value = _favorites.value.filter { it.id != favoriteId }
                } else {
                    _error.value = "Không thể xóa khỏi yêu thích: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Lỗi: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    // Xóa yêu thích dựa trên userId và productId
    fun removeFavoriteByProductId(userId: String, productId: String) {
        _loading.value = true
        viewModelScope.launch {
            try {
                // Tìm favorite item cần xóa
                val favoriteToDelete = _favorites.value.find {
                    it.userId == userId && it.productId == productId
                }

                if (favoriteToDelete != null) {
                    // Gọi API xóa favorite
                    val response = favoriteService.deleteFavorite(favoriteToDelete.id)
                    if (response.isSuccessful) {
                        // Cập nhật danh sách local
                        _favorites.value = _favorites.value.filter {
                            it.id != favoriteToDelete.id
                        }
                    } else {
                        _error.value = "Không thể xóa khỏi yêu thích: ${response.message()}"
                    }
                } else {
                    // Không tìm thấy mục yêu thích
                    _error.value = "Không tìm thấy sản phẩm trong danh sách yêu thích"
                    // Đảm bảo cập nhật danh sách yêu thích mới nhất
                    getFavorites(userId)
                }
            } catch (e: Exception) {
                _error.value = "Lỗi: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    // Kiểm tra sản phẩm có trong danh sách yêu thích không
    fun checkFavorite(userId: String, productId: String, onResult: (Boolean) -> Unit) {
        // Kiểm tra trong dữ liệu local trước
        if (_favorites.value.isNotEmpty()) {
            val isExist = _favorites.value.any { it.userId == userId && it.productId == productId }
            onResult(isExist)
            return
        }

        // Nếu chưa có dữ liệu local, gọi API để lấy danh sách và kiểm tra
        viewModelScope.launch {
            try {
                _loading.value = true
                // Lấy toàn bộ danh sách yêu thích từ API
                val response = favoriteService.getFavorites(userId)
                if (response.isSuccessful) {
                    _favorites.value = response.body() ?: emptyList()
                    // Kiểm tra sản phẩm trong danh sách vừa lấy
                    val isExist = _favorites.value.any { it.userId == userId && it.productId == productId }
                    onResult(isExist)
                } else {
                    onResult(false)
                    _error.value = "Không thể kiểm tra trạng thái yêu thích: ${response.message()}"
                }
            } catch (e: Exception) {
                onResult(false)
                _error.value = "Lỗi: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    // Xóa thông báo lỗi
    fun resetError() {
        _error.value = null
    }
}