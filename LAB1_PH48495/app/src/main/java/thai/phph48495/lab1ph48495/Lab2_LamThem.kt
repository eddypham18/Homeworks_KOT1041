package thai.phph48495.lab1ph48495

fun bai1(maybeString: String?): String{
    if (maybeString != null && maybeString.length > 0) {
        return "String of length ${maybeString.length}"
    } else {
        return "Empty or null string"
    }
}
fun bai2() {
    bai2_2("Hello")
    bai2_2(1)
    bai2_2(0L)
    bai2_2(MyClass())
    bai2_2("hello")
}

fun bai2_2(obj: Any) {
    when (obj) {
        1 -> println("One")
        "Hello" -> println("Greeting")
        is Long -> println("Long")
        !is String -> println("Not a string")
        else -> println("Unknown")
    }
}
class MyClass
fun bai3() {
    println(whenAssign("Hello"))
    println(whenAssign(3.4))
    println(whenAssign(1))
    println(whenAssign(MyClass1()))
}

fun whenAssign(obj: Any): Any {
    val result = when (obj) {
        1 -> "one"
        "Hello" -> 1
        is Double -> false
        else -> 42
    }
    return result
}
class MyClass1
fun bai4(){
    val cakes = listOf("carrot", "cheese", "chocolate")

    for (cake in cakes) {
        println("Yummy, it's a $cake cake!")
    }
}
fun eatACake() = println("Eat a Cake")
fun bakeACake() = println("Bake a Cake")
fun bai5(args: Array<String>) {
    var cakesEaten = 0
    var cakesBaked = 0

    while (cakesEaten < 5) {
        eatACake()
        cakesEaten ++
    }

    do {
        bakeACake()
        cakesBaked++
    } while (cakesBaked < cakesEaten)

}
class Animal(val name: String)

class Zoo(val animals: List<Animal>) {
    operator fun iterator(): Iterator<Animal> {
        return animals.iterator()
    }
}

fun bai6() {
    val zoo = Zoo(listOf(Animal("zebra"), Animal("lion")))
    for (animal in zoo) {
        println("Watch out, it's a ${animal.name}")
    }

}

fun bai7(){
    for(i in 0..3) {
        println(i)
    }
    println(" ")

    for(i in 0 until 3) {
        println(i)
    }
    println(" ")
    for(i in 2..8 step 2) {
        println(i)
    }
    println(" ")

    for (i in 3 downTo 0) {
        println(i)
    }
    println(" ")
}
fun bai8(){
    for (c in 'a'..'d') {
        println(c)
    }
    println(" ")

    for (c in 'z' downTo 's' step 2) {
        println(c)
    }
    println(" ")
}
fun bai9(){
    val x = 2
    if (x in 1..5) {
        println("x is in range from 1 to 5")
    }
    println()

    if (x !in 6..10) {
        println("x is not in range from 6 to 10")
    }
}
fun main(){
    println("=".repeat(20) + "Bai 1" + "=".repeat(20))
    println(bai1(null))
    println(bai1(""))
    println(bai1("Kotlin"))
    println("=".repeat(20)+"Bai 2"+"=".repeat(20))
    bai2()
    println("=".repeat(20) +"Bai 3"+"=".repeat(20) )
    bai3()
    println("=".repeat(20) +"Bai 4"+"=".repeat(20) )
    bai4()
    println("=".repeat(20) +"Bai 5"+"=".repeat(20) )
    bai5(emptyArray())
    println("=".repeat(20) +"Bai 6"+"=".repeat(20) )
    bai6()
    println("=".repeat(20) +"Bai 7"+"=".repeat(20) )
    bai7()
    println("=".repeat(20) +"Bai 8"+"=".repeat(20) )
    bai8()
    println("=".repeat(20) +"Bai 9"+"=".repeat(20) )
    bai9()

}