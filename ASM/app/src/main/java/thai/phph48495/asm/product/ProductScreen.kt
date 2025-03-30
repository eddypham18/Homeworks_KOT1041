package thai.phph48495.asm.product

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import thai.phph48495.asm.R



@Composable
fun ProductScreen(navController: NavController) {
    // Fake danh sách sản phẩm mẫu
    val fakeProducts = listOf(
        Product(
            _id = "p1",
            name = "Black Simple Lamp",
            price = 12.00,
            stars = 4.5,
            reviews = 150,
            description = "Sofa set with modern design for comfortable living.",
            image = "https://s3-alpha-sig.figma.com/img/2443/fe11/03a0919f36f923a162b57615bd507c3e?Expires=1743984000&Key-Pair-Id=APKAQ4GOSFWCW27IBOMQ&Signature=fdFTG78faNNUcHvuxZlX2D6Z195~UYWa6e0cNS6731JACHwE0p8Slh3MCpAis1Nyjw05KRhHvXDkZnXBoMYjc10noFvbjVaLF~PYUMKFVLHonocWIAf42mg7Y~J2oLDNmcgJfg9NuM6VVZOpDb42-f8THzgZ2F6FRsfbcUE~p3VT4BqnOFYmeHZS4pSQUbjfEpESKK6k1BPFWwyQ~hvMA0QnBPHSubedFIWLuu3e74quQcbTXPI0wP5l6yBO3p1yn-wPJAwkAenbJ4zoJeoSAB4f4WBKbeHXHJ1WhU4W4fAHSYar4bC1Pfkj405Sclo~UMPU4uWfOLPB1-uFrZwbHg__"
        ),
        Product(
            _id = "p2",
            name = "Coffee Chair",
            price = 20.00,
            stars = 4.0,
            reviews = 80,
            description = "A modern chair that complements any living room.",
            image = "https://s3-alpha-sig.figma.com/img/d2cc/b173/ffcf81766d608d6e6d7a70ae8ebfe5bb?Expires=1743984000&Key-Pair-Id=APKAQ4GOSFWCW27IBOMQ&Signature=BFpdCbdh1N2GLv0hDtWkzX6Bm2yHGAhoVbt6utstmuLFhgswfd1qqJq1Ol2BPz4HOW~ar-CzSys-id6192Wbhv0ZjjSFj5i8N-cgpHL~zx1lR1Sbp1MyKxmeJFmfAMYfv8duSfVOQqA6jPfVJckAuURp1GdRJ6dhdtFNwGgGjvo6BRkLUoWaB0EzeQ7gFYKamP4SMLgzmg7o-YP1tK2cRXvCyXRMDfdO1XA9y6r1so3R60QI2QeIkMeiCJUxiM8BlW96nyQxz9MyxKA7gukPCLND5VvqoANudUEAdm6OB5K7mO0q60ppUeAj-n5E2k0Z2A~8Biy6WAhvj77ksq91MA__"
        ),
        Product(
            _id = "p3",
            name = "Minimal Stand",
            price = 25.00,
            stars = 4.8,
            reviews = 200,
            description = "Elegant wooden table perfect for dining area.",
            image = "https://s3-alpha-sig.figma.com/img/01b3/a6d6/88ed658a2d0159fe18074af4a860b24f?Expires=1743984000&Key-Pair-Id=APKAQ4GOSFWCW27IBOMQ&Signature=MhW~veypgUtDmzGJ8HiY1hxwn8t5bgOUCstD9TpJq~wR2gPZyAR7a2KjT1Yu3WIL5084S7GjAJ3NZMM8DjikKHJQGK28AkAPyK5ImM95HfUCY75AV5dasjn3TDxNKwbq7GzdpgyO2za9KBWs1rHnSccB9M0Ao0S7QEtZ8xwpfUbviksy~fr9lpz6UUnlGfLkjlvobI-517GGS-JjPKQ8dB5yZVLIgHVEN15bD8AfhC~seGofV~pqdkxytuJvjFlH-paUpb1MsTWIX0kRMUI8ftbfh35Qr6ZTnaUc38cfN73hcnN6aUZ614aATxRub9CHrS1IGNH1fAmhtHtFKzPsCQ__"
        ),
        Product(
            _id = "p4",
            name = "Simple Desk",
            price = 50.00,
            stars = 4.3,
            reviews = 110,
            description = "Cozy armchair with a classic touch.",
            image = "https://s3-alpha-sig.figma.com/img/d628/863f/83328d299b2df6ee0daa119655bdd3f5?Expires=1743984000&Key-Pair-Id=APKAQ4GOSFWCW27IBOMQ&Signature=azB2cQ65ih7OvQwkGFplliSo4E6wTnho3KOvc9vYGbSAbNe7xH~4sF5RAdQF~~SU1Vp-UPFexZWjYKtmKYehxHOlk0xuSRADhQ2rqTilc4FGKkKzl0L-ghWo8G6M7OuISQoFRhL72OEj5uHdKQcZHS207ww5UQdbL10QJ5oy3nZ95PSeCxvNRmHlNcBcDtgo1u-FZnOKZWsnp3h7tlzOqYOwffoxdww7Y~U5i3yLe5~-oEwmfm1PzcSHSm6Cpue7MdXt2xfLjupVI649hQyA-0pqKK2kB4Z3stKixlGUx5i4TJ7YhiiR55t5cU6iVjtCcZvsn17pfVBXFlKQlwMUSg__"
        )
    )

    Column(modifier = Modifier.fillMaxSize()) {
        // Hiển thị menu chip (danh mục)
        ListMenuChip()
        // Hiển thị danh sách sản phẩm ở dạng lưới
        ListProduct(products = fakeProducts, navController = navController)
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
            .clickable { navController.navigate("productDetail/${product._id}") },
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
fun ListMenuChip() {
    // Fake danh sách danh mục hiển thị cho menu chip
    val listMenuChip = listOf(
        Category(icon = R.drawable.stars, text = "Popular"),
        Category(icon = R.drawable.chair, text = "Chair"),
        Category(icon = R.drawable.table, text = "Table"),
        Category(icon = R.drawable.armchair, text = "Armchair"),
        Category(icon = R.drawable.bed, text = "Bed"),
        Category(icon = R.drawable.lamp, text = "Lamp"),
    )
    var selectedChipIndex by remember { mutableStateOf(-1) }
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(listMenuChip) { item ->
            val index = listMenuChip.indexOf(item)
            ItemChipFilter(
                icon = item.icon,
                text = item.text,
                isSelected = selectedChipIndex == index,
                onClick = {
                    selectedChipIndex = if (selectedChipIndex == index) -1 else index
                }
            )
        }
    }
}

// Hàm fake danh mục (có thể sử dụng để hiển thị thêm chip nếu cần)
fun fakeMenuChip(): List<Category> {
    return List(10) { index ->
        Category(icon = R.drawable.icon_cart, text = "Item $index")
    }
}
