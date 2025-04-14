package thai.phph48495.asm.address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import thai.phph48495.asm.profile.User

class AddressViewModel: ViewModel() {
    private val addressService = AddressService.getInstance()
    
    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> = _currentUser
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error
    
    private val _operationSuccess = MutableLiveData<Boolean>()
    val operationSuccess: LiveData<Boolean> = _operationSuccess
    
    fun getUserAddresses(userId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = addressService.getUserWithAddresses(userId)
                if (response.isSuccessful) {
                    _currentUser.value = response.body()
                } else {
                    _error.value = "Không thể tải địa chỉ: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Lỗi: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updateDefaultAddress(userId: String, addressId: String) {
        _isLoading.value = true
        _operationSuccess.value = false
        viewModelScope.launch {
            try {
                val user = _currentUser.value ?: return@launch
                
                // Đánh dấu địa chỉ hiện tại làm mặc định và bỏ mặc định các địa chỉ khác
                val updatedAddresses = user.addresses.map { address ->
                    address.copy(isDefault = address.id == addressId)
                }
                
                // Tạo user mới với danh sách địa chỉ đã cập nhật
                val updatedUser = user.copy(addresses = updatedAddresses)
                
                // Cập nhật UI ngay lập tức trước khi gọi API để người dùng thấy thay đổi
                _currentUser.postValue(updatedUser)
                
                // Gọi API để lưu thay đổi
                val response = addressService.updateUserAddresses(userId, updatedUser)
                if (response.isSuccessful) {
                    // Cập nhật UI với dữ liệu từ response
                    _currentUser.postValue(response.body())
                    _operationSuccess.value = true
                } else {
                    _error.postValue("Không thể cập nhật địa chỉ: ${response.message()}")
                    // Khôi phục dữ liệu cũ nếu API thất bại
                    _currentUser.postValue(user)
                }
            } catch (e: Exception) {
                _error.postValue("Lỗi: ${e.message}")
                // Cố gắng lấy lại dữ liệu mới nhất từ server
                getUserAddresses(userId)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
    
    fun addAddress(userId: String, address: Address) {
        _isLoading.value = true
        _operationSuccess.value = false
        viewModelScope.launch {
            try {
                val user = _currentUser.value ?: return@launch
                
                // Thêm địa chỉ mới vào danh sách hiện tại
                val updatedAddresses = user.addresses.toMutableList()
                updatedAddresses.add(address)
                
                val updatedUser = user.copy(addresses = updatedAddresses)
                
                val response = addressService.updateUserAddresses(userId, updatedUser)
                if (response.isSuccessful) {
                    _currentUser.value = response.body()
                    _operationSuccess.value = true
                } else {
                    _error.value = "Không thể thêm địa chỉ: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Lỗi: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updateAddress(userId: String, updatedAddress: Address) {
        _isLoading.value = true
        _operationSuccess.value = false
        viewModelScope.launch {
            try {
                val user = _currentUser.value ?: return@launch
                
                // Cập nhật địa chỉ trong danh sách
                val updatedAddresses = user.addresses.map { address ->
                    if (address.id == updatedAddress.id) updatedAddress else address
                }
                
                val updatedUser = user.copy(addresses = updatedAddresses)
                
                val response = addressService.updateUserAddresses(userId, updatedUser)
                if (response.isSuccessful) {
                    _currentUser.value = response.body()
                    _operationSuccess.value = true
                } else {
                    _error.value = "Không thể cập nhật địa chỉ: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Lỗi: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun deleteAddress(userId: String, addressId: String) {
        _isLoading.value = true
        _operationSuccess.value = false
        viewModelScope.launch {
            try {
                val user = _currentUser.value ?: return@launch
                
                // Xóa địa chỉ khỏi danh sách
                val updatedAddresses = user.addresses.filter { it.id != addressId }
                
                val updatedUser = user.copy(addresses = updatedAddresses)
                
                val response = addressService.updateUserAddresses(userId, updatedUser)
                if (response.isSuccessful) {
                    _currentUser.value = response.body()
                    _operationSuccess.value = true
                } else {
                    _error.value = "Không thể xóa địa chỉ: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Lỗi: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun resetOperationStatus() {
        _operationSuccess.value = false
        _error.value = null
    }
}