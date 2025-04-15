package thai.phph48495.asm.cart

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import thai.phph48495.asm.product.Product
import thai.phph48495.asm.product.ProductService
import java.util.UUID

class CartViewModel: ViewModel() {
    private val cartService = CartService.getInstance()
    private val productService = ProductService.getInstance()

    // Danh sách giỏ hàng
    private val _cartItems = mutableStateOf<List<Cart>?>(null)
    val cartItems: State<List<Cart>?> = _cartItems

    // Tổng tiền giỏ hàng
    private val _totalCart = mutableStateOf(0.0)
    val totalCart: State<Double> = _totalCart

    // Trạng thái loading
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    // Thông báo lỗi
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Lấy giỏ hàng theo userId
    fun getCartsByUserId(userId: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                _cartItems.value = cartService.getCartsByUserId(userId)
                calculateTotal()
            } catch (e: Exception) {
                _error.value = "Không thể tải giỏ hàng: ${e.message}"
                Log.d("CartViewModel", "Error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

    // Thêm sản phẩm vào giỏ hàng
    fun addToCart(userId: String, product: Product, quantity: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

                // Kiểm tra sản phẩm đã có trong giỏ hàng chưa
                val existingCarts = cartService.getCartsByUserId(userId)
                val existingCart = existingCarts.find { it.productId == product.id }

                if (existingCart != null) {
                    // Nếu sản phẩm đã có trong giỏ hàng, cập nhật số lượng
                    val newQuantity = existingCart.quantity + quantity
                    val updateRequest = UpdateCartQuantityRequest(newQuantity)
                    cartService.updateCartQuantity(existingCart.id, updateRequest)
                } else {
                    // Nếu sản phẩm chưa có trong giỏ hàng, thêm mới
                    val totalCartItem = product.price * quantity
                    val cartRequest = AddToCartRequest(
                        userId = userId,
                        productId = product.id,
                        quantity = quantity,
                        totalCartItem = totalCartItem,
                        nameProduct = product.name,
                        priceProduct = product.price,
                        imageProduct = product.image
                    )
                    cartService.addToCart(cartRequest)
                }

                // Làm mới giỏ hàng
                getCartsByUserId(userId)
            } catch (e: Exception) {
                _error.value = "Không thể thêm vào giỏ hàng: ${e.message}"
                Log.d("CartViewModel", "Error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    fun updateCartQuantity(cartId: String, userId: String, quantity: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

                // Lấy thông tin giỏ hàng hiện tại để tính lại tổng tiền
                val cart = _cartItems.value?.find { it.id == cartId }
                if (cart != null) {
                    // Tính lại tổng tiền mỗi mục
                    val newTotalCartItem = cart.priceProduct * quantity

                    // Cập nhật số lượng và tổng tiền
                    val updateRequest = UpdateCartQuantityRequest(
                        quantity = quantity,
                        totalCartItem = newTotalCartItem
                    )
                    cartService.updateCartQuantity(cartId, updateRequest)

                    // Làm mới giỏ hàng
                    getCartsByUserId(userId)
                } else {
                    val updateRequest = UpdateCartQuantityRequest(quantity)
                    cartService.updateCartQuantity(cartId, updateRequest)

                    // Làm mới giỏ hàng
                    getCartsByUserId(userId)
                }
            } catch (e: Exception) {
                _error.value = "Không thể cập nhật giỏ hàng: ${e.message}"
                Log.d("CartViewModel", "Error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

    // Xóa sản phẩm khỏi giỏ hàng
    fun deleteCart(cartId: String, userId: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

                cartService.deleteCart(cartId)

                // Làm mới giỏ hàng
                getCartsByUserId(userId)
            } catch (e: Exception) {
                _error.value = "Không thể xóa khỏi giỏ hàng: ${e.message}"
                Log.d("CartViewModel", "Error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

    // Tính tổng tiền giỏ hàng
    private fun calculateTotal() {
        _cartItems.value?.let { carts ->
            _totalCart.value = carts.sumOf { it.totalCartItem }
        }
    }

    // Reset lỗi
    fun resetError() {
        _error.value = null
    }
}