package me.theforbiddenai.aoc2021

import java.io.File

// Welcome to over-engineered bingo! lmao
class DayFour {

    private val input = getInput()
    private val numbers = getNumbers()
    private val bingoCards = getBingoCards()

    // Answer 49686
    fun challengeOne() {
        val bingo = bingoCards.minByOrNull { it.simulateGame(numbers) } ?: bingoCards[0]
        println(bingo.getUnmarkedNumbers(numbers).sum() * bingo.getLastNumber(numbers))
    }

    // Answer 26878
    fun challengeTwo() {
        val bingo = bingoCards.maxByOrNull { it.simulateGame(numbers) } ?: bingoCards[0]
        println(bingo.getUnmarkedNumbers(numbers).sum() * bingo.getLastNumber(numbers))
    }

    private class BingoCard(val rows: List<String>) {

        fun getColumn(col: Int): List<Int> {
            return rows.mapNotNull { it.split(Regex("\\s+"))[col].toIntOrNull() }
        }

        fun getRow(row: Int): List<Int> {
            return rows[row].split(Regex("\\s+")).mapNotNull { it.toIntOrNull() }
        }

        fun simulateGame(numbers: List<Int>): Int {
            val indexList = mutableListOf<Int>()
            for (i in 0 until 5) {
                indexList.add(getHighestIndex(getColumn(i), numbers))
                indexList.add(getHighestIndex(getRow(i), numbers))
            }
            return indexList.minOrNull() ?: -1
        }

        fun getWinningCombination(numbers: List<Int>): List<Int> {
            val list = mutableMapOf<List<Int>, Int>()
            for (i in 0 until 5) {
                list[getColumn(i)] = getHighestIndex(getColumn(i), numbers)
                list[getRow(i)] = getHighestIndex(getRow(i), numbers)
            }

            return list.filter { (_, value) -> value == list.minOf { it.value } }.keys.first()
        }

        fun getUnmarkedNumbers(numbers: List<Int>): List<Int> {
            val winner = getWinningCombination(numbers)
            val highestIndex = getHighestIndex(winner, numbers)
            val bingoNumbers = rows.joinToString(" ").split(Regex("\\s+"))

            return numbers.filter { numbers.indexOf(it) > highestIndex }
                .filter { bingoNumbers.contains(it.toString()) }
        }

        fun getLastNumber(numbers: List<Int>): Int {
            val winner = getWinningCombination(numbers)
            return numbers[getHighestIndex(winner, numbers)]
        }

        private fun getHighestIndex(input: List<Int>, numbers: List<Int>): Int {
            return input.maxOfOrNull { numbers.indexOf(it) } ?: -1
        }

    }

    private fun getNumbers(): List<Int> {
        return input[0].split(",").mapNotNull { it.replace(Regex("\\D+"), "").toIntOrNull() }
    }

    private fun getBingoCards(): List<BingoCard> {
        return input.filter { it.isNotBlank() && !it.contains(",") }
            .map { it.trim() }
            .windowed(5, step = 5)
            .map { BingoCard(it) }
    }

    private fun getInput(): List<String> {
        return File("src/main/resources/input/day 4.txt").readLines()
    }
}