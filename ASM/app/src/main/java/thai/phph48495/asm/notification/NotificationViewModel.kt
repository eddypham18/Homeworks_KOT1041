package thai.phph48495.asm.notification

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date

class NotificationViewModel : ViewModel() {
    private val notificationService = NotificationService.getInstance()
    
    // Danh sách thông báo
    private val _notifications = mutableStateOf<List<Notify>>(emptyList())
    val notifications = _notifications
    
    // Trạng thái loading
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
    
    // Thông báo lỗi
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    // Lấy danh sách thông báo của người dùng
    fun getUserNotifications(userId: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                
                val response = notificationService.getUserNotifications(userId)
                if (response.isSuccessful) {
                    _notifications.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Không thể lấy danh sách thông báo: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Lỗi: ${e.message}"
                Log.e("NotificationViewModel", "Error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }
    
    // Tạo thông báo mới
    fun createNotification(
        userId: String,
        title: String,
        message: String,
        type: String = "order",
        imageUrl: String? = null,
        relatedId: String? = null
    ) {
        viewModelScope.launch {
            try {
                _loading.value = true
                
                val notification = Notify(
                    userId = userId,
                    title = title,
                    message = message,
                    dateCreated = Date(),
                    isRead = false,
                    type = type,
                    imageUrl = imageUrl,
                    relatedId = relatedId
                )
                
                val response = notificationService.createNotification(notification)
                if (response.isSuccessful) {
                    // Cập nhật danh sách thông báo nếu thêm thành công
                    getUserNotifications(userId)
                } else {
                    _error.value = "Không thể tạo thông báo: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Lỗi: ${e.message}"
                Log.e("NotificationViewModel", "Error creating notification: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }
    
    // Đánh dấu thông báo đã đọc
    fun markAsRead(notificationId: String, userId: String) {
        viewModelScope.launch {
            try {
                notificationService.markAsRead(notificationId)
                // Làm mới danh sách sau khi đánh dấu đã đọc
                getUserNotifications(userId)
            } catch (e: Exception) {
                Log.e("NotificationViewModel", "Error marking notification as read: ${e.message}")
            }
        }
    }
    
    // Xóa thông báo
    fun deleteNotification(notificationId: String, userId: String) {
        viewModelScope.launch {
            try {
                notificationService.deleteNotification(notificationId)
                // Làm mới danh sách sau khi xóa
                getUserNotifications(userId)
            } catch (e: Exception) {
                Log.e("NotificationViewModel", "Error deleting notification: ${e.message}")
            }
        }
    }
    
    // Reset lỗi
    fun resetError() {
        _error.value = null
    }
} 