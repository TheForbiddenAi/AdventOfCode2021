package me.theforbiddenai.aoc2021

import java.io.File
import kotlin.math.max

class DayFifteen {

    // Answer 423
    fun challengeOne() {
        val dataMap = calculateDistances(getInput())
        val list = toCoordList(getInput())
        println(dataMap[list.last()]!!.first)
    }

    // Answer is 2778
    fun challengeTwo() {
        val dataMap = calculateDistances(extendedGrid())
        val list = toCoordList(extendedGrid())
        println(dataMap[list.last()]!!.first)
    }

    private fun calculateDistances(input: List<String>): Map<Coordinate, Pair<Int, Coordinate>> {
        val coordList = toCoordList(input)

        val dataMap = mutableMapOf<Coordinate, Pair<Int, Coordinate>>()
        dataMap[coordList.first()] = Pair(0, coordList.first())

        val start = coordList.first()
        val set = mutableSetOf(start)

        while (set.isNotEmpty()) {
            val coord = set.first()
            val currentRisk = dataMap[coord]!!.first

            val adjacent = coord.getAdjacent(input)
            adjacent.forEach {

                val risk = currentRisk + it.risk
                var dataPair = dataMap[it]

                if (dataPair != null && risk < dataPair.first || dataPair == null) {
                    dataPair = Pair(risk, coord)
                    dataMap[it] = dataPair
                    set.add(it)
                }

            }
            set.remove(coord)
        }

        return dataMap
    }

    // x = charIndex, y = rowIndex (I switched it from previous iterations of the class in other files because it makes more sense this way lmao)
    data class Coordinate(val x: Int, val y: Int, val risk: Int) {

        // filter is too expensive
        fun getAdjacent(input: List<String>): List<Coordinate> {
            val list = mutableListOf<Coordinate>()
            if (y + 1 < input.size) list.add(Coordinate(x, y + 1, input[y + 1][x].digitToInt()))
            if (y - 1 >= 0) list.add(Coordinate(x, y - 1, input[y - 1][x].digitToInt()))
            if (x + 1 < input[y].length) list.add(Coordinate(x + 1, y, input[y][x + 1].digitToInt()))
            if (x - 1 >= 0) list.add(Coordinate(x - 1, y, input[y][x - 1].digitToInt()))
            return list
        }

    }

    private fun extendedGrid(): List<String> {
        val text = File("src/main/resources/input/day 15.txt").readLines()

        var extendedRows = ""
        text.forEach { row ->
            for (horizontalIncrement in 0..4) {
                extendedRows += extend(row, horizontalIncrement)
            }
            extendedRows += "\n"
        }

        val rows = extendedRows.split("\n").filter { it.isNotBlank() }

        var finalStr = ""
        for (verticalIncrement in 0..4) {
            finalStr += rows.joinToString("") { "${extend(it, verticalIncrement)}\n" }
        }

        return finalStr.split("\n").filter { it.isNotBlank() }
    }

    private fun extend(row: String, increment: Int): String {
        var extended = ""
        row.forEach { char ->
            val new = char.digitToInt() + increment
            extended += if (new > 9) max(1, new - 9) else new
        }

        return extended
    }

    private fun getInput(): List<String> {
        return File("src/main/resources/input/day 15.txt").readLines()
    }

    private fun toCoordList(input: List<String>): List<Coordinate> {
        val coordList = mutableListOf<Coordinate>()

        input.indices.forEach { rowIndex ->
            val row = input[rowIndex]
            row.indices.forEach { riskIndex ->
                val risk = if (rowIndex == 0 && riskIndex == 0) 0 else row[riskIndex].digitToInt()
                coordList.add(Coordinate(riskIndex, rowIndex, risk))
            }
        }

        return coordList
    }

}