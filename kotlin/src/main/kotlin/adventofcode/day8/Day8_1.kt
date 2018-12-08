package adventofcode.day8

import java.io.File
import java.util.*

object Day8_1 {
    fun parse(line: String): List<Int> {
        return Scanner(line).asSequence().map { it.toInt(10) }.toList()
    }

    private fun <T> MutableList<T>.pop(n: Int): List<T> {
        val res = this.take(n)
        (0 until n).forEach { this.removeAt(0) }
        return res
    }

    data class Node(val metadata: List<Int>, val children: List<Node>)

    fun readNode(inputs: MutableList<Int>): Node {
        val (childCount, metadataCount) = inputs.pop(2)
        val children = (0 until childCount).map { readNode(inputs) }
        val metadata = inputs.pop(metadataCount)
        return Node(metadata = metadata, children = children)
    }

    fun process(inputs: MutableList<Int>): Int {
        val tree = readNode(inputs)
        fun traverse(node: Node): Int {
            return node.metadata.sum() + node.children.map { traverse(it) }.sum()
        }
        return traverse(tree)
    }
}

fun main(args: Array<String>) {
    val lines = File("./day8-input").readText()
    val inputs = Day8_1.parse(lines)
    println(Day8_1.process(inputs.toMutableList()))
}
