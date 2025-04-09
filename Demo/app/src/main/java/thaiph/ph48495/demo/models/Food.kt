package thaiph.ph48495.demo.models

import android.os.Parcelable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Food(val id:Int, val name:String, val avatar:String, val price:String) : Parcelable

val foodList =  mutableStateListOf<Food>(
    Food(1, "Pizza Margherita", "https://mediawinwin.vn/upload/images/sanpham/bao-gia-chup-mon-an-dich-vu-chup-anh-do-an-chuyen-nghiep-6.JPG", "10.99 $"),
    Food(2, "Bánh", "https://beptueu.vn/hinhanh/tintuc/top-15-hinh-anh-mon-an-ngon-viet-nam-khien-ban-khong-the-roi-mat-1.jpg", "15.99 $"),
    Food(3, "Burger Deluxe", "https://mediawinwin.vn/upload/images/sanpham/bao-gia-chup-mon-an-dich-vu-chup-anh-do-an-chuyen-nghiep-6.JPG", "8.99 $"),
    Food(4, "Bánh", "https://beptueu.vn/hinhanh/tintuc/top-15-hinh-anh-mon-an-ngon-viet-nam-khien-ban-khong-the-roi-mat-1.jpg", "12.99 $"),
    Food(5, "Grilled Salmon", "https://mediawinwin.vn/upload/images/sanpham/bao-gia-chup-mon-an-dich-vu-chup-anh-do-an-chuyen-nghiep-6.JPG", "18.99 $"),
    Food(6, "Bánh", "https://beptueu.vn/hinhanh/tintuc/top-15-hinh-anh-mon-an-ngon-viet-nam-khien-ban-khong-the-roi-mat-1.jpg", "7.99 $"),
    Food(7, "Chocolate Cake", "https://mediawinwin.vn/upload/images/sanpham/bao-gia-chup-mon-an-dich-vu-chup-anh-do-an-chuyen-nghiep-6.JPG", "5.99 $"),
    Food(8, "Bánh", "https://beptueu.vn/hinhanh/tintuc/top-15-hinh-anh-mon-an-ngon-viet-nam-khien-ban-khong-the-roi-mat-1.jpg", "9.99 $"),
    Food(9, "Ramen Bowl", "https://mediawinwin.vn/upload/images/sanpham/bao-gia-chup-mon-an-dich-vu-chup-anh-do-an-chuyen-nghiep-6.JPG", "11.99 $"),
    Food(10, "Bánh", "https://beptueu.vn/hinhanh/tintuc/top-15-hinh-anh-mon-an-ngon-viet-nam-khien-ban-khong-the-roi-mat-1.jpg", "6.99 $")
)