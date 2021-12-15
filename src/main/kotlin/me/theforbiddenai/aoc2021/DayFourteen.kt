package me.theforbiddenai.aoc2021

import java.io.File

class DayFourteen {

    private var polymerTemplate = ""
    private var insertionRules = mutableMapOf<String, String>()

    init {
        initializeInput()
    }

    // Answer 2740
    fun challengeOne() {
        var polymerArray = polymerTemplate.toCharArray()
        for (i in 0..9) {
            polymerArray = executeStepCharArray(polymerArray)
        }

        val count = polymerArray.groupBy { it }.map { it.key to it.value.size }
        println(count.maxOf { it.second } - count.minOf { it.second })
    }

    // Answer 2959788056211
    fun challengeTwo() {
        var polymerMap = polymerTemplate.windowed(2).groupBy { it }
            .map { it.key to it.value.size.toLong() }
            .toMap()
            .toMutableMap()

        for (i in 0..39) {
            polymerMap = executeStepUsingMap(polymerMap)
        }

        val countMap = countLetters(polymerMap)
        println(countMap.maxOf { it.value } - countMap.minOf { it.value })
    }

    // I figured from the start that I needed to use a map due to day 6, but I wanted to see if this would work as well
    // at reasonable speeds. Given that executeStepUsingMap() exists, it did not for part 2 lmao
    private fun executeStepCharArray(charArray: CharArray): CharArray {
        val storage = charArray.toMutableList()
        for (index in charArray.indices) {
            val left = charArray[index]
            val right = charArray.getOrNull(index + 1) ?: break

            val pair = "$left$right"
            val insert = insertionRules[pair] ?: ""

            val insertAt = storage.size - charArray.size + index + 1
            storage.add(insertAt, insert[0])
        }

        return storage.toCharArray()
    }

    private fun executeStepUsingMap(map: MutableMap<String, Long>): MutableMap<String, Long> {
        val newMap = mutableMapOf<String, Long>()
        map.forEach { (pair, amount) ->
            val insert = insertionRules[pair]
            val leftPair = "${pair[0]}$insert"
            val rightPair = "$insert${pair[1]}"

            newMap[leftPair] = newMap.getOrElse(leftPair) { 0L } + amount
            newMap[rightPair] = newMap.getOrElse(rightPair) { 0L } + amount
        }

        return newMap
    }

    private fun countLetters(map: MutableMap<String, Long>): MutableMap<Char, Long> {
        val countMap = mutableMapOf<Char, Long>()
        var isFirst = true
        map.forEach { (pair, amount) ->
            if (isFirst) {
                countMap[pair[0]] = amount
                isFirst = false
            }

            countMap[pair[1]] = countMap.getOrElse(pair[1]) { 0L } + amount
        }

        return countMap
    }


    private fun initializeInput() {
        val text = File("src/main/resources/input/day 14.txt").readLines()
        text.filter { it.isNotBlank() }
            .map { it.split("->") }
            .forEach {
                if (it.size == 1) {
                    polymerTemplate = it[0]
                    return@forEach
                }

                insertionRules[it[0].trim()] = it[1].trim()
            }
    }

}