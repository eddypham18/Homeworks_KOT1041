package thai.phph48495.asm.cart

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
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import thai.phph48495.asm.ProgressDialog
import thai.phph48495.asm.activity.ButtonSplash
import thai.phph48495.asm.activity.HeaderWithBack
import thai.phph48495.asm.product.navigateToProductDetail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController) {
    // Tạo dữ liệu giả
    val cartProducts = listOf(
        Cart(
            _id = "2",
            userId = "user1",
            productId = "p2",
            nameProduct = "Black Simple Lamp",
            imageProduct = "https://s3-alpha-sig.figma.com/img/2443/fe11/03a0919f36f923a162b57615bd507c3e?Expires=1743984000&Key-Pair-Id=APKAQ4GOSFWCW27IBOMQ&Signature=fdFTG78faNNUcHvuxZlX2D6Z195~UYWa6e0cNS6731JACHwE0p8Slh3MCpAis1Nyjw05KRhHvXDkZnXBoMYjc10noFvbjVaLF~PYUMKFVLHonocWIAf42mg7Y~J2oLDNmcgJfg9NuM6VVZOpDb42-f8THzgZ2F6FRsfbcUE~p3VT4BqnOFYmeHZS4pSQUbjfEpESKK6k1BPFWwyQ~hvMA0QnBPHSubedFIWLuu3e74quQcbTXPI0wP5l6yBO3p1yn-wPJAwkAenbJ4zoJeoSAB4f4WBKbeHXHJ1WhU4W4fAHSYar4bC1Pfkj405Sclo~UMPU4uWfOLPB1-uFrZwbHg__"
            ,
            priceProduct = 15.0,
            quantity = 2,
            totalCartItem = 30.0
        )
    )

    // Tính tổng giá trị giỏ hàng
    val totalCartPrice = cartProducts.sumOf { it.totalCartItem }

    // Đang không sử dụng loading
    val isLoading = false

    Scaffold(
        topBar = {
            HeaderWithBack(modifier = Modifier, text = "Cart", navController = navController)
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    items(cartProducts) { cart ->
                        // Không cần xử lý cập nhật số lượng nên truyền hàm rỗng
                        ItemCart(cart = cart, onQuantityUpdate = {}, navController = navController)
                    }
                }
                if (isLoading) {
                    ProgressDialog()
                }
            }
        },
        bottomBar = {
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
                    Text(text = "Total: ")
                    Text(text = "$ $totalCartPrice")
                }
                ButtonSplash(
                    modifier = Modifier
                        .height(60.dp)
                        .padding(8.dp),
                    text = "Check out",
                    onclick = {
                        navController.navigate("checkout_screen/${totalCartPrice}")
                    }
                )
            }
        }
    )
}

@Composable
fun ItemCart(
    cart: Cart,
    onQuantityUpdate: (newQuantity: Int) -> Unit,
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
                        .padding(bottom = 12.dp)
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
                    // Giao diện số lượng nhưng không thay đổi dữ liệu thật
                    QuantityCart(
                        modifier = Modifier,
                        quantity = cart.quantity,
                        onMinus = {},
                        onPlus = {}
                    )
                }
            }
            Icon(
                Icons.Default.Close,
                contentDescription = "",
                modifier = Modifier.clickable {
                    // Không có hành động xóa
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
    Row(verticalAlignment = Alignment.CenterVertically) {
        Button(
            onClick = onMinus,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0x9ED8D8D8),
                contentColor = Color.Gray
            ),
            shape = RoundedCornerShape(5.dp),
            contentPadding = PaddingValues(1.dp),
            modifier = Modifier.size(30.dp)
        ) {
            Text("-", fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = "$quantity", fontSize = 16.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Button(
            onClick = onPlus,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0x9ED8D8D8),
                contentColor = Color.Gray
            ),
            shape = RoundedCornerShape(5.dp),
            contentPadding = PaddingValues(1.dp),
            modifier = Modifier.size(30.dp)
        ) {
            Text("+", fontSize = 20.sp)
        }
    }
}

@Preview
@Composable
fun CartScreenPreview() {
    CartScreen(navController = NavController(LocalContext.current))
}
