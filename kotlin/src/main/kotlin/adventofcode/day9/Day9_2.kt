package adventofcode.day8

object Day9_2 {

    data class Marble(val value: Int, var prev: Marble?, var next: Marble?) {
        fun insertAfter(n: Int) {
            if (this.prev == this) {
                val newMarble = Marble(n, this, this)
                this.prev = newMarble
                this.next = newMarble
            } else {
                val newMarble = Marble(n, this, this.next)
                this.next!!.prev = newMarble
                this.next = newMarble
            }
        }

        fun removeAfter(): Int {
            val next = this.next!!
            next.next!!.prev = this
            this.next = next.next!!

            next.next = null
            next.prev = null

            return next.value
        }

        override fun toString(): String = "$value"
    }

    fun firstMarble(value: Int): Marble {
        val marble = Marble(value, null, null)
        marble.next = marble
        marble.prev = marble
        return marble
    }

    fun printChain(startingMarble: Marble): String {
        var currentMarble = startingMarble
        val results = mutableListOf<Int>()

        while (currentMarble.value != 0) {
            currentMarble = currentMarble.next!!
        }
        do {
            results.add(currentMarble.value)
            currentMarble = currentMarble.next!!
        } while (currentMarble.value != 0)

        return results.toString()
    }

    fun process(playerCount: Int, marbleCount: Int): Long {
        var currentMarble = firstMarble(0)
        val scores = (0 until playerCount).map { 0L }.toMutableList()

        var currentPlayer = 0

        for (nextMarble in 1..marbleCount) {
            if (nextMarble % 23 == 0) {
                // go back 8 times
                (0..7).forEach { currentMarble = currentMarble.prev!! }
                val removed = currentMarble.removeAfter()
                scores[currentPlayer] += (nextMarble + removed).toLong()
                currentMarble = currentMarble.next!!
            } else {
                currentMarble = currentMarble.next!!
                currentMarble.insertAfter(nextMarble)
                currentMarble = currentMarble.next!!
            }

            currentPlayer = (currentPlayer + 1) % playerCount

//            println("$nextMarble - ${printChain(currentMarble)}")
        }
        return scores.max()!!
    }
}

fun main(args: Array<String>) {
    println(Day9_2.process(playerCount = 9, marbleCount = 25))
    println(Day9_2.process(playerCount = 10, marbleCount = 1618))
    println(Day9_2.process(playerCount = 13, marbleCount = 7999))
    println(Day9_2.process(playerCount = 17, marbleCount = 1104))
    println(Day9_2.process(playerCount = 21, marbleCount = 6111))
    println(Day9_2.process(playerCount = 30, marbleCount = 5807))
    println(Day9_2.process(playerCount = 491, marbleCount = 71058))
    println(Day9_2.process(playerCount = 491, marbleCount = 71058 * 100))
}
