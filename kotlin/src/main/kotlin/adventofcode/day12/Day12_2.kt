package adventofcode.day12

import java.io.File
import java.lang.Exception
import java.lang.IllegalArgumentException

object Day12_2 {
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

    private fun score(state: String, padSize: Int): Int {
        return state.withIndex().sumBy { (i, x) -> if (x == '.') 0 else i - padSize }
    }

    fun process(initialState: String, rules: List<Rule>): Long {
        val pad = (0 until 10_000).map { '.' }.joinToString("")
        var state = pad + initialState + pad
        val scores = mutableListOf(score(state, pad.length))
        val states = mutableListOf(state)

        for (i in 0 until 1000) {
            state = step(state, rules)
            scores.add(score(state, pad.length))
            states.add(state)
        }

        states.takeLast(10).forEach { println("${it.indexOf('#')}: ${it.count { it == '#' }}") }

        // numbers taken from the above println
        // (194 #'s, after a thousand iterations, starting at position 900, moving one position per iteration)
        val start = 50_000_000_000L - 1_000L + 900L
        return (start until start + 194).sum()
    }
}

fun main(args: Array<String>) {
    val lines = File("./day12-input").readLines()
    val (initialState, rules) = Day12_2.parse(lines)
    println(Day12_2.process(initialState, rules))
}