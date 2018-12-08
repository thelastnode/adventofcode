package adventofcode.day7

import java.io.File


data class Input(val step: String, val dependency: String)

val LINE_REGEX = Regex("Step (\\w+) must be finished before step (\\w+) can begin")

fun parse(line: String): Input {
    val match = LINE_REGEX.find(line)
    val (_, dependency, step) = match!!.groupValues
    return Input(step = step, dependency = dependency)
}

fun process(inputs: List<Input>): String {
    val steps = inputs.flatMap { listOf(it.dependency, it.step) }.distinct().toMutableSet()
    val dependenciesByStep = inputs
            .groupBy { it.step }
            .mapValues { it.value.map { input -> input.dependency }.toMutableSet() }
            .toMutableMap()
    val ordering = mutableListOf<String>()

    while (steps.isNotEmpty()) {
        val nextStep = steps.filter { step -> (step !in dependenciesByStep) || dependenciesByStep[step]!!.isEmpty() }
                .sorted()
                .first()
        steps.remove(nextStep)
        ordering.add(nextStep)
        dependenciesByStep.forEach { (_, dependencies) -> dependencies.remove(nextStep) }
    }

    return ordering.joinToString("")
}

fun main(args: Array<String>) {
    val lines = File("./day7-input").readText().split("\n")
    val inputs = lines.map { parse(it) }
    println(process(inputs))
}
