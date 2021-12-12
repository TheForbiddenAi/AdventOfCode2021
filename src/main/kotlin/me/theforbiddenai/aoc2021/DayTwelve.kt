package me.theforbiddenai.aoc2021

import java.io.File

class DayTwelve {

    private val input = getInput()
    private val paths = mutableListOf<MutableList<Cave>>()

    // Answer 3802
    fun challengeOne() {
        input.filter { it.isStart }.map { calculatePath(it, canVisitedTwice = false) }
        println(paths.distinct().size)
    }

    // Answer 99448
    fun challengeTwo() {
        input.filter { it.isStart }.map { calculatePath(it, canVisitedTwice = true) }
        println(paths.distinct().size)
    }

    private fun calculatePath(
        cave: Cave,
        path: MutableList<Cave> = mutableListOf(),
        canVisitedTwice: Boolean,
        visitedTwice: Boolean = false
    ): MutableList<Cave> {
        var visited = visitedTwice
        if (cave.isSmallCave && path.contains(cave)) {
            if (visitedTwice || !canVisitedTwice) return mutableListOf()
            visited = true
        }
        val newPath = path.toMutableList()
        newPath.add(cave)

        if (cave.isEnd) {
            paths.add(newPath)
        }

        cave.connections.forEach { calculatePath(it, newPath, canVisitedTwice, visited) }

        return path

    }

    private fun getInput(): List<Cave> {
        val text = File("src/main/resources/input/day 12.txt").readLines()

        val caveMap = mutableMapOf<String, Cave>()
        text.sortedByDescending { it.length } // end/start connections will always be at the top
            .map { it.split("-") }
            .map { it.sortedBy { list -> list.length } } // end/start will always be last item
            .forEach {
                val caveOneName = it[0]
                val caveTwoName = it[1]

                if (caveTwoName.length > 2) {
                    val caveOne = caveMap.getOrElse(caveOneName) { Cave(caveOneName) }

                    caveOne.isStart = caveTwoName.length == 5 || caveOne.isStart
                    caveOne.isEnd = caveTwoName.length == 3 || caveOne.isEnd

                    caveMap[caveOneName] = caveOne
                } else {
                    val caveOne = caveMap.getOrElse(caveOneName) { Cave(caveOneName) }
                    val caveTwo = caveMap.getOrElse(caveTwoName) { Cave(caveTwoName) }

                    caveOne.connections.add(caveTwo)
                    caveTwo.connections.add(caveOne)

                    caveMap[caveOneName] = caveOne
                    caveMap[caveTwoName] = caveTwo
                }
            }

        return caveMap.values.toList()
    }

    private class Cave(
        val name: String,
        var isStart: Boolean = false,
        var isEnd: Boolean = false,
        val connections: MutableList<Cave> = mutableListOf()
    ) {
        val isSmallCave = name.equals(name.lowercase(), false)

        override fun toString(): String {
            return name
        }
    }

}