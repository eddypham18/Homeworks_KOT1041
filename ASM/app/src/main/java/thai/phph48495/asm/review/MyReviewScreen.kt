package thai.phph48495.asm.review

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import coil.compose.AsyncImage
import thai.phph48495.asm.R
import thai.phph48495.asm.activity.HeaderWithBack

// Data class cho Review
data class MyReview(
    val reviewer: String,
    val date: String,
    val rating: Int,
    val comment: String,
    val productName: String,
    val price: String,
    val productImageRes: Int
)

@Composable
fun MyReView(navController: NavController) {
    Scaffold(
        topBar = {
            HeaderWithBack(navController = navController, text = "My Reviews", modifier = Modifier.fillMaxWidth())
        },
        content = { innerPadding ->
            ShowMyReView(innerPadding)
        }
    )
}

@Composable
fun ShowMyReView(innerPaddingValues: PaddingValues) {
    val reviews = remember {
        listOf(
            MyReview(
                reviewer = "Bruno Fernandes",
                date = "20/03/2020",
                rating = 5,
                comment = "Nice Furniture with good delivery. The delivery time is very fast. The products look exactly like the picture in the app. Besides, the color is also the same and the quality is very good despite the very cheap price.",
                productName = "Minimal Stand",
                price = "$50.00",
                productImageRes = R.drawable.image1
            ),
            MyReview(
                reviewer = "Alice Johnson",
                date = "15/04/2020",
                rating = 4,
                comment = "Good quality furniture but a bit expensive. Overall, satisfied with the purchase.",
                productName = "Modern Chair",
                price = "$80.00",
                productImageRes = R.drawable.image1
            ),
            MyReview(
                reviewer = "David Lee",
                date = "01/05/2020",
                rating = 5,
                comment = "Excellent design and durability. Highly recommended!",
                productName = "Wooden Table",
                price = "$120.00",
                productImageRes = R.drawable.image1
            )
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, top = innerPaddingValues.calculateTopPadding(), end = 10.dp)
    ) {
        LazyColumn {
            items(reviews) { review ->
                MyReViewItem(review = review)
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}

@Composable
fun MyReViewItem(review: MyReview) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(270.dp)
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(Color.White),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Hiển thị ảnh sản phẩm (avatar review) theo resource ID
                Image(
                    painter = painterResource(id = review.productImageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .width(90.dp)
                        .height(90.dp),
                    contentScale = ContentScale.FillBounds
                )
                Column(
                    modifier = Modifier
                        .width(200.dp)
                        .padding(start = 20.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = review.productName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = review.price,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = review.reviewer,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = review.date,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray
                    )
                }
                Row(
                    modifier = Modifier.width(120.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(review.rating) {
                        Image(
                            painter = painterResource(id = R.drawable.stars),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Text(
                    text = review.comment,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Justify,
                    color = Color.Black,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        // Hiển thị avatar review (ở góc trên bên trái)
        Image(
            painter = painterResource(id = R.drawable.avatar),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .padding(8.dp)
                .clip(RoundedCornerShape(50)),
            contentScale = ContentScale.Crop
        )
    }
}


@Preview
@Composable
fun PreviewMyReView() {
    MyReView(navController = NavController(LocalContext.current))
}
