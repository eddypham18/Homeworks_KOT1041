package thaiph.ph48495.demo.service

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import thaiph.ph48495.demo.models.Food
import kotlin.math.log

class ViewModelApp:ViewModel() {
    //lay du lieu data food tu server
    private val _listProduct = mutableStateOf<List<Food>?>(null)
    var listFood: State<List<Food>?> = _listProduct
    fun getAllFoods(){
        viewModelScope.launch {
            try {
                _listProduct.value = RetrofitInstance.api.getAllFoods()
            }catch (e:Exception){
                Log.d("===",e.message.toString())
            }
        }
    }

    fun deleteFood(id : String){
        viewModelScope.launch {
            try {
                val res = RetrofitInstance.api.deleteFood(id)
                if(res.isSuccessful){
                    getAllFoods()
                }
            }catch (e:Exception){
                Log.d("===",e.message.toString())
            }
        }
    }

    fun editFood(id: String, food: Food){
        viewModelScope.launch {
            try {
                val res = RetrofitInstance.api.editFood(id, food)
                if(res.isSuccessful){
                    getAllFoods()
                }
            }catch (e:Exception){
                Log.d("===",e.message.toString())
            }
        }
    }
}