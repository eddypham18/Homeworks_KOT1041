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
    fun updateDefaultPaymentMethod(method: PaymentMethod, shouldSetDefault: Boolean) {
        val userId = method.userId ?: return
        
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                
                // Lưu lại danh sách hiện tại để khôi phục nếu có lỗi
                val originalList = _paymentMethods.value
                
                // Tạo một danh sách mới 
                val updatedList = if (shouldSetDefault) {
                    // Nếu đặt làm mặc định, cập nhật tất cả các phương thức
                    _paymentMethods.value.map { paymentMethod ->
                        if (paymentMethod.id == method.id) {
                            paymentMethod.copy(isDefault = true)
                        } else {
                            paymentMethod.copy(isDefault = false)
                        }
                    }
                } else {
                    // Nếu bỏ phương thức mặc định, chỉ cập nhật phương thức hiện tại
                    _paymentMethods.value.map { paymentMethod ->
                        if (paymentMethod.id == method.id) {
                            paymentMethod.copy(isDefault = false)
                        } else {
                            paymentMethod
                        }
                    }
                }
                
                // Cập nhật UI ngay lập tức
                _paymentMethods.value = updatedList
                
                try {
                    // Cập nhật phương thức hiện tại trên server
                    val updateRequest = UpdatePaymentMethodRequest(
                        userId = method.userId,
                        cardNumber = method.cardNumber,
                        cvv = method.cvv,
                        expirationDate = method.expirationDate,
                        isDefault = shouldSetDefault
                    )
                    
                    val response = paymentService.updatePaymentMethod(method.id, updateRequest)
                    
                    if (response.isSuccessful && shouldSetDefault) {
                        // Nếu đặt làm mặc định thành công, cập nhật các phương thức khác thành không mặc định
                        val otherMethods = _paymentMethods.value.filter { it.id != method.id && it.isDefault }
                        
                        for (otherMethod in otherMethods) {
                            val otherRequest = UpdatePaymentMethodRequest(
                                userId = otherMethod.userId,
                                cardNumber = otherMethod.cardNumber,
                                cvv = otherMethod.cvv,
                                expirationDate = otherMethod.expirationDate,
                                isDefault = false
                            )
                            
                            paymentService.updatePaymentMethod(otherMethod.id, otherRequest)
                        }
                    } else if (!response.isSuccessful) {
                        // Nếu cập nhật thất bại, khôi phục trạng thái cũ và hiển thị lỗi
                        _paymentMethods.value = originalList
                        _error.value = "Không thể cập nhật trạng thái mặc định"
                        getUserPaymentMethods(userId)
                    }
                } catch (e: Exception) {
                    // Nếu có lỗi, khôi phục trạng thái cũ và hiển thị lỗi
                    _paymentMethods.value = originalList
                    _error.value = "Lỗi: ${e.message}"
                    getUserPaymentMethods(userId)
                }
            } catch (e: Exception) {
                _error.value = "Lỗi: ${e.message}"
                Log.e("PaymentViewModel", "Lỗi khi cập nhật phương thức thanh toán mặc định", e)
                getUserPaymentMethods(userId)
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
    
    // Reset lỗi
    fun resetError() {
        _error.value = null
    }
}