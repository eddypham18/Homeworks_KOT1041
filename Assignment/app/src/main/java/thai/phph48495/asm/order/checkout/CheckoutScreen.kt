package thai.phph48495.asm.order.checkout

import android.util.Log
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import thai.phph48495.asm.activity.ButtonSplash
import thai.phph48495.asm.activity.HeaderWithBack
import thai.phph48495.asm.activity.UserSession
import thai.phph48495.asm.address.Address
import thai.phph48495.asm.address.AddressViewModel
import thai.phph48495.asm.cart.CartService
import thai.phph48495.asm.cart.Cart
import thai.phph48495.asm.cart.CartViewModel
import thai.phph48495.asm.order.order.OrderItem
import thai.phph48495.asm.order.order.OrderViewModel
import thai.phph48495.asm.paymentMethod.PaymentMethod
import thai.phph48495.asm.paymentMethod.PaymentViewModel
import thai.phph48495.asm.profile.User
import thai.phph48495.asm.R
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(navController: NavController, totalMoney: Double) {
    // Khởi tạo ViewModel
    val paymentViewModel: PaymentViewModel = viewModel()
    val addressViewModel: AddressViewModel = viewModel()
    val orderViewModel: OrderViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()
    
    // Lấy context và user ID
    val context = LocalContext.current
    val userSession = UserSession.getUser(context)
    val userId = userSession?.id ?: ""
    
    // Trạng thái từ PaymentViewModel
    val paymentMethods = paymentViewModel.paymentMethods.value
    val defaultPaymentMethod = paymentMethods.find { it.isDefault }
    val paymentLoading by paymentViewModel.loading.collectAsState()
    val paymentError by paymentViewModel.error.collectAsState()
    
    // Trạng thái từ AddressViewModel
    val user by addressViewModel.currentUser.observeAsState()
    val addressLoading by addressViewModel.isLoading.observeAsState(false)
    val addressError by addressViewModel.error.observeAsState()
    
    // Trạng thái từ OrderViewModel
    val orderLoading by orderViewModel.loading.collectAsState()
    val orderError by orderViewModel.error.collectAsState()
    val orderCreated by orderViewModel.orderCreated.collectAsState()
    
    // Trạng thái từ CartViewModel
    val cartItemsState = cartViewModel.cartItems.value
    
    // Lấy địa chỉ mặc định từ danh sách địa chỉ của người dùng
    val defaultAddress = user?.addresses?.find { it.isDefault }
    
    // Tổng tiền
    val totalAmount = totalMoney
    
    // Trạng thái dialog
    var showDialog by remember { mutableStateOf(false) }
    
    // Hiển thị thông báo lỗi
    var errorMessage by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(paymentError, addressError, orderError) {
        errorMessage = orderError ?: paymentError ?: addressError
    }
    
    // Lấy dữ liệu khi màn hình được mở
    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            paymentViewModel.getUserPaymentMethods(userId)
            addressViewModel.getUserAddresses(userId)
            
            // Lấy thông tin giỏ hàng
            cartViewModel.getCartsByUserId(userId)
        }
    }
    
    // Thêm effect để làm mới dữ liệu địa chỉ và phương thức thanh toán khi màn hình được focus
    LaunchedEffect(Unit) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            if (backStackEntry.destination.route?.contains("checkout") == true && userId.isNotEmpty()) {
                Log.d("CheckoutScreen", "Refreshing data when returning to checkout screen")
                // Làm mới dữ liệu địa chỉ và phương thức thanh toán khi quay lại màn hình checkout
                addressViewModel.getUserAddresses(userId)
                paymentViewModel.getUserPaymentMethods(userId)
            }
        }
    }
    
    // Xử lý khi đơn hàng được tạo thành công
    LaunchedEffect(orderCreated) {
        if (orderCreated) {
            // Lưu lại danh sách giỏ hàng cần xóa trước khi thực hiện xóa
            val cartItemsToDelete = cartItemsState?.toList() ?: emptyList()
            
            // Xóa giỏ hàng sau khi đặt hàng thành công
            coroutineScope.launch {
                try {
                    // Xóa từng sản phẩm trong giỏ hàng
                    cartItemsToDelete.forEach { cartItem ->
                        cartViewModel.deleteCart(cartItem.id, userId)
                    }
                    
                    // Làm mới giỏ hàng sau khi xóa
                    cartViewModel.getCartsByUserId(userId)
                } catch (e: Exception) {
                    // Lỗi xóa giỏ hàng không ảnh hưởng đến quy trình đặt hàng
                    Log.e("CheckoutScreen", "Error deleting cart items: ${e.message}")
                }
            }
            
            // Chuyển hướng đến màn hình thành công
            navController.navigate("submitSuccess") {
                popUpTo("checkout") { inclusive = true }
            }
            
            // Reset trạng thái
            orderViewModel.resetOrderCreated()
        }
    }

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
                    // Chỉ cho phép gửi đơn hàng nếu có địa chỉ và phương thức thanh toán
                    if (defaultAddress != null && defaultPaymentMethod != null) {
                        showDialog = true
                    } else {
                        // Hiển thị thông báo lỗi cụ thể
                        errorMessage = if (defaultAddress == null) 
                            "Vui lòng chọn địa chỉ giao hàng mặc định" 
                        else 
                            "Vui lòng chọn phương thức thanh toán mặc định"
                    }
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                // Hiển thị loading
                if (paymentLoading || addressLoading || orderLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF1E3A8A))
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        // Sử dụng dữ liệu thực từ API
                        ShippingAddressCheckout(
                            defaultAddress = defaultAddress,
                            user = userSession,
                            navController = navController
                        )
                        PaymentMethodCheckout(
                            defaultPayment = defaultPaymentMethod,
                            navController = navController
                        )
                        TotalCheckout(totalAmount = totalAmount)
                    }
                    
                    // Hiển thị thông báo lỗi
                    if (errorMessage != null) {
                        Snackbar(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 80.dp, start = 16.dp, end = 16.dp)
                        ) {
                            Text(text = errorMessage ?: "Đã xảy ra lỗi")
                        }
                        
                        // Tự động ẩn thông báo lỗi sau 3 giây
                        LaunchedEffect(errorMessage) {
                            kotlinx.coroutines.delay(3000)
                            errorMessage = null
                            paymentViewModel.resetError()
                            addressViewModel.resetOperationStatus()
                            orderViewModel.resetError()
                        }
                    }
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
            // Tạo đơn hàng mới
            if (userId.isNotEmpty() && defaultAddress != null && defaultPaymentMethod != null && cartItemsState != null && cartItemsState.isNotEmpty()) {
                // Tổng số lượng sản phẩm
                val totalQuantity = cartItemsState.sumOf { it.quantity }
                
                // Tạo danh sách sản phẩm trong đơn hàng
                val orderItems = cartItemsState.map { cartItem ->
                    OrderItem(
                        nameProduct = cartItem.nameProduct,
                        quantity = cartItem.quantity.toString()
                    )
                }
                
                // Gọi API tạo đơn hàng
                orderViewModel.createOrder(
                    userId = userId,
                    totalMoney = totalAmount + 5.00, // Tổng tiền + phí vận chuyển
                    totalQuantity = totalQuantity,
                    nameUser = userSession?.name ?: "",
                    addressUser = defaultAddress.address,
                    paymentUser = defaultPaymentMethod.cardNumber,
                    items = orderItems,
                    onSuccess = {
                        // Đóng dialog xác nhận
                        showDialog = false
                    }
                )
            } else {
                errorMessage = "Thiếu thông tin để tạo đơn hàng"
                showDialog = false
            }
        }
    )
}

@Composable
fun ShippingAddressCheckout(defaultAddress: Address?, user: User?, navController: NavController) {
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
                    text = user?.name ?: "Khách hàng",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(12.dp)
                )
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Hiển thị địa chỉ đầy đủ
                val addressText = if (defaultAddress != null) {
                    buildString {
                        append(defaultAddress.address)
                        if (defaultAddress.district.isNotEmpty()) append(", ${defaultAddress.district}")
                        if (defaultAddress.city.isNotEmpty()) append(", ${defaultAddress.city}")
                        if (defaultAddress.country.isNotEmpty()) append(", ${defaultAddress.country}")
                    }
                } else {
                    "Bạn chưa chọn địa chỉ mặc định!"
                }
                
                Text(
                    text = addressText,
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
                
                // Hiển thị thông tin thanh toán chi tiết hơn nếu có
                if (defaultPayment != null) {
                    val cardNumberDisplay = defaultPayment.cardNumber
                    val expiryDate = defaultPayment.expirationDate
                    
                    Column {
                        Text(
                            text = cardNumberDisplay,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Expires: $expiryDate",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                } else {
                    Text(
                        text = "Bạn chưa chọn phương thức thanh toán mặc định!",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
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
