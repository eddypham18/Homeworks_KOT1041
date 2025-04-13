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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import thai.phph48495.asm.activity.UserSession
import thai.phph48495.asm.order.checkout.DialogConfirm
import thai.phph48495.asm.R

@Composable
fun ProfileScreen(navController: NavController) {
    // Sử dụng ViewModel
    val profileViewModel: ProfileViewModel = viewModel()
    val context = LocalContext.current
    
    // Lấy thông tin người dùng đã đăng nhập từ UserSession
    val userSession = UserSession.getUser(context)
    
    // Theo dõi trạng thái từ ViewModel
    val user = profileViewModel.user.value
    val loading by profileViewModel.loading.collectAsState()
    val error by profileViewModel.error.collectAsState()
    
    // Trạng thái hiển thị dialog xác nhận đăng xuất
    var showDialog by remember { mutableStateOf(false) }

    // Lấy thông tin người dùng khi vào màn hình
    LaunchedEffect(userSession?.id) {
        if (userSession?.id != null) {
            profileViewModel.getUserProfile(userSession.id)
        }
    }

    // Hiển thị thông báo lỗi nếu có
    LaunchedEffect(error) {
        if (error != null) {
            // Hiển thị thông báo lỗi
            // Và reset lỗi sau một khoảng thời gian
            kotlinx.coroutines.delay(3000)
            profileViewModel.resetError()
        }
    }

    Scaffold(
        topBar = {
            HeaderProfile(modifier = Modifier, text = "Profile", onLogout = {
                showDialog = true
            })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Hiển thị loading
            if (loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF1E3A8A))
                }
            } 
            // Hiển thị nội dung chính
            else {
                Column(modifier = Modifier.padding(paddingValues)) {
                    // Hiển thị thông tin người dùng từ API hoặc session
                    ItemMyProfile(user = user ?: userSession)
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
            
            // Hiển thị lỗi
            if (error != null) {
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Text(text = error ?: "Đã xảy ra lỗi")
                }
            }
        }
    }
    
    // Dialog xác nhận đăng xuất
    DialogConfirm(
        showDialog = showDialog,
        title = "Thông Báo",
        text = "Bạn có chắc muốn đăng xuất?",
        onDismiss = { showDialog = false },
        onConfirm = { 
            UserSession.logout(context)
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
        }
    )
}

@Composable
fun ItemMyProfile(user: User?) {
    if (user == null) {
        // Hiển thị trạng thái trống nếu không có dữ liệu
        EmptyStateView(
            icon = R.drawable.img_logout,
            title = "Bạn chưa đăng nhập",
            message = "Vui lòng đăng nhập để xem thông tin cá nhân",
            buttonText = "Đăng nhập",
            onClick = {
                // Xử lý khi người dùng bấm nút đăng nhập
            }
        )
        return
    }
    
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
                model = "https://i.pinimg.com/736x/8f/1c/a2/8f1ca2029e2efceebd22fa05cca423d7.jpg",
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
            if (user.phone?.isNotEmpty() == true) {
                Text(
                    text = user.phone,
                    style = MaterialTheme.typography.titleMedium.copy(color = Color.Gray)
                )
            }
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

@Composable
fun EmptyStateView(
    icon: Int,
    title: String,
    message: String,
    buttonText: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 24.dp)
        )
        
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 24.dp),
            textAlign = TextAlign.Center
        )
        
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1E3A8A)
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = buttonText)
        }
    }
}
