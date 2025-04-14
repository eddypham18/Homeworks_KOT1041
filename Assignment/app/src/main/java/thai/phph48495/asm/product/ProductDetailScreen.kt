package thai.phph48495.asm.product

import android.widget.Toast
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import thai.phph48495.asm.activity.UserSession
import thai.phph48495.asm.cart.CartViewModel
import thai.phph48495.asm.favorite.FavoriteViewModel

@Composable
fun ProductDetailScreen(productId: String, navController: NavController) {
    // ViewModel
    val productViewModel: ProductViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()
    val favoriteViewModel: FavoriteViewModel = viewModel()
    val productDetail = productViewModel.productDetail.value
    val loading by productViewModel.loading.collectAsState()
    val error by productViewModel.error.collectAsState()
    val context = LocalContext.current
    
    // Loading và error từ CartViewModel và FavoriteViewModel
    val cartLoading by cartViewModel.loading.collectAsState()
    val cartError by cartViewModel.error.collectAsState()
    val favoriteLoading by favoriteViewModel.loading.collectAsState()
    val favoriteError by favoriteViewModel.error.collectAsState()
    
    // Lấy dữ liệu sản phẩm khi màn hình được khởi tạo
    LaunchedEffect(productId) {
        productViewModel.getProductById(productId)
    }

    var productQuantity by remember { mutableStateOf(1) }
    var isFavorite by remember { mutableStateOf(false) }
    val userId = UserSession.getUserId(context) ?: ""
    
    // Kiểm tra sản phẩm có trong danh sách yêu thích không
    LaunchedEffect(userId, productId) {
        if (userId.isNotEmpty() && productId.isNotEmpty()) {
            favoriteViewModel.getFavorites(userId) // Đầu tiên lấy danh sách yêu thích mới nhất
            favoriteViewModel.checkFavorite(userId, productId) { result ->
                isFavorite = result
            }
        }
    }
    
    // Theo dõi thay đổi từ favoriteViewModel để cập nhật trạng thái yêu thích
    val favorites by favoriteViewModel.favorites.collectAsState()
    
    // Cập nhật trạng thái yêu thích khi danh sách favorites thay đổi
    LaunchedEffect(favorites) {
        if (userId.isNotEmpty() && productId.isNotEmpty()) {
            val isProductFavorite = favorites.any { it.userId == userId && it.productId == productId }
            isFavorite = isProductFavorite
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Hiển thị lỗi nếu có
        if (error != null || cartError != null || favoriteError != null) {
            Snackbar(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = error ?: cartError ?: favoriteError ?: "Đã xảy ra lỗi")
            }
            LaunchedEffect(error) {
                if (error != null) {
                    productViewModel.resetError()
                }
            }
            LaunchedEffect(cartError) {
                if (cartError != null) {
                    cartViewModel.resetError()
                }
            }
            LaunchedEffect(favoriteError) {
                if (favoriteError != null) {
                    favoriteViewModel.resetError()
                }
            }
        }
        
        // Hiển thị loading
        if (loading || cartLoading || favoriteLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } 
        // Hiển thị chi tiết sản phẩm khi có dữ liệu
        else if (productDetail != null) {
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
                    HeaderProductDetail(productDetail, navController)
                }
                // Nội dung chi tiết sản phẩm
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    BodyProductDetail(
                        product = productDetail,
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
                product = productDetail,
                userId = userId,
                isFavorite = isFavorite,
                quantity = productQuantity,
                navController = navController,
                onAddToCart = {
                    if (userId.isEmpty()) {
                        Toast.makeText(context, "Vui lòng đăng nhập để thêm vào giỏ hàng", Toast.LENGTH_SHORT).show()
                    } else {
                        cartViewModel.addToCart(userId, productDetail, productQuantity)
                        Toast.makeText(context, "Đã thêm ${productDetail.name} vào giỏ hàng", Toast.LENGTH_SHORT).show()
                    }
                },
                onToggleFavorite = { favorite ->
                    if (userId.isEmpty()) {
                        Toast.makeText(context, "Vui lòng đăng nhập để thêm vào yêu thích", Toast.LENGTH_SHORT).show()
                    } else {
                        if (favorite) {
                            favoriteViewModel.addToFavorite(userId, productDetail)
                            Toast.makeText(context, "Đã thêm ${productDetail.name} vào danh sách yêu thích", Toast.LENGTH_SHORT).show()
                        } else {
                            favoriteViewModel.removeFavoriteByProductId(userId, productId)
                            Toast.makeText(context, "Đã xóa ${productDetail.name} khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show()
                        }
                        favoriteViewModel.getFavorites(userId)
                    }
                }
            )
        }
        // Hiển thị thông báo khi không tìm thấy sản phẩm
        else if (!loading && error == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Không tìm thấy sản phẩm", fontSize = 18.sp)
            }
        }
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
            // Xếp hạng sản phẩm
            Icon(
                Icons.Default.Star,
                contentDescription = "Rating",
                tint = Color(0xFFFFB818)
            )
            Text(
                text = "${product.stars}",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "(${product.reviews} reviews)",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        // Mô tả sản phẩm
        Text(
            text = product.description,
            fontSize = 16.sp,
            color = Color.Gray,
            fontFamily = FontFamily.SansSerif
        )
        Spacer(modifier = Modifier.height(20.dp))
        
        // Chọn số lượng
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onMinus,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF5F5F5),
                        contentColor = Color.Black
                    ),
                    modifier = Modifier.size(30.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(text = "-", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    text = quantity.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(15.dp))
                Button(
                    onClick = onPlus,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF5F5F5),
                        contentColor = Color.Black
                    ),
                    modifier = Modifier.size(30.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(text = "+", fontSize = 20.sp)
                }
            }
            
            // Hiển thị giá
            Text(
                text = "$ ${product.price}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun FooterProductDetail(
    modifier: Modifier = Modifier,
    product: Product,
    userId: String,
    isFavorite: Boolean,
    quantity: Int,
    navController: NavController,
    onAddToCart: () -> Unit,
    onToggleFavorite: (Boolean) -> Unit
) {
    var favorite by remember { mutableStateOf(isFavorite) }
    
    // Cập nhật trạng thái khi isFavorite thay đổi từ bên ngoài
    LaunchedEffect(isFavorite) {
        favorite = isFavorite
    }
    
    Row(
        modifier = modifier
            .height(60.dp)
            .background(Color.Transparent),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Nút yêu thích
        Button(
            onClick = { 
                favorite = !favorite
                onToggleFavorite(favorite)
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = if (favorite) Color.Red else Color.Black
            ),
            elevation = ButtonDefaults.buttonElevation(5.dp),
            modifier = Modifier
                .padding(end = 16.dp)
                .width(60.dp)
                .fillMaxHeight()
        ) {
            Icon(
                if (favorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
                modifier = Modifier.size(24.dp)
            )
        }
        
        // Nút thêm vào giỏ hàng
        Button(
            onClick = onAddToCart,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Text(
                text = "Add to cart | $${product.price * quantity}",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
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