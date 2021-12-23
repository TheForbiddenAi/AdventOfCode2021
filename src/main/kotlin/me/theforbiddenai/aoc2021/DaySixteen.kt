package me.theforbiddenai.aoc2021

import java.io.File

class DaySixteen {

    private val binaryInput = getInput()

    // Answer 1007
    fun challengeOne() {
        val packet = parseOperatorPacket(binaryInput)
        var total = 0

        val packetList = mutableListOf(packet)

        while (packetList.isNotEmpty()) {
            val currentPacket = packetList[0]
            total += currentPacket.version

            packetList.addAll(currentPacket.subpackets)
            packetList.remove(currentPacket)
        }

        println(total)
    }

    // Answer 834151779165
    fun challengeTwo() {
        val packet = parseOperatorPacket(binaryInput)
        println(packet.executeOperation())
    }

    private fun parseOperatorPacket(binary: String): Packet {
        val lengthTypeID = binary.substring(6, 7).toInt()
        val lengthBinary = binary.substring(7, if (lengthTypeID == 0) 22 else 18)
        val lengthDecimal = lengthBinary.toInt(2)

        val headerRemoved = binary.substringAfter(lengthBinary)
        var nextType = headerRemoved.substring(3, 6).toInt(2)

        val subpackets = mutableListOf<Packet>()

        var usableBinary = if (lengthTypeID == 0) headerRemoved.substring(0, lengthDecimal) else headerRemoved

        while ((usableBinary.length <= lengthDecimal && lengthTypeID == 0) || (subpackets.size != lengthDecimal && lengthTypeID == 1)) {
            val packet = if (nextType == 4) parseLiteralPacket(usableBinary) else parseOperatorPacket(usableBinary)
            subpackets.add(packet)

            usableBinary = usableBinary.substring(packet.binary.length)
            nextType = if (usableBinary.length >= 11) usableBinary.substring(3, 6).toInt(2) else break
        }

        val header = binary.substringBefore(lengthBinary) + lengthBinary
        val packetBinary = header + subpackets.joinToString("") { it.binary }
        return Packet(packetBinary, subpackets)
    }

    private fun parseLiteralPacket(binary: String): Packet {
        var packetBinary = binary.substring(0, 6)
        var continueParsing = true
        var index = 6

        while (continueParsing) {
            if (index > binary.length) break
            val read = binary.substring(index, index + 5)

            packetBinary += read
            if (read.startsWith("0")) continueParsing = false
            index += 5
        }

        return Packet(packetBinary)
    }

    data class Packet(val binary: String, val subpackets: List<Packet> = listOf()) {

        val version = binary.substring(0, 3).toInt(2)
        private val typeId = binary.substring(3, 6).toInt(2)
        private val lengthId = if (typeId == 4) -1 else binary.substring(6, 7).toInt(2)

        fun executeOperation(): Long {
            if (lengthId == -1) {
                return binary.drop(6).windowed(5, 5).joinToString("") { it.drop(1) }.toLong(2)
            }

            val subValues = subpackets.map { it.executeOperation() }

            return when (typeId) {
                0 -> subValues.sum()
                1 -> subValues.reduce { acc, i -> acc * i }
                2 -> subValues.minOrNull() ?: throw Exception("Unable to find minimum.")
                3 -> subValues.maxOrNull() ?: throw Exception("Unable to find maximum.")
                5 -> if (subValues[0] > subValues[1]) 1 else 0
                6 -> if (subValues[0] < subValues[1]) 1 else 0
                7 -> if (subValues[0] == subValues[1]) 1 else 0
                else -> throw Exception("Invalid Packet.")
            }
        }

    }

    private fun getInput(): String {
        return File("src/main/resources/input/day 16.txt").readText().map {
            val binaryString = Integer.valueOf(it.toString(), 16).toString(2)
            val zerosNeeded = 4 - binaryString.length
            return@map "0".repeat(zerosNeeded) + binaryString
        }.joinToString("")
    }

}