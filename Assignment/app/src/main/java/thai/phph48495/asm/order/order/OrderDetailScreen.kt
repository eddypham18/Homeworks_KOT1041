package thai.phph48495.asm.order.order

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import thai.phph48495.asm.activity.HeaderWithBack


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(orderId: String, navController: NavController) {
    // Sử dụng ViewModel
    val orderViewModel: OrderViewModel = viewModel()
    
    // Trạng thái từ ViewModel
    val orderDetail = orderViewModel.orderDetail.value
    val loading by orderViewModel.loading.collectAsState()
    val error by orderViewModel.error.collectAsState()
    
    // Trạng thái đã thử lấy dữ liệu
    var hasTriedLoading by remember { mutableStateOf(false) }
    var retryCount by remember { mutableStateOf(0) }
    val maxRetries = 3
    
    // Log cho debugging
    LaunchedEffect(orderDetail) {
        Log.d("OrderDetailScreen", "Order detail: $orderDetail")
    }
    
    // Lấy chi tiết đơn hàng khi màn hình được mở
    LaunchedEffect(orderId, retryCount) {
        if (orderId.isNotEmpty() && retryCount < maxRetries) {
            Log.d("OrderDetailScreen", "Loading order details for ID: $orderId, retry: $retryCount")
            orderViewModel.getOrderDetail(orderId)
            hasTriedLoading = true
            
            // Nếu không có kết quả và chưa vượt quá số lần thử, thử lại sau 1s
            if (orderDetail == null && !loading) {
                delay(1000)
                retryCount++
            }
        }
    }

    Scaffold(
        topBar = {
            HeaderWithBack(modifier = Modifier, text = "Order Detail", navController = navController)
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()) {
            // Hiển thị loading
            if (loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF1E3A8A))
                }
            } 
            // Hiển thị chi tiết đơn hàng
            else if (orderDetail != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Đơn hàng #${orderDetail.OrderID}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            OrderInfoRow(label = "Ngày đặt: ", value = orderDetail.date)
                            OrderInfoRow(label = "Tên khách hàng: ", value = orderDetail.nameUser)
                            OrderInfoRow(label = "Địa chỉ: ", value = orderDetail.addressUser)
                            OrderInfoRow(label = "Phương thức thanh toán: ", value = orderDetail.paymentUser)
                            OrderInfoRow(label = "Tổng số lượng: ", value = "${orderDetail.totalQuantity}")
                            OrderInfoRow(label = "Tổng tiền: ", value = "$${orderDetail.totalMoney}")
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = "Sản phẩm:",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            
                            Column(
                                modifier = Modifier.padding(start = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                orderDetail.items.forEach { item ->
                                    Text(
                                        text = "• ${item.nameProduct} - SL: ${item.quantity}",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Trạng thái: ",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                
                                // Hiển thị trạng thái đơn hàng
                                val statusText = when(orderDetail.status) {
                                    "processing" -> "Đang xử lý"
                                    "delivered" -> "Đã giao"
                                    "canceled" -> "Đã hủy"
                                    else -> "Đang xử lý"
                                }
                                
                                val statusColor = when(orderDetail.status) {
                                    "processing" -> Color.Blue
                                    "delivered" -> Color.Green
                                    "canceled" -> Color.Red
                                    else -> Color.Blue
                                }
                                
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = statusColor.copy(alpha = 0.1f)
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Text(
                                        text = statusText,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = statusColor,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
            // Hiển thị thông báo khi không tìm thấy đơn hàng
            else if (hasTriedLoading && !loading && retryCount >= maxRetries) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Không tìm thấy thông tin đơn hàng",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Mã đơn hàng: $orderId",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                    }
                }
            }
            
            // Hiển thị lỗi
            if (error != null) {
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Text(text = error ?: "Đã xảy ra lỗi")
                }
                
                // Tự động ẩn thông báo lỗi sau 3 giây
                LaunchedEffect(error) {
                    delay(3000)
                    orderViewModel.resetError()
                }
            }
        }
    }
}

@Composable
fun OrderInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier.width(150.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
