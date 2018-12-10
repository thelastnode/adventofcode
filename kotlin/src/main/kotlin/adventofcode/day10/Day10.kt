package adventofcode.day10

import java.io.File
import java.util.*

object Day10 {
    data class Vector(val x: Int, val y: Int) {
        operator fun plus(other: Vector): Vector = Vector(x + other.x, y + other.y)

        fun neighbors(): List<Vector> {
            val offsets = listOf(
                    Vector( 0, -1),
                    Vector( 0,  1),
                    Vector(-1,  0),
                    Vector( 1,  0)
            )
            return offsets.map { this + it }
        }
    }

    data class Point(val position: Vector, val velocity: Vector) {
        fun tick(): Point = Point(position = position + velocity, velocity = velocity)
    }

    val LINE_REGEX = Regex("position=< ?([0-9\\-]+),  ?([0-9\\-]+)> velocity=< ?([0-9\\-]+),  ?([0-9\\-]+)>")

    fun parse(line: String): Point {
        val (_, posX, posY, velX, velY) = LINE_REGEX.find(line)!!.groupValues
        return Point(
                position = Vector(x = posX.toInt(), y = posY.toInt()),
                velocity = Vector(x = velX.toInt(), y = velY.toInt())
        )
    }

    fun printInstant(points: List<Point>): String {
        val positions = points.map { it.position }.toSet()

        val ys = positions.map { it.y }
        val xs = positions.map { it.x }
        val (xmin, xmax) = Pair(xs.min()!!, xs.max()!!)
        val (ymin, ymax) = Pair(ys.min()!!, ys.max()!!)


        return buildString {
            for (y in ymin..ymax) {
                for (x in xmin..xmax) {
                    if (Vector(x, y) in positions) {
                        append("#")
                    } else {
                        append(".")
                    }
                }
                appendln()
            }
        }
    }

    fun tick(points: List<Point>): List<Point> = points.map { it.tick() }

    fun connectedComponents(points: List<Point>): Int {
        val positions = points.map { it.position }.toSet()
        val visited = mutableSetOf<Vector>()
        var components = 0

        for (point in positions) {
            if (point in visited) {
                continue
            }
            components++

            val queue: Queue<Vector> = LinkedList()
            queue.add(point)

            while (queue.isNotEmpty()) {
                val p = queue.poll()
                visited.add(p)

                for (neighbor in p.neighbors()) {
                    if (neighbor !in visited && neighbor in positions) {
                        queue.add(neighbor)
                    }
                }
            }
        }

        return components
    }

    fun process(inputs: List<Point>) {
        val tries = 100_000

        var points = inputs
        val pointsAtTime = mutableListOf<List<Point>>()
        (0 until tries).forEach {
            pointsAtTime.add(points)
            points = tick(points)
        }
        val counts = pointsAtTime.map { connectedComponents(it) }

        val minIndex = counts.withIndex().minBy { it.value }!!.index

        println("Min components: ${counts[minIndex]} @ $minIndex")
        println(printInstant(pointsAtTime[minIndex]))
    }
}

fun main(args: Array<String>) {
    val lines = File("./day10-input").readLines()
    val inputs = lines.map { Day10.parse(it) }
    Day10.process(inputs)
}