package thai.phph48495.lab6.model.entities

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