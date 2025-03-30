package thai.phph48495.asm.product

import android.text.style.ClickableSpan
import androidx.activity.compose.LocalActivity
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage



@Composable
fun ProductDetailScreen(productId: String, navController: NavController) {
    // Fake dữ liệu sản phẩm mẫu
    val fakeProduct = Product(
        _id = productId,
        name = "Black Simple Lamp",
        price = 29.99,
        stars = 4.5,
        reviews = 120,
        description = "This is a sample description for a fake product. It provides details about features and usage.",
        image = "https://s3-alpha-sig.figma.com/img/2443/fe11/03a0919f36f923a162b57615bd507c3e?Expires=1743984000&Key-Pair-Id=APKAQ4GOSFWCW27IBOMQ&Signature=fdFTG78faNNUcHvuxZlX2D6Z195~UYWa6e0cNS6731JACHwE0p8Slh3MCpAis1Nyjw05KRhHvXDkZnXBoMYjc10noFvbjVaLF~PYUMKFVLHonocWIAf42mg7Y~J2oLDNmcgJfg9NuM6VVZOpDb42-f8THzgZ2F6FRsfbcUE~p3VT4BqnOFYmeHZS4pSQUbjfEpESKK6k1BPFWwyQ~hvMA0QnBPHSubedFIWLuu3e74quQcbTXPI0wP5l6yBO3p1yn-wPJAwkAenbJ4zoJeoSAB4f4WBKbeHXHJ1WhU4W4fAHSYar4bC1Pfkj405Sclo~UMPU4uWfOLPB1-uFrZwbHg__"
    )

    var productQuantity by remember { mutableStateOf(1) }
    var isFavorite by remember { mutableStateOf(false) }
    val userId = "fake_user"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp) // Khoảng cách cho footer
        ) {
            // Header hiển thị ảnh sản phẩm và nút Back, Color Picker
            Box(
                modifier = Modifier
                    .weight(1.2f)
                    .fillMaxWidth()
            ) {
                HeaderProductDetail(fakeProduct, navController)
            }
            // Nội dung chi tiết sản phẩm
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                BodyProductDetail(
                    product = fakeProduct,
                    quantity = productQuantity,
                    onMinus = { if (productQuantity > 1) productQuantity-- },
                    onPlus = { productQuantity++ },
                    navController = navController
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        // Footer với nút thêm vào giỏ hàng và thay đổi trạng thái yêu thích
        FooterProductDetail(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            productId = productId,
            userId = userId,
            isFavorite = isFavorite,
            quantity = productQuantity,
            navController = navController
        )
    }
}

@Composable
fun HeaderProductDetail(product: Product, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 12.dp)
        ) {
            // Hiển thị ảnh sản phẩm
            AsyncImage(
                model = product.image,
                contentDescription = "Product Image",
                modifier = Modifier
                    .padding(start = 20.dp)
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(bottomStart = 100.dp)),
                contentScale = ContentScale.FillBounds,
            )
        }
        // Nút Back
        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(13.dp),
            contentPadding = PaddingValues(12.dp),
            modifier = Modifier
                .padding(top = 10.dp, start = 15.dp)
                .size(40.dp)
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Back",
            )
        }
        // Color Picker giả (chỉ hiển thị giao diện)
        Button(
            onClick = {  },
            modifier = Modifier.align(Alignment.CenterStart),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            elevation = ButtonDefaults.buttonElevation(12.dp),
        ) {
            Column {
                listOf(Color.Gray, Color.Yellow, Color.Blue).forEach { color ->
                    Box(
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                            .size(20.dp)
                            .background(color, shape = CircleShape)
                            .clickable {  }
                    )
                }
            }
        }
    }
}

@Composable
fun BodyProductDetail(
    product: Product,
    quantity: Int,
    onMinus: () -> Unit,
    onPlus: () -> Unit,
    navController: NavController
) {
    Spacer(modifier = Modifier.height(15.dp))
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = product.name,
            fontSize = 25.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.SansSerif
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Text(
                text = "$ ${product.price}",
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 25.dp).align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.weight(1f))
            QuantityProduct(
                modifier = Modifier,
                quantity = quantity,
                onMinus = onMinus,
                onPlus = onPlus
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {navController.navigate("reviews")},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
        ) {
            RateProduct(
            stars = product.stars.toString(),
            reviewCount = product.reviews.toString()
        ) }


        Spacer(modifier = Modifier.height(13.dp))
        Text(
            text = product.description,
            lineHeight = 24.sp,
            color = Color.Gray,
            fontSize = 16.sp
        )

    }
}

@Composable
fun FooterProductDetail(
    modifier: Modifier,
    userId: String,
    productId: String,
    isFavorite: Boolean,
    quantity: Int,
    navController: NavController
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { /* Không xử lý logic */ },
            modifier = Modifier
                .fillMaxHeight()
                .size(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Gray,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(5.dp),
            contentPadding = PaddingValues(10.dp)
        ) {
            if (isFavorite)
                Icon(Icons.Default.Favorite, contentDescription = "Favorite")
            else
                Icon(Icons.Default.FavoriteBorder, contentDescription = "Not Favorite")
        }
        Spacer(modifier = Modifier.width(20.dp))
        Button(
            onClick = { navController.navigate("cart") },
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(7.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            elevation = ButtonDefaults.buttonElevation(12.dp)
        ) {
            Text(
                text = "Add to cart",
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun QuantityProduct(
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

@Composable
fun RateProduct(stars: String, reviewCount: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            Icons.Default.Star,
            contentDescription = "",
            tint = Color.Yellow,
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = stars,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = "($reviewCount review)", color = Color.Gray)
    }
}

fun navigateToProductDetail(productId: String, navController: NavController) {
    navController.navigate("productDetail/$productId")
}

@Preview
@Composable
fun PreviewProductDetailScreen() {
    ProductDetailScreen("fake_product_id", navController = NavController(LocalContext.current))
}