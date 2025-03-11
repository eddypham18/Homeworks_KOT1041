package thai.phph48495.lab1ph48495

fun main() {
    println("Chương trình tính tổng, hiệu, tích và thương")
    println("============================================")
    print("Nhập số a: ")
    var a : Double= 0.0
    try {
        a = readln().toDouble()
    } catch (e: NumberFormatException) {
        println("Số a không hợp lệ")
        return
    }

    var b : Double = 0.0
    try {
        print("Nhập số b: ")
        b = readln().toDouble()
    } catch (e: NumberFormatException) {
        println("Số b không hợp lệ")
        return
    }


    println("Tổng của $a và $b là: ${a + b}")
    println("Hiệu của $a và $b là: ${a - b}")
    println("Tích của $a và $b là: ${a * b}")
    println("Thương của $a và $b là: ${a / b}")

}