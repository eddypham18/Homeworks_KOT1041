package thaiph.ph48495.demo

fun main() {
//    val a = 10
//    val b = 2
//    var ucln = 1
//    val min = if(a>b) b else a
//
//    for (i in min downTo 1){
//        if(a%2==0 && b%2==0){
//            ucln = i
//            break
//        }
//    }
//    print("Uoc chung lon nhat cua $a va $b la: $ucln")

    val notNullText: String = "Definitely not null"
    val nullableText1: String? = "Might be null"
    val nullableText2: String? = null

    fun funny(text: String?) {
        if (text != null)
            println(text)
        else
            println("Nothing to print :(")
    }

    fun funnier(text: String?) {
        val toPrint = text ?: "Nothing to print :("
        println(toPrint)
    }

    funnier(null)

    var listFood = listOf("pho bo", "pho ga", "com rang", "bun dau")
    var listPrice = listOf(30,45,50,60)

    myLable@ for((index, item) in listFood.withIndex()){
        println(item)
        for (price in listPrice){
            println(price)
            if(price == 45) break@myLable
            else continue@myLable
        }
    }
}