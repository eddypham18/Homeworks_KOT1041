package thai.phph48495.asm.profile

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import thai.phph48495.asm.address.Address
import thai.phph48495.asm.paymentMethod.PaymentMethod

class ProfileViewModel : ViewModel() {
    private val profileService = ProfileService.getInstance()
    
    // Thông tin người dùng
    private val _user = mutableStateOf<User?>(null)
    val user = _user
    
    // Trạng thái loading
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
    
    // Thông báo lỗi
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    // Danh sách địa chỉ của người dùng
    private val _addresses = mutableStateOf<List<Address>>(emptyList())
    val addresses = _addresses
    
    // Danh sách phương thức thanh toán
    private val _paymentMethods = mutableStateOf<List<PaymentMethod>>(emptyList())
    val paymentMethods = _paymentMethods
    
    // Lấy thông tin cá nhân của người dùng
    fun getUserProfile(userId: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                
                val response = profileService.getUserProfile(userId)
                if (response.isSuccessful) {
                    _user.value = response.body()
                } else {
                    _error.value = "Không thể lấy thông tin người dùng: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Lỗi: ${e.message}"
                Log.e("ProfileViewModel", "Error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }
    
    // Lấy danh sách địa chỉ của người dùng
    fun getUserAddresses(userId: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                
                val response = profileService.getUserAddresses(userId)
                if (response.isSuccessful) {
                    _addresses.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Không thể lấy danh sách địa chỉ: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Lỗi: ${e.message}"
                Log.e("ProfileViewModel", "Error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }
    
    // Lấy danh sách phương thức thanh toán
    fun getUserPaymentMethods(userId: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                
                val response = profileService.getUserPaymentMethods(userId)
                if (response.isSuccessful) {
                    _paymentMethods.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Không thể lấy danh sách phương thức thanh toán: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Lỗi: ${e.message}"
                Log.e("ProfileViewModel", "Error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }
    
    // Cập nhật thông tin người dùng
    fun updateUserProfile(user: User, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                
                val response = profileService.updateUserProfile(user.id, user)
                if (response.isSuccessful) {
                    _user.value = response.body()
                    onSuccess()
                } else {
                    _error.value = "Không thể cập nhật thông tin người dùng: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Lỗi: ${e.message}"
                Log.e("ProfileViewModel", "Error: ${e.message}")
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