package thaiph.ph48495.demo.models

import android.os.Parcelable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Food(val id:Int, val name:String, val avatar:String, val price:String) : Parcelable
