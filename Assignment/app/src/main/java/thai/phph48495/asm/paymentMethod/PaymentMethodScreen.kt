package thai.phph48495.asm.paymentMethod

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import thai.phph48495.asm.activity.HeaderWithBack
import thai.phph48495.asm.activity.UserSession
import thai.phph48495.asm.R
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodScreen(navController: NavController) {
    // Sử dụng ViewModel thực tế
    val paymentViewModel: PaymentViewModel = viewModel()
    val context = LocalContext.current
    
    // Lấy thông tin người dùng đã đăng nhập
    val user = UserSession.getUser(context)
    val userId = user?.id ?: ""
    val userName = user?.name ?: ""
    
    // Theo dõi trạng thái từ ViewModel
    val paymentMethodList = paymentViewModel.paymentMethods.value
    val loading by paymentViewModel.loading.collectAsState()
    val error by paymentViewModel.error.collectAsState()
    
    // Lấy danh sách phương thức thanh toán khi vào màn hình
    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            paymentViewModel.getUserPaymentMethods(userId)
        }
    }
    
    // Thêm LaunchedEffect để làm mới dữ liệu khi quay lại từ màn hình CheckoutScreen
    LaunchedEffect(Unit) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            if (backStackEntry.destination.route?.contains("paymentMethod") == true && userId.isNotEmpty()) {
                // Làm mới dữ liệu phương thức thanh toán khi quay lại màn hình này
                paymentViewModel.getUserPaymentMethods(userId)
            }
        }
    }
    
    Scaffold(
        topBar = {
            HeaderWithBack(modifier = Modifier, text = "Payment Methods", navController = navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("addPaymentMethod") },
                shape = CircleShape,
                containerColor = Color.Black,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add payment method")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F8F8))
        ) {
            // Hiển thị lỗi nếu có
            if (error != null) {
                Snackbar(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = error ?: "Đã xảy ra lỗi")
                }
                LaunchedEffect(error) {
                    if (error != null) {
                        paymentViewModel.resetError()
                    }
                }
            }
            
            // Hiển thị loading
            if (loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF1E3A8A))
                }
            } 
            // Hiển thị thông báo nếu người dùng chưa đăng nhập
            else if (userId.isEmpty()) {
                EmptyStateView(
                    icon = R.drawable.mastercard2,
                    title = "No Payment Methods",
                    message = "Please login to view your payment methods",
                    buttonText = "Login",
                    onClick = {
                        navController.navigate("login")
                    }
                )
            }
            // Hiển thị danh sách phương thức thanh toán nếu có
            else if (paymentMethodList.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(paymentMethodList) { method ->
                        CardPaymentMethod(
                            method = method,
                            nameUser = userName,
                            onDefaultChange = { isDefault -> 
                                // Gọi viewModel để cập nhật trạng thái mặc định
                                paymentViewModel.updateDefaultPaymentMethod(method, isDefault)
                            }
                        )
                    }
                }
            } 
            // Hiển thị thông báo nếu danh sách trống
            else if (!loading && error == null) {
                EmptyStateView(
                    icon = R.drawable.mastercard2,
                    title = "No Payment Methods",
                    message = "You don't have any payment methods yet. Add one to get started.",
                    buttonText = "Add Payment Method",
                    onClick = {
                        navController.navigate("addPaymentMethod")
                    }
                )
            }
        }
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
