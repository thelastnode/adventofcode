package adventofcode.day11

object Day11_2 {
    data class Coord(val x: Int, val y: Int, val w: Int)

    fun powerLevel(coord: Pair<Int, Int>, gridSerial: Int): Int {
        val (x, y) = coord
        val rackId = x + 10
        val t = (rackId * y + gridSerial) * rackId
        return (t / 100) % 10 - 5
    }

    fun process(gridSerial: Int): Coord {
        val grid = mutableMapOf<Pair<Int, Int>, Int>()

        for (x in 1..300) {
            for (y in 1..300) {
                grid[Pair(x, y)] = powerLevel(Pair(x, y), gridSerial)
            }
        }

        val powers: Array<Array<IntArray>> = Array(301) { Array(301) { IntArray(301) } }

        // base case
        for (x in 1..300) for (y in 1..300) {
            powers[x][y][1] = grid[Pair(x, y)]!!
        }

        for (w in 2..300) for (x in 1..(300 - w + 1)) for (y in 1..(300 - w + 1)) {
            var power = powers[x][y][w - 1]

            for (i in 0 until w) {
                power += grid[Pair(x + i, y + w - 1)]!!
            }
            for (j in 0 until w) {
                power += grid[Pair(x + w - 1, y + j)]!!
            }
            power += grid[Pair(x + w - 1, y + w - 1)]!!

            powers[x][y][w] = power
        }

        var maxVal = Int.MIN_VALUE
        var maxCoord = Coord(-1, -1, -1)
        for (w in 2..300) for (x in 1..(300 - w + 1)) for (y in 1..(300 - w + 1)) {
            if (powers[x][y][w] > maxVal) {
                maxVal = powers[x][y][w]
                maxCoord = Coord(x, y, w)
            }
        }


        return maxCoord
    }
}

fun main(args: Array<String>) {
//    println(Day11_2.process(18))
//    println(Day11_2.process(42))
    println(Day11_2.process(3214))
}