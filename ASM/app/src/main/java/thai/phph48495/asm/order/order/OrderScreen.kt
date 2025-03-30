package thai.phph48495.asm.order.order

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import thai.phph48495.asm.activity.ButtonSplash
import thai.phph48495.asm.activity.HeaderWithBack


@Composable
fun OrderScreen(navController: NavController) {
    val orders = remember {
        listOf(
            Order(
                _id = "order1",
                OrderID = 1001,
                userId = "user1",
                cartId = "cart1",
                totalMoney = 150.0,
                totalQuantity = 3,
                nameUser = "Nguyễn Văn A",
                addressUser = "123 ABC Street",
                paymentUser = "**** **** **** 1234",
                date = "2025-03-30",
                items = listOf(
                    OrderItem(nameProduct = "Sản phẩm 1", quantity = "1"),
                    OrderItem(nameProduct = "Sản phẩm 2", quantity = "1"),
                    OrderItem(nameProduct = "Sản phẩm 3", quantity = "1")
                )
            ),
            Order(
                _id = "order2",
                OrderID = 1002,
                userId = "user2",
                cartId = "cart2",
                totalMoney = 200.0,
                totalQuantity = 4,
                nameUser = "Trần Thị B",
                addressUser = "456 DEF Street",
                paymentUser = "**** **** **** 5678",
                date = "2025-04-01",
                items = listOf(
                    OrderItem(nameProduct = "Sản phẩm 4", quantity = "2"),
                    OrderItem(nameProduct = "Sản phẩm 5", quantity = "2")
                )
            )
        )
    }

    // Biến lưu trạng thái category đang chọn
    var selectedIndex by remember { mutableStateOf(0) }

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
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(orders) { order ->
                ItemOrder(order = order, navController = navController)
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
                    text = "",
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
                        text = "${order.totalMoney}",
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
                    navController.navigate("orderDetail/${order._id}")
                }
                Text(
                    text = "Đã giao",
                    style = MaterialTheme.typography.titleLarge
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
