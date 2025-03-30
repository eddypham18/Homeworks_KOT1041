package thai.phph48495.asm.notification

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import thai.phph48495.asm.R
import thai.phph48495.asm.product.Product


@Composable
fun NotifyScreen(navController: NavController) {

    // Fake danh sách sản phẩm để hiển thị thông báo
    val fakeProductArr = remember {
        listOf(
            Product(
                _id = "1",
                name = "Order #123456789",
                price = 0.0,
                stars = 0.0,
                reviews = 0,
                description = "Your order #123456789 has been confirmed",
                image = "https://via.placeholder.com/150"
            ),
            Product(
                _id = "2",
                name = "Order #987654321",
                price = 0.0,
                stars = 0.0,
                reviews = 0,
                description = "Your order #987654321 has been confirmed",
                image = "https://via.placeholder.com/150"
            ),
            Product(
                _id = "3",
                name = "Order #1122334455",
                price = 0.0,
                stars = 0.0,
                reviews = 0,
                description = "Your order #1122334455 has been confirmed",
                image = "https://via.placeholder.com/150"
            )
        )
    }
    Scaffold (

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            NotificationGrid(productArr = fakeProductArr)
        }
    }
}




@Composable
fun NotificationGrid(productArr: List<Product>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(productArr) { product ->
            NotificationItem()
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun NotificationItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.image2),
            contentDescription = null,
            modifier = Modifier
                .width(100.dp)
                .fillMaxHeight(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .padding(start = 10.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.padding(end = 10.dp)) {
                Text(
                    text = "Your order #123456789 has been confirmed",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Turpis pretium et in arcu adipiscing nec. Turpis pretium et in arcu adipiscing nec.",
                    fontSize = 13.sp,
                    fontWeight = FontWeight(400),
                    color = Color.Gray,
                    textAlign = TextAlign.Justify,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "")
                    Text(
                        text = "New",
                        fontSize = 15.sp,
                        fontWeight = FontWeight(800),
                        color = Color("#27AE60".toColorInt())
                    )
                }
            }
        }
    }
}