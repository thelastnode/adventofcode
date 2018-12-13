package adventofcode.day13

import java.io.File
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.util.*

object Day13_2 {
    enum class State(val repr: Set<Char>) {
        TRACK(setOf('|', '-')),
        CURVE(setOf('\\', '/')),
        INTERSECTION(setOf('+')),
        CART(setOf('>', 'v', '^', '<'));
    }

    fun stateFromChar(c: Char): State = State.values().find { c in it.repr }!!

    data class Position(val x: Int, val y: Int) : Comparable<Position> {
        fun left() = Position(x - 1, y)
        fun right() = Position(x + 1, y)
        fun up() = Position(x, y - 1)
        fun down() = Position(x, y + 1)

        override fun compareTo(other: Position): Int {
            val yComp = y.compareTo(other.y)
            if (yComp != 0) {
                return yComp
            }
            return x.compareTo(other.x)
        }
    }

    data class Cell(val pos: Position, val state: State, val raw: Char)

    enum class NextIntersection(val next: () -> NextIntersection) {
        LEFT({ NextIntersection.STRAIGHT }),
        STRAIGHT({ NextIntersection.RIGHT }),
        RIGHT({ NextIntersection.LEFT })
    }

    data class Cart(val pos: Position, val next: NextIntersection, val raw: Char) {
        fun forward(): Cart {
            val nextPos = when (raw) {
                '>' -> pos.right()
                '^' -> pos.up()
                '<' -> pos.left()
                'v' -> pos.down()
                else -> throw IllegalArgumentException()
            }
            return Cart(nextPos, next, raw)
        }

        fun leftTurn(): Cart = Cart(pos, next, when (raw) {
            '>' -> '^'
            '^' -> '<'
            '<' -> 'v'
            'v' -> '>'
            else -> throw IllegalArgumentException()
        })
        fun rightTurn(): Cart = Cart(pos, next, when (raw) {
            '>' -> 'v'
            '^' -> '>'
            '<' -> '^'
            'v' -> '<'
            else -> throw IllegalArgumentException()
        })
    }

    fun parse(lines: List<String>): List<Cell> {
        return lines.withIndex().flatMap { (i, line) ->
            line
                .mapIndexed { j, x ->
                    when (x) {
                        ' ' -> null
                        else -> Cell(Position(j, i), stateFromChar(x), x)
                    }
                }
                .filterNotNull()
        }
    }

    private fun printGrid(grid: SortedMap<Position, Cell>, carts: List<Cart>) {
        val cartPos = carts.associateBy { it.pos }
        val maxX = grid.map { it.key.x }.max()!!
        val maxY = grid.map { it.key.y }.max()!!

        for (j in 0..maxY) {
            for (i in 0..maxX) {
                val p = Position(i, j)
                if (p in cartPos) {
                    print(cartPos[p]!!.raw)
                } else if (p in grid) {
                    print(grid[p]!!.raw)
                } else {
                    print(' ')
                }
            }
            println()
        }
    }

    fun getCartReplacement(cells: SortedMap<Position, Cell>, pos: Position): Cell {
        val left = pos.left().run { this in cells && cells[this]!!.raw in setOf('-', '\\', '/', '+') }
        val right = pos.right().run { this in cells && cells[this]!!.raw in setOf('-', '\\', '/', '+') }
        val up = pos.up().run { this in cells && cells[this]!!.raw in setOf('|', '\\', '/', '+') }
        val down = pos.down().run { this in cells && cells[this]!!.raw in setOf('|', '\\', '/', '+') }

        val newTrack = when {
            up && down && left && right -> '+'
            (up && left) || (down && right) -> '/'
            (up && right) || (down && left) -> '\\'
            up && down -> '|'
            left && right -> '-'
            else -> throw IllegalStateException()
        }
        return Cell(pos = pos, state = stateFromChar(newTrack), raw = newTrack)
    }

    fun removeCarts(cells: SortedMap<Position, Cell>): SortedMap<Position, Cell> {
        return cells
            .map { (_, c) ->
                when (c.state) {
                    State.CART -> getCartReplacement(cells, c.pos)
                    else -> c
                }
            }
            .associateBy { it.pos }
            .toSortedMap()
    }

    private fun step(carts: List<Cart>, grid: SortedMap<Position, Cell>): List<Cart> {
        val remainingCarts = carts.sortedBy { it.pos }.toMutableList()
        val processedCarts = mutableListOf<Cart>()

        fun processCart(cart: Cart) {
            val (pos, next, raw) = cart
            val nextPos = when (raw) {
                '>' -> pos.right()
                '^' -> pos.up()
                '<' -> pos.left()
                'v' -> pos.down()
                else -> throw IllegalArgumentException()
            }
            val nextCell = grid[nextPos]!!
            val nextRaw = when (nextCell.state) {
                State.TRACK -> cart.forward()
                State.INTERSECTION -> when (next) {
                    NextIntersection.LEFT -> cart.leftTurn()
                    NextIntersection.STRAIGHT -> cart.forward()
                    NextIntersection.RIGHT -> cart.rightTurn()
                }
                State.CURVE -> when {
                    nextCell.raw == '/' && cart.raw in setOf('v', '^') -> cart.rightTurn()
                    nextCell.raw == '/' && cart.raw in setOf('>', '<') -> cart.leftTurn()
                    nextCell.raw == '\\' && cart.raw in setOf('v', '^') -> cart.leftTurn()
                    nextCell.raw == '\\' && cart.raw in setOf('>', '<') -> cart.rightTurn()
                    else -> throw IllegalArgumentException()
                }
                else -> throw IllegalArgumentException()
            }.raw
            val nextNext = if (nextCell.state == State.INTERSECTION) next.next() else next

            val cartPositions = remainingCarts.map { it.pos }.toSet().union(processedCarts.map { it.pos })
            val hasCrashed = nextPos in cartPositions

            if (hasCrashed) {
                remainingCarts.removeIf { it.pos == nextPos }
                processedCarts.removeIf { it.pos == nextPos }
            } else {
                processedCarts.add(Cart(nextPos, nextNext, if (hasCrashed) 'X' else nextRaw))
            }
        }

        while (remainingCarts.isNotEmpty()) {
            val nextCartToMove = remainingCarts.removeAt(0)
            processCart(nextCartToMove)
        }


        return processedCarts
    }

    fun process(inputs: List<Cell>): Any? {
        var carts = inputs.filter { it.state == State.CART }
            .map { Cart(it.pos, NextIntersection.LEFT, it.raw) }
        val grid = inputs.associateBy { it.pos }.toSortedMap()
            .run { removeCarts(this) }

        while (carts.size > 1) {
            carts = step(carts, grid)
//            printGrid(grid, carts)
        }

        return carts
    }
}

fun main(args: Array<String>) {
    val lines = File("./day13-input").readLines()
    val inputs = Day13_2.parse(lines)
    println(Day13_2.process(inputs))
}