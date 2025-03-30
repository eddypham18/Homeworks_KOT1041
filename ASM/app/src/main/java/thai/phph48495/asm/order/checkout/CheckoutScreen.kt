package thai.phph48495.asm.order.checkout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import thai.phph48495.asm.activity.ButtonSplash
import thai.phph48495.asm.activity.HeaderWithBack
import thai.phph48495.asm.address.Address
import thai.phph48495.asm.paymentMethod.PaymentMethod
import thai.phph48495.asm.profile.User
import thai.phph48495.asm.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(navController: NavController, totalMoney: Double) {
    // Tạo dữ liệu giả
    val fakeAddress = Address(
        address = "123 Fake Street, Fake City",
        isDefault = true
    )
    val fakePaymentMethod = PaymentMethod(
        cardNumber = "**** **** **** 1234",
        isDefault = true
    )
    val fakeUser = User(
        uid = "user1",
        name = "John Doe",
        addresses = listOf(fakeAddress),
        paymentMethods = listOf(fakePaymentMethod)
    )
    // Với dữ liệu giả, ta sử dụng totalMoney truyền vào
    val totalAmount = totalMoney

    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.background(color = Color.Transparent).padding(8.dp)) {
        Scaffold(
            topBar = {
                HeaderWithBack(modifier = Modifier, text = "Check Out", navController = navController)
            },
            bottomBar = {
                ButtonSplash(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                        .height(60.dp),
                    text = "SUBMIT ORDER"
                ) {
                    showDialog = true
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    // Sử dụng dữ liệu giả
                    ShippingAddressCheckout(
                        defaultAddress = fakeUser.addresses.find { it.isDefault },
                        user = fakeUser,
                        navController = navController
                    )
                    PaymentMethodCheckout(
                        defaultPayment = fakeUser.paymentMethods.find { it.isDefault },
                        navController = navController
                    )
                    TotalCheckout(totalAmount = totalAmount)
                }
            }
        }
    }
    DialogConfirm(
        showDialog = showDialog,
        title = "Thông báo",
        text = "Bạn có chắc muốn xác nhận đơn hàng không?",
        onDismiss = { showDialog = false },
        onConfirm = {
            navController.navigate("submitSuccess")
        }
    )
}

@Composable
fun ShippingAddressCheckout(defaultAddress: Address?, user: User, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        TitleItemCheckout(
            text = "Shopping Address",
            onIconClick = {
                navController.navigate("shippingAddress") {
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(12.dp)
                )
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = defaultAddress?.address ?: "You have not selected a default address!",
                    modifier = Modifier.padding(12.dp),
                    fontSize = 15.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun PaymentMethodCheckout(defaultPayment: PaymentMethod?, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        TitleItemCheckout(
            text = "Payment",
            onIconClick = {
                navController.navigate("paymentMethod")
            }
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.mastercard2),
                    contentDescription = "Image method",
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .size(45.dp)
                )
                Text(
                    text = defaultPayment?.cardNumber
                        ?: "You have not selected a default payment method!",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun TotalCheckout(totalAmount: Double) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Order",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = "$ $totalAmount",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Delivery",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = "$ 5.00",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total:",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = "$ ${totalAmount + 5.00}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun TitleItemCheckout(text: String, onIconClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray
        )
        Icon(
            Icons.Default.Edit,
            contentDescription = "Edit shipping address",
            modifier = Modifier.clickable { onIconClick() }
        )
    }
}

@Composable
fun DialogConfirm(
    showDialog: Boolean,
    title: String,
    text: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(text = title) },
            text = { Text(text = text) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    }
                ) {
                    Text("Xác nhận")
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text("Hủy")
                }
            }
        )
    }
}
