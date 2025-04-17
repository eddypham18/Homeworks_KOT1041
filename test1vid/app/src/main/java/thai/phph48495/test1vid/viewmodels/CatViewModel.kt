package thai.phph48495.test1vid.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import thai.phph48495.test1vid.models.Cat
import thai.phph48495.test1vid.service.ApiService
import kotlin.random.Random

class CatViewModel : ViewModel() {

    val TAG : String = "zzzzzzzzzzz"

    val catViewModel = ApiService.getInstance()

    private var _listCat = mutableStateOf<List<Cat>>(emptyList())
    var listCat : State<List<Cat>> = _listCat

    fun getAllCats(){
        viewModelScope.launch {
            try {
                val orginalList = catViewModel.getAllCats()
                if(orginalList.isNotEmpty()){
                    val newList = orginalList.map { cat ->
                        cat.copy(
                            name = "Meo "+ Random.nextInt(0,100).toString(),
                            image = "https://cataas.com/cat/${cat.id}",
                        )
                    }

                    _listCat.value = newList
                    Log.d(TAG, "getAllCats: " + _listCat.value)
                } else {
                    Log.d(TAG, "getAllCats: " + "List is empty")
                }

            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}