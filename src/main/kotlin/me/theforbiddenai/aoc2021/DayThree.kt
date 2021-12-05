package me.theforbiddenai.aoc2021

class DayThree {

    private val binary = getResource()
    private val length = binary[0].length

    // Answer: 775304
    fun challengeOne() {
        var gammaStr = ""
        var epsilonStr = ""

        for (i in 0 until length) {
            val oneCount = binary.sumOf { it[i].digitToInt() }
            val zeroCount = binary.size - oneCount

            gammaStr += if (oneCount > zeroCount) "1" else "0"
            epsilonStr += if (oneCount > zeroCount) "0" else "1"
        }

        println("Gamma: $gammaStr\nEpsilon: $epsilonStr")
        println(gammaStr.toInt(2) * epsilonStr.toInt(2))
    }

    // Answer: 1370737
    fun challengeTwo() {
        val oxygen = getBinaryNum(true)
        val carbonDioxide = getBinaryNum(false)

        println("O: $oxygen - CO: $carbonDioxide")
        println(oxygen * carbonDioxide)
    }

    private fun getBinaryNum(oxygen: Boolean): Int {
        val array = binary.toMutableList() // Copies array

        for (i in 0 until length) {
            val oneCount = array.sumOf { it[i].digitToInt() }
            val zeroCount = array.size - oneCount

            // Ah, yes readability at its finest. I could probably make this nicer, but I can't be bothered to since it works.
            val remove = if(oxygen) if (oneCount >= zeroCount) 0 else 1
            else if (oneCount >= zeroCount) 1 else 0

            array.removeIf { it[i].digitToInt() == remove }
            if(array.size == 1) return array[0].toInt(2)
        }
        return 0
    }

    private fun getResource(): MutableList<String> {
        val text = DayOne::class.java.getResource("/challenges/day 3.txt").readText()
        // I love invisible characters.......
        return text.split("\n").map { it.replace(Regex("\\D+"), "") }.toMutableList()
    }

}