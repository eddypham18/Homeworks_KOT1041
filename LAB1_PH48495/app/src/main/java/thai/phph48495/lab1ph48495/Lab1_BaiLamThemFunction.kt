package thai.phph48495.lab1ph48495

fun main() {
    //Default Parameter Values and Named Arguments
//    printMessage("Hello")
//    printMessageWithPrefix("Hello", "Log")
//    printMessageWithPrefix("Hello")
//    printMessageWithPrefix(prefix = "Log", message = "Hello")
//    println(sum(1, 2))
//    println(multiply(2, 4))
    //========================================================================

    /*Infix Functions
    Hàm phải là một hàm thành viên hoặc một hàm extension.
    Hàm phải có đúng một tham số.
    Hàm không được sử dụng dấu ngoặc đơn khi gọi.

    Sử dụng infix khi bạn muốn định nghĩa các hàm biểu diễn mối quan hệ hoặc hành động ngắn gọn giữa hai đối tượng, đặc biệt khi hàm chỉ có một tham số đầu vào.
     */

//    infix fun Int.times(str: String) = str.repeat(this)
//    println(2 times "Bye ")
//
//    val pair = "Ferrari" to "Katrina"
//    println(pair)
//
//    infix fun String.onto(other: String) = Pair(this, other)
//    val myPair = "McLaren" onto "Lucas"
//    println(myPair)
//
//    val sophia = Person("Sophia")
//    val claudia = Person("claudia")
//
//    sophia likes claudia
//    println(sophia.likedPeople.map{it.name})
    //========================================================================

    /*
    *   Operator Functions
    *
    * Sử dụng operator khi bạn muốn định nghĩa hoặc tùy chỉnh các toán tử toán học, so sánh, hoặc truy cập cho các đối tượng của lớp tự định nghĩa.
    * */

//    operator fun Int.times(str: String) = str.repeat(this)
//    println(2 * "Bye ")
//
//    operator fun String.get(range: IntRange) = substring(range)
//    val str = "Always forgive your enemies; nothing annoys them so much."
//    println(str[0..14])
    //========================================================================

    /*
    * Functions with vararg Parameters
    *
    * Varargs allow you to pass any number of arguments by separating them with commas.
    * */

//    fun printAll(vararg messages: String) {
//        for (m in messages) println(m)
//    }
//    printAll("Hello", "Hallo", "Salut", "Hola", "你好")
//
//    fun printAllWithPrefix(vararg messages: String, prefix: String) {
//        for (m in messages) println(prefix + m)
//    }
//    printAllWithPrefix(
//        "Hello", "Hallo", "Salut", "Hola", "你好",
//        prefix = "Greeting: "
//    )
//
//    //spread operator
//    fun log(vararg entries: String) {
//        printAll(*entries)
//    }
//    log("Hello", "Hallo", "Salut", "Hola", "你好")

}


/*
*   Default Parameter Values and Named Arguments
* */
//fun printMessage(message: String) : Unit{
//    println(message)
//}
//
//fun printMessageWithPrefix(message: String, prefix: String = "Info"){
//    println("[$prefix] $message")
//}
//
//fun sum(x: Int, y : Int): Int {
//    return x+y
//}
//
//fun multiply(x: Int, y: Int) = x*y
//========================================================================


/*
* Infix Functions
* */

class Person(val name: String) {
    val likedPeople = mutableListOf<Person>()
    infix fun likes(other: Person) { likedPeople.add(other) }
}