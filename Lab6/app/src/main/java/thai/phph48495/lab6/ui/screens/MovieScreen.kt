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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import thai.phph48495.lab6.model.entities.Movie
import thai.phph48495.lab6.viewmodels.MovieViewModel


enum class ListType{
    ROW, COLUMN, GRID
}

@Composable
fun MovieScreen(navController: NavController) {

    val movieViewModel: MovieViewModel = viewModel()
    val movies by movieViewModel.movies.observeAsState(initial = emptyList())
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