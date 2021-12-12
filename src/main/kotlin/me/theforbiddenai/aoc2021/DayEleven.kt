package me.theforbiddenai.aoc2021

import java.io.File

class DayEleven {

    private val input = getInput()
    private val flashedList = mutableListOf<Coordinate>()

    // Answer 1757
    fun challengeOne() {
        var totalFlashes = 0
        for (step in 0..99) {
            doEnergyLoop()

            totalFlashes += flashedList.size
            flashedList.clear()
        }
        println(totalFlashes)
    }

    // Answer 422
    fun challengeTwo() {
        var step = 0
        while (flashedList.size != 100) {
            flashedList.clear()
            doEnergyLoop()
            step++
        }
        println(step)
    }

    private fun doEnergyLoop() {
        input.keys.indices.forEach { rowIndex ->
            val energyList = input[rowIndex] ?: return@forEach

            energyList.indices.forEach energyLoop@{ energyIndex ->
                val newEnergyLevel = (energyList.getOrNull(energyIndex) ?: return@energyLoop) + 1
                val coord = Coordinate(rowIndex, energyIndex)

                if (newEnergyLevel == 10) flash(coord)
                else if (!flashedList.contains(coord)) energyList[energyIndex] = newEnergyLevel
            }

            input[rowIndex] = energyList
        }
    }

    private fun flash(coord: Coordinate): Int {
        if (flashedList.contains(coord)) return 0

        val energyList = input[coord.x] ?: return 0
        val newValue = (energyList.getOrNull(coord.y) ?: return 0) + 1


        energyList[coord.y] = if (newValue == 10) 0 else newValue
        input[coord.x] = energyList

        if (newValue != 10) return 0

        var flashCount = 1
        flashedList.add(coord)

        flashCount += getAdjacentPoints(coord)
            .sumOf { flash(it) }

        return flashCount
    }


    private fun getAdjacentPoints(coord: Coordinate): List<Coordinate> {
        val x = coord.x
        val y = coord.y
        val coordList = mutableListOf<Coordinate>()

        coordList.add(Coordinate(x + 1, y - 1))
        coordList.add(Coordinate(x + 1, y))
        coordList.add(Coordinate(x + 1, y + 1))

        coordList.add(Coordinate(x, y + 1))
        coordList.add(Coordinate(x, y - 1))

        coordList.add(Coordinate(x - 1, y - 1))
        coordList.add(Coordinate(x - 1, y))
        coordList.add(Coordinate(x - 1, y + 1))

        return coordList.filter { input.getOrElse(it.x) { null }?.getOrNull(it.y) != null }
    }

    data class Coordinate(val x: Int, val y: Int)

    private fun getInput(): MutableMap<Int, MutableList<Int>> {
        val text = File("src/main/resources/input/day 11.txt").readLines()
        val map = mutableMapOf<Int, MutableList<Int>>()

        text.forEach {
            map[map.keys.size] = it.toCharArray().map { ch -> ch.digitToInt() }.toMutableList()
        }

        return map
    }
}