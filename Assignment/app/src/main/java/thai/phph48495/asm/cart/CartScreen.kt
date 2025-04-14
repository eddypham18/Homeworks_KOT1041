package thai.phph48495.asm.cart

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import thai.phph48495.asm.ProgressDialog
import thai.phph48495.asm.activity.ButtonSplash
import thai.phph48495.asm.activity.HeaderWithBack
import thai.phph48495.asm.activity.UserSession
import thai.phph48495.asm.product.navigateToProductDetail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController) {
    val context = LocalContext.current
    val cartViewModel: CartViewModel = viewModel()
    val userId = UserSession.getUserId(context) ?: return

    // Lấy dữ liệu từ ViewModel
    val cartItems = cartViewModel.cartItems.value
    val totalCartPrice = cartViewModel.totalCart.value
    val loading by cartViewModel.loading.collectAsState()
    val error by cartViewModel.error.collectAsState()

    // Lấy giỏ hàng khi màn hình được khởi tạo
    LaunchedEffect(userId) {
        cartViewModel.getCartsByUserId(userId)
    }

    Scaffold(
        topBar = {
            HeaderWithBack(modifier = Modifier, text = "Giỏ hàng", navController = navController)
        },
        content = { paddingValues ->
            Box(modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
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
                            cartViewModel.resetError()
                        }
                    }
                }

                // Hiển thị loading
                if (loading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                // Hiển thị giỏ hàng nếu có dữ liệu
                else if (!cartItems.isNullOrEmpty()) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(7.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(cartItems) { cart ->
                            ItemCart(
                                cart = cart,
                                onQuantityUpdate = { newQuantity ->
                                    if (newQuantity > 0) {
                                        cartViewModel.updateCartQuantity(cart.id, userId, newQuantity)
                                    }
                                },
                                onDeleteCart = {
                                    cartViewModel.deleteCart(cart.id, userId)
                                },
                                navController = navController
                            )
                        }
                    }
                }
                // Hiển thị thông báo nếu giỏ hàng trống
                else if (!loading && error == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Giỏ hàng trống", fontSize = 18.sp)
                    }
                }
            }
        },
        bottomBar = {
            if (!cartItems.isNullOrEmpty()) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Tổng tiền:",
                            fontSize = 18.sp,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                        )
                        Text(
                            text = "$ $totalCartPrice",
                            fontSize = 18.sp,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            color = Color.Red
                        )
                    }
                    ButtonSplash(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .padding(8.dp),
                        text = "Thanh toán",
                        onclick = {
                            // Điều hướng đến màn hình thanh toán
                            navController.navigate("checkout_screen/$totalCartPrice")
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun ItemCart(
    cart: Cart,
    onQuantityUpdate: (newQuantity: Int) -> Unit,
    onDeleteCart: () -> Unit,
    navController: NavController,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navigateToProductDetail(cart.productId, navController)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(7.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray)
                        .width(100.dp)
                        .height(100.dp)
                ) {
                    AsyncImage(
                        model = cart.imageProduct,
                        contentDescription = "img product",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Column {
                        Text(
                            text = cart.nameProduct,
                            style = MaterialTheme.typography.titleMedium.copy(color = Color.Gray)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$ ${cart.totalCartItem}",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    QuantityCart(
                        modifier = Modifier,
                        quantity = cart.quantity,
                        onMinus = {
                            if (cart.quantity > 1) {
                                onQuantityUpdate(cart.quantity - 1)
                            }
                        },
                        onPlus = { onQuantityUpdate(cart.quantity + 1) }
                    )
                }
            }
            Icon(
                Icons.Default.Close,
                contentDescription = "Delete",
                modifier = Modifier.clickable {
                    onDeleteCart()
                }
            )
        }
        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun QuantityCart(
    modifier: Modifier,
    quantity: Int,
    onMinus: () -> Unit,
    onPlus: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Button(
            onClick = onMinus,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0x9ED8D8D8),
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(4.dp),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(24.dp)
        ) {
            Text("-", fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = "$quantity", fontSize = 16.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Button(
            onClick = onPlus,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0x9ED8D8D8),
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(4.dp),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(24.dp)
        ) {
            Text("+", fontSize = 16.sp)
        }
    }
}

@Preview
@Composable
fun CartScreenPreview() {
    CartScreen(navController = NavController(LocalContext.current))
}