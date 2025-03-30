package thai.phph48495.asm.order.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import thai.phph48495.asm.activity.HeaderWithBack


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(orderId: String, navController: NavController) {
    // Tạo dữ liệu giả cho đơn hàng với cấu trúc mới
    val fakeOrder = Order(
        _id = "order_fake_001",
        OrderID = 1001,
        userId = "user_01",
        cartId = "cart_01",
        totalMoney = 150.0,
        totalQuantity = 3,
        nameUser = "Nguyễn Văn A",
        addressUser = "123 Đường ABC, Quận 1, TP.HCM",
        paymentUser = "**** **** **** 1234",
        date = "2025-03-30",
        items = listOf(
            OrderItem(nameProduct = "Sản phẩm 1", quantity = "1"),
            OrderItem(nameProduct = "Sản phẩm 2", quantity = "1"),
            OrderItem(nameProduct = "Sản phẩm 3", quantity = "1")
        )
    )

    Scaffold(
        topBar = {
            HeaderWithBack(modifier = Modifier, text = "Order Detail", navController = navController)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            fakeOrder.let { order ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "Order no ${order.OrderID}")
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Date: ",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Gray
                                )
                                Text(text = order.date, style = MaterialTheme.typography.titleMedium)
                            }
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Name: ",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Gray
                                )
                                Text(text = order.nameUser, style = MaterialTheme.typography.titleMedium)
                            }
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Address: ",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Gray
                                )
                                Text(text = order.addressUser, style = MaterialTheme.typography.titleMedium)
                            }
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Payment: ",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Gray
                                )
                                Text(text = order.paymentUser, style = MaterialTheme.typography.titleMedium)
                            }
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Total Quantity: ",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "${order.totalQuantity}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Total Money: ",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "$ ${order.totalMoney}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Products: ",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Gray
                                )
                                val productsInfo = order.items.joinToString(separator = "\n") {
                                    "${it.nameProduct} - ${it.quantity}"
                                }
                                Text(
                                    text = productsInfo,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                            }
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Status: ",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "Delivering",
                                    style = MaterialTheme.typography.titleMedium.copy(color = Color.Green)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
