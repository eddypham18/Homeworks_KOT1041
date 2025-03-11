package thai.phph48495.lab1ph48495

fun main() {
    //val có thể không cần biến khởi tạo ban đầu, nhưng khi đã có giá trị thì không thể thay đổi
    var a: String = "initial"
    println(a)
    val b: Int = 1
    val c :Int


    //Khi sử dụng từ khóa var thì bắt buộc phải khởi tạo biến
    var e: Int
//    println(e)

    val d: Int

    if (true) {
        d = 1
    } else {
        d = 2
    }

    println(d)
}