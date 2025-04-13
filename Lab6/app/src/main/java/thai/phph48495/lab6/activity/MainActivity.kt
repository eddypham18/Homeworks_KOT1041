package thai.phph48495.lab6.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import thai.phph48495.lab6.ui.screens.CinemaSeatBookingScreen
import thai.phph48495.lab6.ui.screens.MovieScreen
import thai.phph48495.lab6.ui.theme.Lab6Theme
import thai.phph48495.lab6.utils.createTheaterSeating

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            Lab6Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyApp(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}

@Composable
fun MyApp(name: String, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "bai1",
        modifier = modifier
    ) {
        composable("bai1") {
            MovieScreen(navController)
        }

        composable("bai3") {
            val totalRows = 9
            val totalSeatsPerRow = 9
            val aislePositionInRow = 4
            val aislePositionInColumn = 5

            val seats = createTheaterSeating(
                totalRows = totalRows,
                totalSeatsPerRow = totalSeatsPerRow,
                aislePositionInRow = aislePositionInRow,
                aislePositionInColumn = aislePositionInColumn
            )

            CinemaSeatBookingScreen(
                seats = seats,
                totalSeatsPerRow = totalSeatsPerRow
            )
        }
    }
}
