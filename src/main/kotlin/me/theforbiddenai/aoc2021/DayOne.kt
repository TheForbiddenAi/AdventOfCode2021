package me.theforbiddenai.aoc2021

class DayOne {

    private val numbers = getResource()

    // Shortly after I wrote this monstrosity I discovered the windowed function rip

    // Answer: 1292
    fun challengeOne() {
        var count = 0

        // for some reason numbers.indices wasn't working correctly?!?!?!?
        for(i in 0 until numbers.size) {
            if (i == 0) continue
            val num = numbers[i]
            val prev = numbers[i - 1]

            if(num > prev) count++;
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
        while(index < numbers.size) {
            val num = numbers[index]

            windowTotal += num
            windowSub++

            println("$window - $windowSub - $index - $num - $windowTotal")

            if(windowSub == 3) {
                if(window != 0 && windowTotal > windowPrevCount) {
                    println("CALLED")
                    countInc++
                }

                windowPrevCount = windowTotal
                window++

                windowTotal = 0
                windowSub = 0

                index -= 1
                println("\n")
            } else {
                index++
            }



        }

        println(countInc)
    }

    private fun getResource(): MutableList<Int> {
        val text = DayOne::class.java.getResource("/challenges/day 1.txt").readText()
        return text.split("\n").map {
            // Invisible characters go brrrrr
            it.replace(Regex("\\D+"), "").toInt()
        }.toMutableList()
    }

}