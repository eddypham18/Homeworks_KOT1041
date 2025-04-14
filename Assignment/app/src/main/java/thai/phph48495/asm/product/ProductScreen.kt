package thai.phph48495.asm.product

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import thai.phph48495.asm.R



@Composable
fun ProductScreen(navController: NavController) {
    // ViewModel
    val productViewModel: ProductViewModel = viewModel()
    val products = productViewModel.listProduct.value
    val loading by productViewModel.loading.collectAsState()
    val error by productViewModel.error.collectAsState()
    
    // Lấy dữ liệu sản phẩm khi màn hình được khởi tạo
    LaunchedEffect(Unit) {
        productViewModel.getAllProducts()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Hiển thị menu chip (danh mục)
        ListMenuChip(onCategorySelected = { category ->
            if (category.isEmpty()) {
                productViewModel.getAllProducts()
            } else {
                productViewModel.getProductsByCategory(category)
            }
        })

        // Hiển thị lỗi nếu có
        if (error != null) {
            Snackbar(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = error ?: "Đã xảy ra lỗi")
            }
            LaunchedEffect(error) {
                // Tự động ẩn thông báo lỗi sau một khoảng thời gian
                if (error != null) {
                    productViewModel.resetError()
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
        // Hiển thị danh sách sản phẩm
        else if (!products.isNullOrEmpty()) {
            ListProduct(products = products, navController = navController)
        }
        // Hiển thị thông báo nếu không có sản phẩm
        else if (!loading && error == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Không có sản phẩm nào", fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun ListProduct(products: List<Product>, navController: NavController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp)
    ) {
        items(products) { product ->
            ItemProduct(product = product, navController = navController)
        }
    }
}

@Composable
fun ItemProduct(product: Product, navController: NavController) {
    // Khi nhấn vào sản phẩm, điều hướng đến màn hình chi tiết sản phẩm
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("productDetail/${product.id}")
            },
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray)
                .width(155.dp)
                .height(200.dp)
        ) {
            AsyncImage(
                model = product.image,
                contentDescription = "img product",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = product.name,
            fontSize = 17.sp,
            color = Color.Gray
        )
        Text(
            text = "$ ${product.price}",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemChipFilter(icon: Int, text: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FilterChip(
            selected = isSelected,
            onClick = onClick,
            label = {},
            border = null,
            colors = FilterChipDefaults.filterChipColors(
                containerColor = Color(0x9ED8D8D8),
                selectedContainerColor = Color.Black,
                selectedLeadingIconColor = Color.White,
                selectedTrailingIconColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = "filter icon",
                    modifier = Modifier.size(40.dp)
                )
            },
            modifier = Modifier
                .width(44.dp)
                .height(44.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = text,
            fontSize = 14.sp,
            color = if (isSelected) Color.Black else Color(0x6D949494)
        )
    }
}

@Composable
fun ListMenuChip(onCategorySelected: (String) -> Unit) {
    // Danh sách danh mục hiển thị cho menu chip
    val listMenuChip = listOf(
        Category(icon = R.drawable.stars, text = "All"),
        Category(icon = R.drawable.chair, text = "Chair"),
        Category(icon = R.drawable.table, text = "Table"),
        Category(icon = R.drawable.armchair, text = "Armchair"),
        Category(icon = R.drawable.bed, text = "Bed"),
        Category(icon = R.drawable.lamp, text = "Lamp"),
    )
    
    // Map hiển thị sang category trong API
    val categoryMap = mapOf(
        "All" to "",
        "Chair" to "chair",
        "Table" to "table",
        "Armchair" to "armchair",
        "Bed" to "bed",
        "Lamp" to "lamp"
    )
    
    var selectedChipIndex by remember { mutableStateOf(0) }
    
    LazyRow(
        modifier = Modifier.padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(listMenuChip.withIndex().toList()) { (index, category) ->
            ItemChipFilter(
                icon = category.icon,
                text = category.text,
                isSelected = selectedChipIndex == index,
                onClick = {
                    selectedChipIndex = index
                    onCategorySelected(categoryMap[category.text] ?: "")
                }
            )
        }
    }
}

