package thaiph.ph48495.demo.service

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import thaiph.ph48495.demo.models.Food

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

    fun deleteFood(id : Int){
        viewModelScope.launch {
            try {
                RetrofitInstance.api.deleteFood(id)
            }catch (e:Exception){
                Log.d("===",e.message.toString())
            }
        }
    }
}