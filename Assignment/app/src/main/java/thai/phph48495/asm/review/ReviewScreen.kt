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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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



@Composable
fun ReViewScreen(navController: NavController) {
    Scaffold(
        topBar = {
            HeaderWithBack(
                text = "Review & Rating",
                navController = navController,
                modifier = Modifier.fillMaxWidth()
            )
        },
    ) { innerPadding ->
        ReviewAndRatingScreen(innerPadding)
    }
}

@Composable
fun ReviewAndRatingScreen(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 10.dp,
                top = innerPadding.calculateTopPadding(),
                end = 10.dp
            )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            ProductReview()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 25.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                CustomButton(
                    title = "Write a review",
                    modifier = Modifier
                        .padding(7.dp)
                        .fillMaxWidth()
                        .height(60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF242424))
                        .clickable { /* Không xử lý logic ở đây */ },
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W600
                    )
                )
            }
        }
    }
}

@Composable
fun ProductReview() {
    // Fake danh sách review
    val reviews = remember {
        listOf(
            Review(
                reviewer = "Bruno Fernandes",
                date = "20/03/2020",
                rating = 5,
                comment = "Nice Furniture with good delivery. The delivery time is very fast. The products look exactly like the picture in the app. Besides, the color is also the same and the quality is very good despite the very cheap price.",
                avatarUrl = "https://png.pngtree.com/png-clipart/20230930/original/pngtree-man-avatar-isolated-png-image_13022161.png"
            ),
            Review(
                reviewer = "Alice Johnson",
                date = "15/04/2020",
                rating = 4,
                comment = "The product is as described and the shipping was quick. However, packaging could be improved.",
                avatarUrl = "https://png.pngtree.com/png-clipart/20230930/original/pngtree-man-avatar-isolated-png-image_13022161.png"
            ),
            Review(
                reviewer = "David Lee",
                date = "01/05/2020",
                rating = 5,
                comment = "Excellent quality! I love the design and the comfort level is top-notch.",
                avatarUrl = "https://png.pngtree.com/png-clipart/20230930/original/pngtree-man-avatar-isolated-png-image_13022161.png"
            )
        )
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .background(Color.White),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.image1),
                contentDescription = null,
                modifier = Modifier
                    .width(110.dp)
                    .height(120.dp),
                contentScale = ContentScale.FillBounds
            )
            Column(
                modifier = Modifier
                    .width(200.dp)
                    .padding(start = 20.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Minimal Stand",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )
                Row(
                    modifier = Modifier.width(200.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.stars),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "4.5",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(7.dp)
                    )
                }
                Text(
                    text = "10 reviews",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color("#303030".toColorInt())
                )
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        Divider(color = Color("#F0F0F0".toColorInt()), thickness = 1.dp)
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn {
            items(reviews) { review ->
                ReViewItem(review = review)
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
        Spacer(modifier = Modifier.height(250.dp))
    }
}

@Composable
fun ReViewItem(review: Review) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Column(
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                        .background(Color.White)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(15.dp),
                        verticalArrangement = Arrangement.SpaceEvenly,
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
            }

            Image(
                painter = painterResource(id = R.drawable.avatar),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(25.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun CustomButton(
    title: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle
) {
    Box(
        modifier = modifier.clickable { /* Fake: không xử lý logic */ },
        contentAlignment = Alignment.Center
    ) {
        Text(text = title, style = textStyle)
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewReViewScreen() {
    ReViewScreen(navController = NavController(LocalContext.current))
}