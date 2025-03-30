package thai.phph48495.asm.favorite

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
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import thai.phph48495.asm.product.navigateToProductDetail


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(navController: NavController) {
    // Tạo dữ liệu giả cho danh sách yêu thích
    val favoriteProducts = listOf(
        Favorite(
            _id = "1",
            userId = "user1",
            productId = "p1",
            isFavorite = true,
            nameProduct = "Black Simple Lamp",
            priceProduct = 10.0,
            imageProduct = "https://s3-alpha-sig.figma.com/img/2443/fe11/03a0919f36f923a162b57615bd507c3e?Expires=1743984000&Key-Pair-Id=APKAQ4GOSFWCW27IBOMQ&Signature=fdFTG78faNNUcHvuxZlX2D6Z195~UYWa6e0cNS6731JACHwE0p8Slh3MCpAis1Nyjw05KRhHvXDkZnXBoMYjc10noFvbjVaLF~PYUMKFVLHonocWIAf42mg7Y~J2oLDNmcgJfg9NuM6VVZOpDb42-f8THzgZ2F6FRsfbcUE~p3VT4BqnOFYmeHZS4pSQUbjfEpESKK6k1BPFWwyQ~hvMA0QnBPHSubedFIWLuu3e74quQcbTXPI0wP5l6yBO3p1yn-wPJAwkAenbJ4zoJeoSAB4f4WBKbeHXHJ1WhU4W4fAHSYar4bC1Pfkj405Sclo~UMPU4uWfOLPB1-uFrZwbHg__"
        ),
        Favorite(
            _id = "2",
            userId = "user1",
            productId = "p2",
            isFavorite = true,
            nameProduct = "Coffee Chair",
            priceProduct = 20.0,
            imageProduct = "https://s3-alpha-sig.figma.com/img/d2cc/b173/ffcf81766d608d6e6d7a70ae8ebfe5bb?Expires=1743984000&Key-Pair-Id=APKAQ4GOSFWCW27IBOMQ&Signature=BFpdCbdh1N2GLv0hDtWkzX6Bm2yHGAhoVbt6utstmuLFhgswfd1qqJq1Ol2BPz4HOW~ar-CzSys-id6192Wbhv0ZjjSFj5i8N-cgpHL~zx1lR1Sbp1MyKxmeJFmfAMYfv8duSfVOQqA6jPfVJckAuURp1GdRJ6dhdtFNwGgGjvo6BRkLUoWaB0EzeQ7gFYKamP4SMLgzmg7o-YP1tK2cRXvCyXRMDfdO1XA9y6r1so3R60QI2QeIkMeiCJUxiM8BlW96nyQxz9MyxKA7gukPCLND5VvqoANudUEAdm6OB5K7mO0q60ppUeAj-n5E2k0Z2A~8Biy6WAhvj77ksq91MA__"
        ),
        Favorite(
            _id = "3",
            userId = "user1",
            productId = "p3",
            isFavorite = true,
            nameProduct = "Minimal Stand",
            priceProduct = 30.0,
            imageProduct = "https://s3-alpha-sig.figma.com/img/01b3/a6d6/88ed658a2d0159fe18074af4a860b24f?Expires=1743984000&Key-Pair-Id=APKAQ4GOSFWCW27IBOMQ&Signature=MhW~veypgUtDmzGJ8HiY1hxwn8t5bgOUCstD9TpJq~wR2gPZyAR7a2KjT1Yu3WIL5084S7GjAJ3NZMM8DjikKHJQGK28AkAPyK5ImM95HfUCY75AV5dasjn3TDxNKwbq7GzdpgyO2za9KBWs1rHnSccB9M0Ao0S7QEtZ8xwpfUbviksy~fr9lpz6UUnlGfLkjlvobI-517GGS-JjPKQ8dB5yZVLIgHVEN15bD8AfhC~seGofV~pqdkxytuJvjFlH-paUpb1MsTWIX0kRMUI8ftbfh35Qr6ZTnaUc38cfN73hcnN6aUZ614aATxRub9CHrS1IGNH1fAmhtHtFKzPsCQ__"
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(7.dp)) {
            items(favoriteProducts) { favorite ->
                ItemFavorite(
                    favorite = favorite,
                    navController = navController,
                    onDeleteItem = {
                    }
                )
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
                navigateToProductDetail(favorite.productId, navController)
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
                    Text(text = favorite.nameProduct, fontSize = 14.sp)
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
