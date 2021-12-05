package me.theforbiddenai.aoc2021

import java.io.File

class DayFive {

    private val lines = getInput()

    // Answer 7438
    fun challengeOne() {
        val validLines = lines.toMutableList()
        validLines.removeIf { it.start.x != it.end.x && it.start.y != it.end.y }

        println(findOverlaps(validLines).filter { (_, value) -> value > 1 }.size)
    }

    // Answer 21406
    fun challengeTwo() {
        println(findOverlaps(lines).filter { (_, value) -> value > 1 }.size)
    }

    private fun findOverlaps(lines: List<CoordinateLine>): MutableMap<Coordinate, Int> {
        val coordOccurrenceMap = mutableMapOf<Coordinate, Int>()
        val allCoords = mutableListOf<Coordinate>()

        lines.map { it.coordList }.forEach { allCoords.addAll(it) }

        allCoords.forEach {
            var count = coordOccurrenceMap[it] ?: 0
            coordOccurrenceMap[it] = ++count
        }

        return coordOccurrenceMap
    }

    private data class Coordinate(val x: Int, val y: Int)

    private data class CoordinateLine(val start: Coordinate, val end: Coordinate) {

        val coordList = mutableListOf<Coordinate>()

        init {
            initCoords()
        }

        private fun initCoords() {
            val deltaX = (end.x - start.x)
            if (deltaX == 0) {
                val range = if (end.y > start.y) start.y..end.y else end.y..start.y
                for (y in range) {
                    val coord = Coordinate(start.x, y)
                    coordList.add(coord)
                }
                return
            }

            val slope = (end.y - start.y) / deltaX
            val range = if (end.x > start.x) start.x..end.x else end.x..start.x

            for (x in range) {
                // y - y1 = k(x - x1) - Gotta love point-slope form
                // y = k(x - x1) + y1
                val y = slope * (x - start.x) + start.y
                if (isBetween(y, start.y, end.y)) {
                    val coord = Coordinate(x, y)
                    coordList.add(coord)
                }
            }
        }

        private fun isBetween(input: Int, start: Int, end: Int): Boolean {
            return input in start..end || input in end..start
        }

    }

    private fun getInput(): List<CoordinateLine> {
        val text = File("src/main/resources/input/day 5.txt").readLines()

        val coordinateLines = mutableListOf<CoordinateLine>()

        text.map { it.split("->") }
            .forEach { (pairOne, pairTwo) ->
                val oneArgs = pairOne.trim().split(",").mapNotNull { it.toIntOrNull() }
                val twoArgs = pairTwo.trim().split(",").mapNotNull { it.toIntOrNull() }

                val start = Coordinate(oneArgs[0], oneArgs[1])
                val end = Coordinate(twoArgs[0], twoArgs[1])
                coordinateLines.add(CoordinateLine(start, end))
            }

        return coordinateLines
    }

}