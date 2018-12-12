package adventofcode.day12

import java.io.File
import java.lang.Exception
import java.lang.IllegalArgumentException

object Day12_1 {
    data class Rule(val condition: String, val result: Char)

    fun parse(lines: List<String>): Pair<String, List<Rule>> {
        val initialState = lines[0].split(": ")[1].trim()
        val rules = lines.drop(2)
                .filter { it.trim().isNotEmpty() }
                .map { it.split(" => ") }
                .map { (condition, result) -> Rule(condition, result[0])}

        return Pair(initialState, rules)
    }

    fun conditions(state: String): List<String> {
        return ("..$state..").windowed(5)
    }

    private fun step(initialState: String, rules: List<Rule>): String {
        val rulesMap = rules.groupBy { it.condition }
                .mapValues { (_, v) ->
                    if (v.size != 1) {
                        throw IllegalArgumentException()
                    }
                    v[0]
                }
        return conditions(initialState)
                .map {
                    if (it in rulesMap) {
                        rulesMap[it]!!.result
                    } else {
                        '.'
                    }
                }
                .joinToString("")
    }

    fun process(initialState: String, rules: List<Rule>): Int {
        val pad = (0 until 100).map { '.' }.joinToString("")
        var state = pad + initialState + pad

        for (i in 0 until 20) {
            state = step(state, rules)
        }

        return state.withIndex().sumBy { (i, x) -> if (x == '.') 0 else i - pad.length  }
    }
}

fun main(args: Array<String>) {
    val lines = File("./day12-input").readLines()
    val (initialState, rules) = Day12_1.parse(lines)
    println(Day12_1.process(initialState, rules))
}