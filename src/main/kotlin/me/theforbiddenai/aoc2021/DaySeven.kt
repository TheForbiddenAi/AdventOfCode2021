package me.theforbiddenai.aoc2021

import java.io.File
import kotlin.math.abs

class DaySeven {

    private val input = getInput()

    // Answer 345035
    fun challengeOne() {
        val fuelMap = mutableMapOf<Int, Int>()


        val max = input.maxOrNull() ?: -1
        val min = input.minOrNull() ?: -1

        for (distance in min..max) {
            fuelMap[distance] = input.sumOf { abs(it - distance) }
        }

        println(fuelMap.minByOrNull { it.value }?.value)
    }

    // Answer 97038163
    fun challengeTwo() {
        val fuelMap = mutableMapOf<Int, Int>()

        val max = input.maxOrNull() ?: -1
        val min = input.minOrNull() ?: -1

        for (distance in min..max) {
            fuelMap[distance] = input.sumOf { calculateFuel(abs(it - distance)) }
        }

        println(fuelMap.minByOrNull { it.value }?.value)
    }

    private fun calculateFuel(n: Int): Int {
        var returnValue = 0
        for (i in 1..n) {
            returnValue += i
        }
        return returnValue
    }

    private fun getInput(): MutableList<Int> {
        val text = File("src/main/resources/input/day 7.txt").readText()
        return text.split(",").map { it.toInt() }.toMutableList()
    }

}