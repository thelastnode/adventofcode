package adventofcode.day11

object Day11_1 {
    fun powerLevel(coord: Pair<Int, Int>, gridSerial: Int): Int {
        val (x, y) = coord
        val rackId = x + 10
        val t = (rackId * y + gridSerial) * rackId
        return (t / 100) % 10 - 5
    }

    fun process(gridSerial: Int): Pair<Int, Int> {
        val grid = mutableMapOf<Pair<Int, Int>, Int>()

        for (x in 1..300) {
            for (y in 1..300) {
                grid[Pair(x, y)] = powerLevel(Pair(x, y), gridSerial)
            }
        }

        val powers = mutableMapOf<Pair<Int, Int>, Int>()

        for (x in 1..(300 - 2)) for (y in 1..(300 - 2)) {
            var power = 0
            for (i in 0..2) for (j in 0..2) {
                power += grid[Pair(x + i, y + j)]!!
            }
            powers[Pair(x, y)] = power
        }

        return powers.maxBy { (k, v) -> v }!!.key
    }
}

fun main(args: Array<String>) {
    println(Day11_1.process(18))
    println(Day11_1.process(42))
    println(Day11_1.process(3214))
}