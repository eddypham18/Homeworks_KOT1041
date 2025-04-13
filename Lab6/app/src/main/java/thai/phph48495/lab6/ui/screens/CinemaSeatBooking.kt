package thai.phph48495.lab6.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import thai.phph48495.lab6.model.entities.Seat
import thai.phph48495.lab6.model.entities.SeatStatus
import thai.phph48495.lab6.ui.components.SeatComposable
import thai.phph48495.lab6.utils.createTheaterSeating
import kotlin.random.Random

@Composable
fun CinemaSeatBookingScreen(seats: List<Seat>, totalSeatsPerRow: Int) {
    val textModifer = Modifier.padding(end = 16.dp, start = 4.dp)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(12.dp)
    ) {
        Text(
            text = "Screen",
            modifier = Modifier.padding(16.dp),
            fontStyle = FontStyle.Italic,
            style = MaterialTheme.typography.displaySmall
        )

        Spacer(modifier = Modifier.padding(20.dp))

        LazyVerticalGrid(columns = GridCells.Fixed(totalSeatsPerRow)) {
            items(seats.size){index: Int ->
                SeatComposable(seat = seats[index])
            }
        }

        Spacer(modifier = Modifier.padding(30.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val exampleEmptySeat = remember { Seat('X', 1, SeatStatus.EMPTY) }
            val exampleSelectedSeat = remember { Seat('Y', 1, SeatStatus.SELECTED) }
            val exampleBookedSeat = remember { Seat('Z', 1, SeatStatus.BOOKED) }

            SeatComposable(seat = exampleEmptySeat, clickable = false)
            Text(
                text = "Avaiable",
                style = MaterialTheme.typography.titleSmall,
                modifier = textModifer
            )

            SeatComposable(seat = exampleSelectedSeat, clickable = false)
            Text(
                text = "Selected",
                style = MaterialTheme.typography.titleSmall,
                modifier = textModifer
            )

            SeatComposable(seat = exampleBookedSeat, clickable = false)
            Text(
                text = "Booked",
                style = MaterialTheme.typography.titleSmall,
                modifier = textModifer
            )
        }
    }
}

@Preview
@Composable
fun PreviewCinemaSeatBookingScreen() {
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





