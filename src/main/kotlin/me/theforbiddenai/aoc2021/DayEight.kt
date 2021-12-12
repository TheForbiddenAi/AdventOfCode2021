package me.theforbiddenai.aoc2021

import java.io.File

class DayEight {

    private val input = getInput()

    // Answer 239
    fun challengeOne() {
        println(input.values.sumOf {
            it.split(" ")
                .map { str -> str.length }
                .count { str -> str == 2 || str == 3 || str == 4 || str == 7 }
        })
    }

    // Answer 946346
    fun challengeTwo() {
        var total = 0
        input.forEach { (key, value) ->
            val translationKey = createTranslationKey(key)
            val valueArgs = value.split(" ").filter { it.isNotBlank() }

            val valueAsInt = try {
                valueArgs.map { arg -> toNumber(translate(arg, translationKey, false)) }
                    .joinToString("").toInt()
            } catch (ex: Exception) {
                key.split(" ").filter { str -> str.length == 2 }.forEach { str ->
                    translationKey[str[1]] = 'c'
                    translationKey[str[0]] = 'f'
                }

                valueArgs.map { arg -> toNumber(translate(arg, translationKey, false)) }
                    .joinToString("").toInt()
            }

            total += valueAsInt
        }
        println(total)

    }

    private fun toNumber(str: String): Int {
        return when (str.toCharArray().sorted().joinToString("")) {
            "abcefg" -> 0
            "cf" -> 1
            "acdeg" -> 2
            "acdfg" -> 3
            "bcdf" -> 4
            "abdfg" -> 5
            "abdefg" -> 6
            "acf" -> 7
            "abcdefg" -> 8
            "abcdfg" -> 9
            else -> {
                throw Exception()
            }
        }
    }

    private fun createTranslationKey(encryptedText: String): MutableMap<Char, Char> {
        val args = encryptedText.split(" ").filter { it.isNotBlank() }

        // Key = obfuscated character, Value = deobfuscated character
        // I.e. (df which is 1) d = key, c = deobfuscated (b/c d is in the position where c should be)
        val translationKey = mutableMapOf<Char, Char>()

        // Unique Integers are 1, 4, 7, 8
        // 0 = 1, 1 = 4, 2 = 7, 3 = 8
        var count = 0
        val uniqueSorted = getUniqueDigitPatterns(args).sortedBy { pattern -> pattern.length }
        uniqueSorted.forEach {
            val modifiedPattern = it.filter { ch -> !translationKey.containsKey(ch) }
            when (count) {
                0 -> {
                    translationKey[modifiedPattern[0]] = 'c'
                    translationKey[modifiedPattern[1]] = 'f'
                }
                1 -> {
                    translationKey[modifiedPattern[0]] = 'a'
                }
                2 -> {
                    // This is such a mess lmao
                    val middleCharacter = args.filter { str -> str.length == 5 }
                        .map { str -> str.replace(Regex("[^$it]"), "") }
                        .filter { str -> str.length == 2 }[0]
                        .filter { ch -> !translationKey.containsKey(ch) }

                    val leftOverChar = modifiedPattern.replace(middleCharacter, "")

                    translationKey[middleCharacter[0]] = 'd'
                    translationKey[leftOverChar[0]] = 'b'
                }
            }
            count++

        }

        args.filter { it.length == 6 }
            .map { translate(it, translationKey, true) }
            .forEach {
                val translationArgs = it.split("|").map { str -> str.trim() }
                val failed = translationArgs[1]
                if (failed.length == 1) {
                    translationKey[failed[0]] = 'g'

                    val missingKey = "abcdefg".filter { ch -> !translationKey.containsKey(ch) }
                    translationKey[missingKey[0]] = 'e'
                }
            }

        return translationKey
    }

    private fun translate(str: String, key: Map<Char, Char>, containFail: Boolean): String {
        val arr = str.map { if (key.containsKey(it)) key[it].toString() else str.indexOf(it).toString() }
        var translated = ""
        var failed = ""
        arr.forEach {
            if (it.matches(Regex("[0-9]"))) {
                failed += str[it.toInt()]
                return@forEach
            }

            translated += it
        }
        return if (containFail) "$translated | $failed" else translated
    }

    private fun getUniqueDigitPatterns(input: List<String>): List<String> {
        return input.filter {
            val length = it.length
            return@filter length == 2 || length == 3 || length == 4 || length == 7
        }.toList()
    }

    private fun getInput(): Map<String, String> {
        val text = File("src/main/resources/input/day 8.txt").readLines()
        return text.map { it.split("|") }.associate { it[0].trim() to it[1].trim() }
    }
}
