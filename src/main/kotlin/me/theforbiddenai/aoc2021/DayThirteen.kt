package me.theforbiddenai.aoc2021

import java.io.File
import kotlin.math.abs

class DayThirteen {

    private val coordList = mutableListOf<Coordinate>()
    private val instructionList = mutableListOf<String>()

    init {
        initializeInputLists()
    }

    // Answer 675
    fun challengeOne() {
        val instructionArgs = instructionList[0].split("=")
        val isX = instructionArgs[0] == "x"
        val value = instructionArgs[1].toInt()
        val foldedMap = coordList.map { fold(it, value, isX) }.distinct()

        println(foldedMap.size)
    }

    // Answer HZKHFEJZ
    fun challengeTwo() {
        var foldedList = coordList.toList()
        instructionList.map { it.split("=") }
            .forEach { (axis, valueStr) ->
                val isX = axis == "x"
                val value = valueStr.toInt()
                foldedList = foldedList.map { fold(it, value, isX) }
            }

        displayList(foldedList.distinct())
    }

    private fun displayList(list: List<Coordinate>) {
        val map = mutableMapOf<Int, MutableList<Int>>()

        list.sortedBy { it.y }.forEach {
            val currentList = map.getOrElse(it.y) { mutableListOf() }
            currentList.add(it.x)
            map[it.y] = currentList
        }

        val maxDots = map.maxOf { it.value.maxOrNull() ?: 0 } + 1

        map.forEach { (_, xList) ->
            val printOut = ".".repeat(maxDots).toCharArray()
            xList.forEach { printOut[it] = '#' }

            println(String(printOut))
        }
    }

    private fun fold(coord: Coordinate, value: Int, isX: Boolean): Coordinate {
        if ((isX && coord.x < value) || (!isX && coord.y < value)) return coord

        val diff = (if (isX) coord.x else coord.y) - value
        val newValue = if (diff <= 0) value + abs(diff) else value - abs(diff)

        return Coordinate(if (isX) newValue else coord.x, if (isX) coord.y else newValue)
    }

    data class Coordinate(val x: Int, val y: Int)

    private fun initializeInputLists() {
        val text = File("src/main/resources/input/day 13.txt").readLines()
        text.filter { it.isNotBlank() }
            .map { it.substringAfterLast(" ") }
            .forEach {
                if (!it.contains(",")) {
                    instructionList.add(it)
                    return@forEach
                }

                val args = it.split(",")
                coordList.add(Coordinate(args[0].toInt(), args[1].toInt()))
            }
    }

}