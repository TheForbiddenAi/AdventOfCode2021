package me.theforbiddenai.aoc2021

import java.io.File
import kotlin.math.min

class DayNine {

    private val input = getInput()

    // Answer 465
    fun challengeOne() {
        println(getMinHeightIndices().sumOf { input[it.x][it.y].digitToInt() + 1 })
    }

    // Answer 1269555
    fun challengeTwo() {
        val coordList = getMinHeightIndices()

        val basinSizes = mutableListOf<Int>()
        coordList.forEach { (row, index) ->
            basinSizes.add(calculateBasinSize(row, index))
        }

        basinSizes.sort()
        basinSizes.reverse()
        println(basinSizes[0] * basinSizes[1] * basinSizes[2])
    }

    private fun calculateBasinSize(
        rowIndex: Int,
        charIndex: Int,
        visited: MutableList<Coordinate> = mutableListOf()
    ): Int {
        var sum = 0
        getAdjacentPoints(rowIndex, charIndex).forEach {
            val value = input[it.x][it.y].digitToInt()
            if (value == 9 || visited.contains(it)) return@forEach

            sum++
            visited.add(it)

            sum += calculateBasinSize(it.x, it.y, visited)
        }

        return sum
    }

    private fun getAdjacentPoints(rowIndex: Int, charIndex: Int): List<Coordinate> {
        val coordList = mutableListOf<Coordinate>()
        coordList.add(Coordinate(rowIndex, charIndex + 1))
        coordList.add(Coordinate(rowIndex, charIndex - 1))
        coordList.add(Coordinate(rowIndex + 1, charIndex))
        coordList.add(Coordinate(rowIndex - 1, charIndex))

        return coordList.filter { input.getOrNull(it.x)?.getOrNull(it.y) != null }
    }

    private fun getMinHeightIndices(): List<Coordinate> {
        val indexList = mutableListOf<Coordinate>()
        input.indices.forEach { rowIndex ->
            val row = input[rowIndex]

            row.indices.forEach { charIndex ->
                val char = input[rowIndex][charIndex].digitToInt()
                val adjacentList = getAdjacentPoints(rowIndex, charIndex).map { input[it.x][it.y].digitToInt() }
                val minAdjacent = adjacentList.minOrNull()!!

                val isMin = min(minAdjacent, char) == char && !adjacentList.contains(char)
                if (isMin) indexList.add(Coordinate(rowIndex, charIndex))
            }
        }

        return indexList
    }

    data class Coordinate(val x: Int, val y: Int)

    private fun getInput(): List<String> {
        return File("src/main/resources/input/day 9.txt").readLines()
    }
}