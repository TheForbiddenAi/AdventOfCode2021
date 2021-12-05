package me.theforbiddenai.aoc2021

import java.io.File

class DayOne {

    private val numbers = getInput()

    // Shortly after I wrote this monstrosity I discovered the windowed function rip

    // Answer: 1292
    fun challengeOne() {
        var count = 0

        // for some reason numbers.indices wasn't working correctly?!?!?!?
        for (i in 0 until numbers.size) {
            if (i == 0) continue
            val num = numbers[i]
            val prev = numbers[i - 1]

            if (num > prev) count++
        }

        println(count)
    }

    // Answer 1262
    fun challengeTwo() {
        var window = 0
        var windowSub = 0
        var windowTotal = 0

        var windowPrevCount = 0

        var countInc = 0
        var index = 0
        while (index < numbers.size) {
            val num = numbers[index]

            windowTotal += num
            windowSub++


            if (windowSub == 3) {
                if (window != 0 && windowTotal > windowPrevCount) {
                    countInc++
                }

                windowPrevCount = windowTotal
                window++

                windowTotal = 0
                windowSub = 0

                index -= 1
            } else {
                index++
            }

        }

        println(countInc)
    }

    private fun getInput(): List<Int> {
        return File("src/main/resources/input/day 1.txt")
            .readLines()
            .map { it.toInt() }
            .toMutableList()
    }

}