package thai.phph48495.asm.order.order

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import thai.phph48495.asm.notification.NotificationViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class OrderViewModel : ViewModel() {
    private val orderService = OrderService.getInstance()
    
    // Danh sách đơn hàng
    private val _orders = mutableStateOf<List<Order>>(emptyList())
    val orders = _orders
    
    // Danh sách đơn hàng được lọc theo trạng thái
    private val _filteredOrders = mutableStateOf<List<Order>>(emptyList())
    val filteredOrders = _filteredOrders
    
    // Chi tiết đơn hàng
    private val _orderDetail = mutableStateOf<Order?>(null)
    val orderDetail = _orderDetail
    
    // Trạng thái loading
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
    
    // Thông báo lỗi
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    // Trạng thái tạo đơn hàng
    private val _orderCreated = MutableStateFlow(false)
    val orderCreated: StateFlow<Boolean> = _orderCreated
    
    // Lấy danh sách đơn hàng của người dùng
    fun getUserOrders(userId: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                
                val response = orderService.getUserOrders(userId)
                if (response.isSuccessful) {
                    _orders.value = response.body() ?: emptyList()
                    // Mặc định lọc theo đã giao
                    filterOrdersByStatus("delivered")
                } else {
                    _error.value = "Không thể lấy danh sách đơn hàng: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Lỗi: ${e.message}"
                Log.e("OrderViewModel", "Error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }
    
    // Lọc đơn hàng theo trạng thái
    fun filterOrdersByStatus(status: String) {
        val allOrders = _orders.value
        _filteredOrders.value = when (status) {
            "processing" -> allOrders.filter { it.status == "processing" }
            "delivered" -> allOrders.filter { it.status == "delivered" }
            "canceled" -> allOrders.filter { it.status == "canceled" }
            else -> allOrders
        }
    }
    
    // Lấy chi tiết đơn hàng
    fun getOrderDetail(orderId: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                
                val response = orderService.getOrderById(orderId)
                if (response.isSuccessful) {
                    _orderDetail.value = response.body()
                } else {
                    _error.value = "Không thể lấy chi tiết đơn hàng: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Lỗi: ${e.message}"
                Log.e("OrderViewModel", "Error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }
    
    // Tạo đơn hàng mới
    fun createOrder(
        userId: String,
        totalMoney: Double,
        totalQuantity: Int,
        nameUser: String,
        addressUser: String,
        paymentUser: String,
        items: List<OrderItem>,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                _orderCreated.value = false
                
                // Tạo ID mới và OrderID
                val orderId = UUID.randomUUID().toString().substring(0, 8)
                val orderNumber = System.currentTimeMillis() % 10000
                
                // Lấy ngày hiện tại
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val currentDate = sdf.format(Date())
                
                // Tạo đối tượng Order với trạng thái "processing"
                val order = Order(
                    id = orderId,
                    OrderID = orderNumber.toInt(),
                    userId = userId,
                    cartId = null,
                    totalMoney = totalMoney,
                    totalQuantity = totalQuantity,
                    nameUser = nameUser,
                    addressUser = addressUser,
                    paymentUser = paymentUser,
                    date = currentDate,
                    items = items,
                    status = "processing"
                )
                
                // Gọi API để tạo đơn hàng
                val response = orderService.createOrder(order)
                if (response.isSuccessful) {
                    _orderCreated.value = true
                    onSuccess()
                    
                    // Tạo thông báo khi đơn hàng được tạo thành công
                    createOrderNotification(userId, orderId, orderNumber.toInt())
                    
                    // Lấy lại danh sách đơn hàng để cập nhật UI
                    getUserOrders(userId)
                    
                    // Sau 5 phút, cập nhật trạng thái sang delivered
                    scheduleOrderDelivery(order.id, userId)
                } else {
                    _error.value = "Không thể tạo đơn hàng: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Lỗi: ${e.message}"
                Log.e("OrderViewModel", "Error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }
    
    // Tạo thông báo khi đơn hàng được đặt thành công
    private fun createOrderNotification(userId: String, orderId: String, orderNumber: Int) {
        try {
            // Sử dụng NotificationService thay vì NotificationViewModel
            val notificationService = thai.phph48495.asm.notification.NotificationService.getInstance()
            val notification = thai.phph48495.asm.notification.Notify(
                userId = userId,
                title = "Đơn hàng #$orderNumber đã được đặt",
                message = "Đơn hàng của bạn đã được đặt thành công và đang được xử lý.",
                type = "order",
                relatedId = orderId
            )
            
            viewModelScope.launch {
                try {
                    notificationService.createNotification(notification)
                } catch (e: Exception) {
                    Log.e("OrderViewModel", "Error creating notification: ${e.message}")
                }
            }
        } catch (e: Exception) {
            Log.e("OrderViewModel", "Error preparing notification: ${e.message}")
        }
    }
    
    // Lên lịch chuyển trạng thái đơn hàng sang delivered sau 5 phút
    private fun scheduleOrderDelivery(orderId: String, userId: String) {
        viewModelScope.launch { 
            // Đợi 30 giây
            delay(30000)
            
            try {
                // Lấy thông tin đơn hàng
                val response = orderService.getOrderById(orderId)
                if (response.isSuccessful) {
                    val order = response.body()
                    if (order != null && order.status == "processing") {
                        // Cập nhật trạng thái đơn hàng sang delivered
                        val updatedOrder = order.copy(status = "delivered")
                        orderService.updateOrder(orderId, updatedOrder)
                        
                        // Cập nhật danh sách đơn hàng sau khi thay đổi trạng thái
                        getUserOrders(userId)
                    }
                }
            } catch (e: Exception) {
                Log.e("OrderViewModel", "Error updating order status: ${e.message}")
            }
        }
    }
    
    // Reset trạng thái lỗi
    fun resetError() {
        _error.value = null
    }
    
    // Reset trạng thái tạo đơn hàng
    fun resetOrderCreated() {
        _orderCreated.value = false
    }
} 