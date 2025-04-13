package thai.phph48495.asm.favorite

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import thai.phph48495.asm.activity.UserSession
import thai.phph48495.asm.product.navigateToProductDetail


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    navController: NavController,
    favoriteViewModel: FavoriteViewModel = viewModel()
) {
    val context = LocalContext.current
    val userId = UserSession.getUserId(context) ?: ""
    
    // Theo dõi trạng thái từ ViewModel
    val favorites by favoriteViewModel.favorites.collectAsState()
    val loading by favoriteViewModel.loading.collectAsState()
    val error by favoriteViewModel.error.collectAsState()
    
    // Lấy danh sách yêu thích khi vào màn hình
    LaunchedEffect(Unit) {
        if (userId.isNotEmpty()) {
            favoriteViewModel.getFavorites(userId)
        }
    }
    
    // Xử lý khi chưa đăng nhập
    if (userId.isEmpty()) {
        DisposableEffect(Unit) {
            // Hiển thị thông báo chưa đăng nhập
            Toast.makeText(context, "Vui lòng đăng nhập để xem sản phẩm yêu thích", Toast.LENGTH_SHORT).show()
            onDispose { }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Vui lòng đăng nhập để xem sản phẩm yêu thích")
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // Hiển thị lỗi nếu có
        if (error != null) {
            LaunchedEffect(error) {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                favoriteViewModel.resetError()
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
        // Kiểm tra danh sách rỗng
        else if (favorites.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Bạn chưa có sản phẩm yêu thích nào", fontSize = 16.sp)
            }
        } 
        // Hiển thị danh sách sản phẩm yêu thích
        else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                // Lọc items để không có null trong các trường quan trọng
                val validFavorites = favorites.filter { 
                    it.id != null &&
                    it.productId != null && 
                    it.productId.isNotEmpty() && 
                    it.nameProduct != null 
                }
                
                items(validFavorites) { favorite ->
                    ItemFavorite(
                        favorite = favorite,
                        navController = navController,
                        onDeleteItem = {
                            favoriteViewModel.deleteFavorite(favorite.id ?: "")
                            Toast.makeText(
                                context,
                                "Đã xóa ${favorite.nameProduct ?: "sản phẩm"} khỏi danh sách yêu thích",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ItemFavorite(
    favorite: Favorite,
    navController: NavController,
    onDeleteItem: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                if (favorite.productId.isNotEmpty()) {
                    navigateToProductDetail(favorite.productId, navController)
                }
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(7.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray)
                        .width(100.dp)
                        .height(100.dp)
                ) {
                    AsyncImage(
                        model = favorite.imageProduct,
                        contentDescription = "img product",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Column {
                    Text(text = favorite.nameProduct ?: "Không có tên", fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "$ ${favorite.priceProduct}",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.Bottom),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "remove item favorite",
                    modifier = Modifier.clickable {
                        onDeleteItem()
                    }
                )
            }
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
