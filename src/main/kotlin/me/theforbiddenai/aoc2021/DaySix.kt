package me.theforbiddenai.aoc2021

import java.io.File

class DaySix {

    private val input = getInput()

    // Answer 358214
    fun challengeOne() {
        for (day in 0..79) {
            input.replaceAll { it - 1 }
            val reset = input.count { it == -1 }

            input.replaceAll { if (it == -1) 6 else it }

            input.addAll(MutableList(reset) { 8 })
            println(day)
        }
        println(input.size)
    }

    // Answer 1622533344325
    fun challengeTwo() {
        var map = mutableMapOf<Int, Long>()

        input.forEach {
            val value = (map[it] ?: 0) + 1
            map[it] = value
        }

        for (day in 0..255) {

            val x = mutableMapOf<Int, Long>()
            map.forEach { (key, value) ->
                val isNegative = (key - 1) == -1
                val newKey = if (isNegative) 6 else key - 1

                if (isNegative) {
                    x[8] = value
                }

                val cValue = x[newKey] ?: 0
                x[newKey] = cValue + value
            }
            map = x
        }

        println(map.values.sum())
    }

    private fun getInput(): MutableList<Int> {
        val text = File("src/main/resources/input/day 6.txt").readText()
        return text.split(",").map { it.toInt() }.toMutableList()
    }

}