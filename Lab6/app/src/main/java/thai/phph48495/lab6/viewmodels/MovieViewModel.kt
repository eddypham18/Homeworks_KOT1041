package thai.phph48495.lab6.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import thai.phph48495.lab6.model.entities.Movie

class MovieViewModel : ViewModel() {
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies
    init {
        _movies.value = Movie.getSampleMovies()
    }
}