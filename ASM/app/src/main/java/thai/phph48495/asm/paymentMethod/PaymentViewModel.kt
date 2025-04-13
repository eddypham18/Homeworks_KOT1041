package thai.phph48495.asm.paymentMethod

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class PaymentViewModel: ViewModel() {
    private val paymentService = PaymentService.getInstance()
    
    // Danh sách phương thức thanh toán
    private val _paymentMethods = mutableStateOf<List<PaymentMethod>>(emptyList())
    val paymentMethods = _paymentMethods
    
    // Trạng thái loading
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
    
    // Thông báo lỗi
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    // Lấy danh sách phương thức thanh toán của người dùng
    fun getUserPaymentMethods(userId: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                
                val response = paymentService.getUserPaymentMethods(userId)
                if (response.isSuccessful) {
                    _paymentMethods.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Không thể lấy danh sách phương thức thanh toán: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Lỗi: ${e.message}"
                Log.e("PaymentViewModel", "Error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }
    
    // Thêm phương thức thanh toán mới
    fun addPaymentMethod(userId: String, cardNumber: String, cvv: String, expirationDate: String, isDefault: Boolean, onSuccess: () -> Unit) {
        // Kiểm tra các trường bắt buộc
        if (userId.isBlank() || cardNumber.isBlank() || cvv.isBlank() || expirationDate.isBlank()) {
            _error.value = "Thông tin thẻ không đầy đủ"
            return
        }
        
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                
                val request = AddPaymentMethodRequest(
                    userId = userId,
                    cardNumber = cardNumber,
                    cvv = cvv,
                    expirationDate = expirationDate,
                    isDefault = isDefault
                )
                
                val response = paymentService.addPaymentMethod(request)
                if (response.isSuccessful) {
                   onSuccess()
                    // Cập nhật danh sách phương thức thanh toán
                    getUserPaymentMethods(userId)
                } else {
                    _error.value = "Không thể thêm phương thức thanh toán: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Lỗi: ${e.message}"
                Log.e("PaymentViewModel", "Error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }
    
    // Cập nhật trạng thái mặc định
    fun updateDefaultPaymentMethod(method: PaymentMethod, newIsDefault: Boolean) {
        // Nếu đang cố đặt về false cho phương thức mặc định thì không cho phép
        // Luôn phải có một phương thức thanh toán mặc định
        if (!newIsDefault && method.isDefault) return
        
        // Nếu phương thức đã được đặt là mặc định và đang cố đặt là mặc định thì không làm gì
        if (method.isDefault && newIsDefault) return
        
        val userId = method.userId ?: return
        
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                
                // Gọi API để cập nhật trạng thái mặc định
                val response = paymentService.setDefaultPaymentMethod(method.id)
                
                if (response.isSuccessful) {
                    // Cập nhật danh sách phương thức thanh toán với trạng thái mới
                    // Chỉ có một phương thức thanh toán được đặt làm mặc định
                    val updatedList = _paymentMethods.value.map { paymentMethod ->
                        if (paymentMethod.id == method.id) {
                            // Đặt phương thức này làm mặc định
                            paymentMethod.copy(isDefault = true)
                        } else {
                            // Đặt tất cả các phương thức khác không phải mặc định
                            paymentMethod.copy(isDefault = false)
                        }
                    }
                    _paymentMethods.value = updatedList
                } else {
                    _error.value = "Không thể đặt phương thức thanh toán mặc định: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Lỗi: ${e.message}"
                Log.e("PaymentViewModel", "Error updating default payment method", e)
            } finally {
                _loading.value = false
            }
        }
    }
    
    // Xóa phương thức thanh toán
    fun deletePaymentMethod(id: String, userId: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                
                val response = paymentService.deletePaymentMethod(id)
                if (response.isSuccessful) {
                    getUserPaymentMethods(userId)
                } else {
                    _error.value = "Không thể xóa phương thức thanh toán: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Lỗi: ${e.message}"
                Log.e("PaymentViewModel", "Error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }


    // Đặt phương thức thanh toán làm mặc định
    fun setDefaultPaymentMethod(paymentMethodId: String, userId: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                
                // Gọi API để đặt phương thức thanh toán làm mặc định
                val response = paymentService.setDefaultPaymentMethod(paymentMethodId)
                
                if (response.isSuccessful) {
                    // Sau khi đặt phương thức mặc định thành công, cập nhật lại danh sách
                    getUserPaymentMethods(userId)
                } else {
                    _error.value = "Không thể đặt phương thức thanh toán mặc định: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Lỗi khi đặt phương thức thanh toán mặc định: ${e.message}"
                Log.e("PaymentViewModel", "Error setting default payment method", e)
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