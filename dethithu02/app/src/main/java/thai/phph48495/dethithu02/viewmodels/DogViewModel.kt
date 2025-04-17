package thai.phph48495.dethithu02.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import thai.phph48495.dethithu02.models.Dog
import thai.phph48495.dethithu02.service.ApiService

class DogViewModel : ViewModel(){
    val apiService = ApiService.getInstance()

    private var _dogList = mutableStateOf<List<Dog>>(emptyList())
    var dogList : State<List<Dog>> = _dogList

    fun getAllDogs(){
        viewModelScope.launch {
            try {
                _dogList.value = apiService.getAllDogs()

                if(_dogList.value.isNotEmpty()){
                    println("Dog list: ${_dogList.value}")
                } else {
                    println("Dog list is empty")
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun addDog(dog: Dog){
        viewModelScope.launch {
            try {
                val response = apiService.addDog(dog)
                if(response.isSuccessful){
                    println("Dog added successfully: ${response.body()}")
                    getAllDogs()
                } else {
                    println("Failed to add dog: ${response.errorBody()}")
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun deleteDog(id: String){
        viewModelScope.launch {
            try {
                val response = apiService.deleleDog(id)

                if(response.isSuccessful){
                    println("Dog deleted successfully: ${response.body()}")
                    getAllDogs()
                } else {
                    println("Failed to delete dog: ${response.errorBody()}")
                }

            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun updateDog(id: String, dog: Dog){
        viewModelScope.launch {
            try {
                val response = apiService.updateDog(id, dog)

                if(response.isSuccessful){
                    println("Dog updated successfully: ${response.body()}")
                    getAllDogs()
                } else {
                    println("Failed to update dog: ${response.errorBody()}")
                }

            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}