package me.theforbiddenai.aoc2021

import java.io.File
import kotlin.math.ceil

class DayTen {

    private val input = getInput()
    private val emptyPairRegex = Regex("\\(\\)|\\{}|\\[]|<>")

    // Answer 341823
    fun challengeOne() {
        println(input.sumOf { convertCharToPoints(findFirstIllegalCharacter(it), true) })
    }

    // Answer 2801302861
    fun challengeTwo() {
        val pointList = mutableListOf<Long>()
        input.map { removePairs(it) }
            .filter { findFirstIllegalCharacter(it) == '\u0000' }
            .forEach {
                var missing = ""
                for (index in it.indices.reversed()) {
                    val char = it[index]
                    missing += getClosingChar(char)
                }
                var total: Long = 0
                missing.forEach { ch ->
                    total *= 5
                    total += convertCharToPoints(ch, false)
                }

                pointList.add(total)
            }

        pointList.sort()
        println(pointList[ceil((pointList.size - 1) / 2.0).toInt()])
    }

    private fun removePairs(str: String): String {
        var modifiedStr = str

        while (modifiedStr.contains(emptyPairRegex)) {
            modifiedStr = modifiedStr.replace(emptyPairRegex, "")
        }

        return modifiedStr
    }

    private fun findFirstIllegalCharacter(str: String): Char {
        val pairsRemoved = removePairs(str)
        var nextClosing = '\u0000'

        for (char in pairsRemoved) {
            val previousExpectedClosing = nextClosing
            nextClosing = getClosingChar(char)

            if (nextClosing == '\u0000' && char != previousExpectedClosing) return char
        }

        return '\u0000'
    }

    private fun getClosingChar(char: Char): Char {
        return when (char) {
            '{' -> '}'
            '[' -> ']'
            '(' -> ')'
            '<' -> '>'
            else -> '\u0000'
        }
    }

    private fun convertCharToPoints(char: Char, isIllegal: Boolean): Int {
        return when (char) {
            ')' -> if (isIllegal) 3 else 1
            ']' -> if (isIllegal) 57 else 2
            '}' -> if (isIllegal) 1197 else 3
            '>' -> if (isIllegal) 25137 else 4
            else -> 0
        }
    }

    private fun getInput(): List<String> {
        return File("src/main/resources/input/day 10.txt").readLines()
    }

}