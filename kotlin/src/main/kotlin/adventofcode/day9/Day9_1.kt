package adventofcode.day8

import java.util.*

object Day9_1 {
    fun process(playerCount: Int, marbleCount: Int): Int {
        val marbles = ArrayList<Int>(marbleCount + 1)
        marbles.add(0)
        val scores = (0 until playerCount).map { 0 }.toMutableList()

        var currentMarbleIndex = 0
        var currentPlayer = 0

        for (nextMarble in 1..marbleCount) {
            if (nextMarble % 23 == 0) {
                val removalIndex = (currentMarbleIndex - 7 + marbles.size) % marbles.size
                val removed = marbles.removeAt(removalIndex)
                scores[currentPlayer] += nextMarble + removed
                currentMarbleIndex = (removalIndex + marbles.size) % marbles.size
            } else {
                val nextMarbleIndex = (currentMarbleIndex + 2) % marbles.size
                if (nextMarbleIndex == marbles.size) {
                    marbles.add(nextMarble)
                } else {
                    marbles.add(nextMarbleIndex, nextMarble)
                }
                currentMarbleIndex = nextMarbleIndex
            }

            currentPlayer = (currentPlayer + 1) % playerCount
        }
        return scores.max()!!
    }
}

fun main(args: Array<String>) {
    println(Day9_1.process(playerCount = 10, marbleCount = 1618))
    println(Day9_1.process(playerCount = 13, marbleCount = 7999))
    println(Day9_1.process(playerCount = 17, marbleCount = 1104))
    println(Day9_1.process(playerCount = 21, marbleCount = 6111))
    println(Day9_1.process(playerCount = 30, marbleCount = 5807))
    println(Day9_1.process(playerCount = 491, marbleCount = 71058))
}
