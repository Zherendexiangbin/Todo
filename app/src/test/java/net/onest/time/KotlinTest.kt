package net.onest.time

import org.junit.Assert
import org.junit.Test

class StringDelegator(var str: String = "haha") {
}

data class Money(val amount: Double) {
    operator fun plus(increment: Money) = Money(amount + increment.amount)

    infix fun add(x: Money) = this + x

    fun printTimes() {
        fun printThis() {
            println(this)
        }

        repeat(3) {
            printThis()
        }
    }
}

class KotlinTest {
    @Test
    fun returnTest() {
//        listOf(1, 2, 3, 4, 5)
//            .forEach lit@ {
//                if (it == 3) return@lit     // 返回到指定位置
//                println(it)
//            }

        listOf(1, 2, 3, 4, 5)
            .forEach(
                fun (x: Int) {
                    if (x == 3) return
                    println(x)
                }
            )
    }

    @Test
    fun lambdaTest() {
        val strPlusInt: String.(Int) -> String = { num ->  (this.toInt() + num).toString() }
        val intToString: Int.(Int) -> String = { num -> num.toString() }
        val strToInt: (String) -> Int = { str -> str.toInt() }
        val strAdd: (String, String) -> Int = { str1, str2 -> strToInt(str1) + strToInt.invoke(str2) }

        val result = "8888".strPlusInt(2)
        Assert.assertEquals(result, "8890")
        println(result)

        val res1 = intToString.invoke(1, 2)
        val res2 = intToString(1, 2)
        val res3 = 1.intToString(2)
        println(res1)
        println(res2)
        println(res3)

        Assert.assertEquals(1100, strAdd("999", "101"))
        println(strAdd("999", "101"))
    }

    @Test
    fun innerFunction() {
        Money(7.62).printTimes()
    }

    @Test
    fun operatorTest() {
        val money1 = Money(6.5)
        val money2 = Money(7.2)

        Assert.assertEquals(money1 + money2, Money(13.7))
        Assert.assertEquals(money1 add money2, Money(13.7))
        println("sum: ${money1 + money2}")
    }

    @Test
    fun byTest() {
    }
}