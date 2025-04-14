package thai.phph48495.asm.product

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ProductViewModel: ViewModel() {
    //lay du lieu tu server
    private val productService = ProductService.getInstance()
    
    // Danh sách sản phẩm
    private val _listProduct = mutableStateOf<List<Product>?>(null)
    val listProduct: State<List<Product>?> = _listProduct
    
    // Chi tiết sản phẩm
    private val _productDetail = mutableStateOf<Product?>(null)
    val productDetail: State<Product?> = _productDetail
    
    // Trạng thái loading
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
    
    // Thông báo lỗi
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    // Lấy tất cả sản phẩm
    fun getAllProducts() {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                _listProduct.value = productService.getProducts()
            } catch (e: Exception) {
                _error.value = "Không thể tải danh sách sản phẩm: ${e.message}"
                Log.d("ProductViewModel", "Error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }
    
    // Lấy sản phẩm theo danh mục
    fun getProductsByCategory(category: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                _listProduct.value = productService.getProductsByCategory(category)
            } catch (e: Exception) {
                _error.value = "Không thể tải sản phẩm theo danh mục: ${e.message}"
                Log.d("ProductViewModel", "Error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }
    
    // Lấy chi tiết sản phẩm
    fun getProductById(id: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                _productDetail.value = productService.getProductById(id).body()
            } catch (e: Exception) {
                _error.value = "Không thể tải chi tiết sản phẩm: ${e.message}"
                Log.d("ProductViewModel", "Error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }
    
    // Reset lỗi
    fun resetError() {
        _error.value = null
    }
}