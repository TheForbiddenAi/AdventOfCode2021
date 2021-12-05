package me.theforbiddenai.aoc2021

import java.io.File

class DayTwo {

    private val instructions = getInput()

    // Answer: 1524750
    fun challengeOne() {
        var depth = 0
        var horizontal = 0

        instructions.forEach { (direction, num) ->
            when (direction) {
                "forward" -> horizontal += num
                "up" -> depth -= num
                "down" -> depth += num
            }
        }

        println("$depth * $horizontal = ${depth * horizontal}")

    }

    // Answer: 1592426537
    fun challengeTwo() {
        var aim = 0
        var depth = 0
        var horizontal = 0

        instructions.forEach { (direction, num) ->
            when (direction) {
                "forward" -> {
                    horizontal += num
                    depth += num * aim
                }
                "up" -> aim -= num
                "down" -> aim += num
            }
        }

        println("$depth * $horizontal (Aim: $aim) = ${depth * horizontal}")
    }

    private fun getInput(): List<Pair<String, Int>> {
        return File("src/main/resources/input/day 2.txt").readLines()
            .map {
                val args = it.split(Regex("\\s+"))
                val num = args[1].toInt()
                args[0] to num
            }
    }

}