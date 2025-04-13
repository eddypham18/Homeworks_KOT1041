package thai.phph48495.asm.order.order

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import thai.phph48495.asm.activity.ButtonSplash
import thai.phph48495.asm.activity.HeaderWithBack
import thai.phph48495.asm.activity.UserSession


@Composable
fun OrderScreen(navController: NavController) {
    // Sử dụng ViewModel
    val orderViewModel: OrderViewModel = viewModel()
    val context = LocalContext.current
    
    // Lấy thông tin người dùng
    val userSession = UserSession.getUser(context)
    val userId = userSession?.id ?: ""
    
    // Trạng thái từ ViewModel
    val orders = orderViewModel.orders.value
    val filteredOrders = orderViewModel.filteredOrders.value
    val loading by orderViewModel.loading.collectAsState()
    val error by orderViewModel.error.collectAsState()
    
    // Ánh xạ danh mục sang trạng thái đơn hàng
    val statusMapping = listOf("delivered", "processing", "canceled")
    
    // Biến lưu trạng thái category đang chọn
    var selectedIndex by remember { mutableStateOf(0) }
    
    // Lấy danh sách đơn hàng khi màn hình được mở
    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            orderViewModel.getUserOrders(userId)
        }
    }
    
    // Lọc đơn hàng khi chọn category
    LaunchedEffect(selectedIndex, orders) {
        val status = statusMapping[selectedIndex]
        orderViewModel.filterOrdersByStatus(status)
    }

    Scaffold(
        topBar = {
            // Ta bọc HeaderWithBack và thanh category trong 1 Column
            Column {
                HeaderWithBack(modifier = Modifier, text = "My order", navController = navController)
                // Thanh category
                OrderCategoryBar(
                    categories = listOf("Delivered", "Processing", "Canceled"),
                    selectedIndex = selectedIndex,
                    onCategorySelected = { index ->
                        selectedIndex = index
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            // Hiển thị loading
            if (loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF1E3A8A))
                }
            } 
            // Hiển thị danh sách rỗng
            else if (filteredOrders.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Không có đơn hàng ${getStatusText(selectedIndex).toLowerCase()}",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            } 
            // Hiển thị danh sách đơn hàng đã lọc
            else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredOrders) { order ->
                        ItemOrder(order = order, navController = navController)
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
                    kotlinx.coroutines.delay(3000)
                    orderViewModel.resetError()
                }
            }
        }
    }
}

@Composable
fun ItemOrder(order: Order, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Order No ${order.OrderID}",
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = order.date,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Gray,
                )
            }
            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Text(
                        text = "Quantity: ",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Gray
                    )
                    Text(
                        text = "${order.totalQuantity}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Row {
                    Text(
                        text = "Total: ",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Gray
                    )
                    Text(
                        text = "$ ${order.totalMoney}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ButtonSplash(
                    modifier = Modifier
                        .padding(12.dp)
                        .width(100.dp)
                        .height(40.dp),
                    text = "Detail"
                ) {
                    navController.navigate("orderDetail/${order.id}")
                }
                
                // Hiển thị trạng thái đơn hàng
                val statusText = when(order.status) {
                    "processing" -> "Đang xử lý"
                    "delivered" -> "Đã giao"
                    "canceled" -> "Đã hủy"
                    else -> "Đang xử lý"
                }
                
                val statusColor = when(order.status) {
                    "processing" -> Color.Blue
                    "delivered" -> Color.Green
                    "canceled" -> Color.Red
                    else -> Color.Blue
                }
                
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.titleLarge,
                    color = statusColor
                )
            }
        }
    }
}

@Composable
fun OrderCategoryBar(
    categories: List<String>,
    selectedIndex: Int,
    onCategorySelected: (Int) -> Unit
) {
    // Dùng Row để đặt các tab cạnh nhau
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        categories.forEachIndexed { index, category ->
            Column(
                modifier = Modifier
                    .clickable { onCategorySelected(index) }
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Text hiển thị tên category
                Text(
                    text = category,
                    fontWeight = if (selectedIndex == index) FontWeight.Bold else FontWeight.Normal,
                    color = if (selectedIndex == index) Color.Black else Color.Gray
                )
                // Nếu là tab đang chọn thì vẽ 1 gạch dưới
                if (selectedIndex == index) {
                    Spacer(
                        modifier = Modifier
                            .height(2.dp)
                            .width(24.dp)
                            .background(Color.Black)
                    )
                }
            }
        }
    }
}

// Hàm lấy text trạng thái dựa trên tab đang chọn
fun getStatusText(selectedIndex: Int): String {
    return when (selectedIndex) {
        0 -> "Đã giao"
        1 -> "Đang xử lý"
        2 -> "Đã hủy"
        else -> "Đã giao"
    }
}

// Hàm lấy màu trạng thái dựa trên tab đang chọn
fun getStatusColor(selectedIndex: Int): Color {
    return when (selectedIndex) {
        0 -> Color.Green
        1 -> Color.Blue
        2 -> Color.Red
        else -> Color.Green
    }
}
