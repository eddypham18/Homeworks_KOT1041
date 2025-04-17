package thai.phph48495.dethi2.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import thai.phph48495.dethi2.models.Car
import thai.phph48495.dethi2.service.ApiService

class CarViewModel : ViewModel(){

    val TAG : String = "zzzzzzzz"

    val apiService = ApiService.getInstance()
    private var _carList = mutableStateOf<List<Car>>(emptyList())
    var carList : State<List<Car>> = _carList



    fun getAllCars(){
        viewModelScope.launch {
            try {
                _carList.value = apiService.getAllCars()
                if(_carList.value.isNotEmpty()){
                    Log.d(TAG, "getAllCars: ${_carList.value}")
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun addCar(car : Car, navController: NavController){
        viewModelScope.launch {
            try {
                val response = apiService.addCar(car)

                if(response.isSuccessful){
                    getAllCars()
                    navController.navigate("home")
                } else {
                    Log.d(TAG, "addCar: ${response.errorBody()}")
                }

            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun deleteCar(id: String){
        viewModelScope.launch {
            try {
                val response = apiService.deleteCar(id)
                if(response.isSuccessful){
                    getAllCars()
                } else {
                    Log.d(TAG, "deleteCar: ${response.errorBody()}")
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun updateCar(id: String, car: Car, navController: NavController){
        viewModelScope.launch {
            try {
                val response = apiService.updateCar(id, car)
                if(response.isSuccessful){
                    getAllCars()
                    navController.navigate("home")
                } else {
                    Log.d(TAG, "updateCar: ${response.errorBody()}")
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

}