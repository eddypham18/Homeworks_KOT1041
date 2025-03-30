package thai.phph48495.asm.profile

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import thai.phph48495.asm.order.checkout.DialogConfirm
import thai.phph48495.asm.R
import thai.phph48495.asm.address.Address
import thai.phph48495.asm.paymentMethod.PaymentMethod


@Composable
fun ProfileScreen(navController: NavController) {
    // Fake dữ liệu người dùng để hiển thị giao diện
    val fakeUser = User(
        uid = "fake123",
        name = "Fake User",
        email = "fakeuser@example.com",
        phone = "0123456789",
        addresses = listOf(
            Address(address = "123 Fake Street, Fake City", isDefault = true)
        ),
        paymentMethods = listOf(
            PaymentMethod(idMethod = "pm1", cardNumber = "**** **** **** 1234", cvv = "123", expirationDate = "12/25", isDefault = true)
        )
    )
    // Sử dụng state cho user, có thể cập nhật nếu cần (ở đây chỉ hiển thị)
    val userState = remember { mutableStateOf(fakeUser) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            HeaderProfile(modifier = Modifier, text = "Profile", onLogout = {
                showDialog = true
            })
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            ItemMyProfile(user = userState.value)
            Spacer(modifier = Modifier.height(12.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                CardOptionProfile(optionText = "My Orders", onClick = { navController.navigate("order") })
                CardOptionProfile(optionText = "Shipping Address", onClick = { navController.navigate("shippingAddress") })
                CardOptionProfile(optionText = "Payment Method", onClick = { navController.navigate("paymentMethod") })
                CardOptionProfile(optionText = "My Reviews", onClick = { navController.navigate("myreviews") })
                CardOptionProfile(optionText = "Setting", onClick = { navController.navigate("setting") })
            }
        }
    }
    DialogConfirm(
        showDialog = showDialog,
        title = "Thông Báo",
        text = "Bạn có chắc muốn đăng xuất?",
        onDismiss = { showDialog = false },
        onConfirm = { logout(navController) }
    )
}

@Composable
fun ItemMyProfile(user: User) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(color = Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.LightGray)
                .size(100.dp)
        ) {
            AsyncImage(
                model = "https://chiemtaimobile.vn/images/companies/1/%E1%BA%A2nh%20Blog/avatar-facebook-dep/Anh-avatar-hoat-hinh-de-thuong-xinh-xan.jpg?1704788263223",
                contentDescription = "img profile",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier.padding(start = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                text = user.email,
                style = MaterialTheme.typography.titleMedium.copy(color = Color.Gray)
            )
        }
    }
}

@Composable
fun CardOptionProfile(optionText: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = optionText,
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                style = MaterialTheme.typography.titleMedium
            )
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = "Option",
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun HeaderProfile(modifier: Modifier, text: String, onLogout: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Icon(
            Icons.Default.Search,
            contentDescription = "",
            modifier = Modifier
                .size(40.dp)
                .padding(start = 10.dp)
                .align(Alignment.CenterStart)
                .clickable { /* Không xử lý logic tìm kiếm */ }
        )
        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = text,
                fontSize = 26.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.img_logout),
            contentDescription = "log out",
            modifier = Modifier
                .size(40.dp)
                .padding(end = 10.dp)
                .align(Alignment.CenterEnd)
                .clickable { onLogout() }
        )
    }
}

fun logout(navController: NavController) {
    navController.navigate("login")
}
