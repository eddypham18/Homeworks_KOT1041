package thai.phph48495.lab6.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage

data class Movie(
    val title: String,
    val time: String,
    val posterUrl: String,
    val description: String
){
    companion object {
        fun getSampleMovies() = listOf<Movie>(
            Movie(
                title = "Avatar: The Way of Water",
                time = "3h 12m",
                posterUrl = "https://m.media-amazon.com/images/M/MV5BNmQxNjZlZTctMWJiMC00NGMxLWJjNTctNTFiNjA1Njk3ZDQ5XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg",
                description = "Avatar: The Way of Water is a 2022 American epic science fiction film directed by James Cameron."
            ),
            Movie(
                title = "Black Panther: Wakanda Forever",
                time = "2h 41m",
                posterUrl = "https://upload.wikimedia.org/wikipedia/vi/3/3b/Black_Panther_Wakanda_Forever_poster.jpg",
                description = "Black Panther: Wakanda Forever is a 2022 American superhero film based on the Marvel Comics character Black Panther."
            ),
            Movie(
                title = "The Batman",
                time = "2h 55m",
                posterUrl = "https://m.media-amazon.com/images/M/MV5BMmU5NGJlMzAtMGNmOC00YjJjLTgyMzUtNjAyYmE4Njg5YWMyXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg",
                description = "The Batman is a 2022 American superhero film based on the DC Comics character Batman."
            ),
            Movie(
                title = "Mr.Robot",
                time = "3h 50m",
                posterUrl = "https://cehvietnam.com/wp-content/uploads/2017/05/poster-medium.jpg?w=450",
                description = "Mr. Robot is an American drama thriller television series created by Sam Esmail."
            ),
            Movie(
                title = "The Batman",
                time = "2h 55m",
                posterUrl = "https://m.media-amazon.com/images/M/MV5BMmU5NGJlMzAtMGNmOC00YjJjLTgyMzUtNjAyYmE4Njg5YWMyXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg",
                description = "The Batman is a 2022 American superhero film based on the DC Comics character Batman."
            ),
            Movie(
                title = "Mr.Robot",
                time = "3h 50m",
                posterUrl = "https://cehvietnam.com/wp-content/uploads/2017/05/poster-medium.jpg?w=450",
                description = "Mr. Robot is an American drama thriller television series created by Sam Esmail."
            )
        )
    }
}

enum class ListType{
    ROW, COLUMN, GRID
}

@Composable
fun MovieScreen(navController: NavController) {

    val movies = Movie.getSampleMovies()
    var listType by remember { mutableStateOf(ListType.ROW) }

    Column{
        Row(
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {listType = ListType.ROW }) {
                Text(text = "Row")
            }
            Button(onClick = {listType = ListType.COLUMN }) {
                Text(text = "Column")
            }
            Button(onClick = {listType = ListType.GRID }) {
                Text(text = "Grid")
            }
            Button(onClick = {navController.navigate("bai3")}) {
                Text("Sang bài 3")
            }
        }
        when(listType){
            ListType.ROW -> MovieRow(movies = movies)
            ListType.COLUMN -> MovieColumn(movies = movies)
            ListType.GRID -> MovieGrid(movies = movies)
        }
    }

}

@Composable
fun MovieRow(
    movies: List<Movie>
){
    LazyRow(
        state = rememberLazyListState(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(movies.size) { index ->
            MovieItem(movie = movies[index])
        }
    }
}

@Composable
fun MovieItem(movie: Movie){
    Card (
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.width(175.dp).height(330.dp)
        ) {
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.height(255.dp).fillMaxWidth().clip(RoundedCornerShape(topEnd = 8.dp, topStart = 8.dp))
            )

            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2
                )
                Text(
                    text = "Thời lượng" +movie.time,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2
                )

            }
        }
    }
}

//Movie Column
@Composable
fun MovieColumn(
    movies: List<Movie>
){
    LazyColumn (
        state = rememberLazyListState(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(movies.size) { index ->
            MovieColumnItem(movie = movies[index])
        }
    }
}

@Composable
fun MovieColumnItem(movie: Movie){
    Card (
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth().height(250.dp)
        ) {
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.width(150.dp).fillMaxHeight().clip(RoundedCornerShape(topEnd = 8.dp, topStart = 8.dp))
            )

            Column(
                modifier = Modifier.padding(8.dp).weight(1f).fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2
                )
                Text(
                    text = "Thời lượng: " +movie.time,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2
                )

                Text(
                    text = "Mô tả: " +movie.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2
                )

            }
        }
    }
}


//Movie Grid
@Composable
fun MovieGrid(
    movies: List<Movie>
){
    LazyVerticalStaggeredGrid (
        state = rememberLazyStaggeredGridState(),
        columns = StaggeredGridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalItemSpacing = 8.dp,
        contentPadding = PaddingValues(8.dp)
    ) {
        items(movies.size) { index ->
            MovieItem(movie = movies[index])
        }
    }
}