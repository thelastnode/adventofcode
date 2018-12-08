package adventofcode.day7

import java.io.File
import java.util.*

object Day7_2 {
    data class Input(val step: String, val dependency: String)

    private val LINE_REGEX = Regex("Step (\\w+) must be finished before step (\\w+) can begin")

    fun parse(line: String): Input {
        val match = LINE_REGEX.find(line)
        val (_, dependency, step) = match!!.groupValues
        return Input(step = step, dependency = dependency)
    }

    data class Event(val completedStep: String, val time: Int) : Comparable<Event> {
        override fun compareTo(other: Event): Int = time.compareTo(other.time)
    }

    fun process(inputs: List<Input>): String {
        var time = 0
        var availableWorkers = 5

        val steps = inputs.flatMap { listOf(it.dependency, it.step) }.distinct().toMutableSet()
        val stepTime = steps.map { Pair(it, it[0].minus('A') + 60 + 1) }.toMap()
        val dependenciesByStep = inputs
                .groupBy { it.step }
                .mapValues { it.value.map { input -> input.dependency }.toMutableSet() }
                .toMutableMap()

        val ordering = mutableListOf<String>()

        val eventQueue = PriorityQueue<Event>()

        fun removeFromDependencies(step: String) {
            dependenciesByStep.forEach { (_, dependencies) -> dependencies.remove(step) }
        }
        fun handleEventCompletion(event: Event) {
            time = event.time
            availableWorkers += 1
            removeFromDependencies(event.completedStep)
            ordering.add(event.completedStep)
        }
        fun fillWorkers() {
            val nextSteps = steps.filter { step -> (step !in dependenciesByStep) || dependenciesByStep[step]!!.isEmpty() }
                    .sorted()
            val stepsToAssign = nextSteps.take(Math.min(availableWorkers, nextSteps.size))
            steps.removeAll(stepsToAssign)

            val newEvents = stepsToAssign.map { step -> Event(completedStep = step, time = time + stepTime[step]!!) }
            availableWorkers -= newEvents.size
            eventQueue.addAll(newEvents)
        }

        do {
            if (eventQueue.isNotEmpty()) {
                val event = eventQueue.poll()
                handleEventCompletion(event)
            }
            fillWorkers()
        } while (eventQueue.isNotEmpty())

        println(ordering.joinToString(""))

        return time.toString()
    }
}

fun main(args: Array<String>) {
    val lines = File("./day7-input").readText().split("\n")
    val inputs = lines.map { Day7_2.parse(it) }
    println(Day7_2.process(inputs))
}
