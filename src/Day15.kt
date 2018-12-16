import java.io.File
import kotlin.math.min

fun main(args: Array<String>) {
    val input = File("Demo.txt").readLines()

    val result = initialiseBoard(input)
    val board = result.first
    val pieces = result.second


    repeat(1)
    {
        println("\nRound $it")
        print(board, pieces)

        pieces.sortBy { p -> p.Point.Col }
        pieces.sortBy { p -> p.Point.Row }

        val piecesForLoop = pieces.toMutableList()

        while (piecesForLoop.count() > 0) {
            val currentPiece = piecesForLoop[0]
            val enemies = pieces.filter { p -> p.Type != currentPiece.Type }

            move(currentPiece, enemies, pieces, board)
            attack(currentPiece, enemies)

            piecesForLoop.removeAt(0)
            pieces.removeAll { p -> p.HP < 1 }
            piecesForLoop.removeAll { p -> p.HP < 1 }

            //print(board, pieces)
        }
    }

    println("\nFinal")
    print(board, pieces)
}

fun move(currentPiece: Piece, enemies: List<Piece>, pieces: MutableList<Piece>, board: MutableList<MutableList<Char>>) {
    val enemiesInRange = getEnemiesInRange(enemies, currentPiece)

    if (enemiesInRange.count() > 0)
        return

    val squaresInRange = mutableMapOf<Point, Int>()
    val currentPoint = currentPiece.Point

    val barriers = getBarriers(pieces, board)

    enemies.forEach {

        var matrix = getShortestDistanceForAllPoints()

        for (move in listOf(Pair(0, -1), Pair(0, 1), Pair(-1, 0), Pair(1, 0))) {
            val nextPoint = Point(currentPoint.Row + move.first, currentPoint.Col + move.second)

            if (board[nextPoint.Row][nextPoint.Col] == '#' || pieces.count { p -> p.Point == nextPoint } > 0)
                continue

            val distance = findShortestDistance(currentPoint, it.Point, mutableListOf(), pieces, board)
            squaresInRange[nextPoint] = distance
        }
    }

    val reachableSquares = squaresInRange.filter { it.value < Int.MAX_VALUE }

    if (reachableSquares.isEmpty())
        return

    val nearestSquares = reachableSquares.minBy { it.value }!!
    currentPiece.Point = nearestSquares.key
}

fun getBarriers(pieces: MutableList<Piece>, board: MutableList<MutableList<Char>>): Any {
    val barriers = mutableListOf<Point>()
    pieces.forEach { barriers.add(it.Point) }

    for (i in 0..board.size)
    {
        for (j in 0..board.size)
        {
            if (board[i][j] == '#')
                barriers.add(Point(i, j))
        }
    }

    return barriers
}


fun findShortestDistance(currentPoint: Point, targetPoint: Point, visited: MutableList<Point>, pieces: MutableList<Piece>, board: MutableList<MutableList<Char>>): Int {
    if (currentPoint == targetPoint)
        return 0

    if (visited.contains(currentPoint))
        return Int.MAX_VALUE

    visited.add(currentPoint)

    val paths = mutableListOf<Int>()

    for (c in listOf(Pair(0, -1), Pair(0, 1), Pair(1, 0), Pair(-1, 0))) {
        val nextPoint = Point(currentPoint.Row + c.first, currentPoint.Col + c.second)

        if (nextPoint == targetPoint)
            return 1

        if (board[nextPoint.Row][nextPoint.Col] == '#' || pieces.count { it.Point == nextPoint } > 0)
            return Int.MAX_VALUE

        var shortestDistanceFromNextPoint = findShortestDistance(nextPoint, targetPoint, visited.toMutableList(), pieces, board)
        if (shortestDistanceFromNextPoint != Int.MAX_VALUE)
            shortestDistanceFromNextPoint++

        paths.add(shortestDistanceFromNextPoint)
    }

    return paths.min() ?: Int.MAX_VALUE
}

fun attack(currentPiece: Piece, enemies: List<Piece>) {
    val enemiesInRange = getEnemiesInRange(enemies, currentPiece)

    if (enemiesInRange.count() == 0)
        return

    // todo
    val weakestEnemyInRange = enemiesInRange
            .sortedBy { p -> p.HP }
            .sortedBy { p -> p.Point.Col }
            .sortedBy { p -> p.Point.Row }
            .first()

    weakestEnemyInRange.HP -= 3
}

private fun getEnemiesInRange(enemies: List<Piece>, currentPiece: Piece): List<Piece> {
    return enemies.filter { p ->
        (Math.abs(p.Point.Col - currentPiece.Point.Col) == 1 && p.Point.Row == currentPiece.Point.Row) ||
                (Math.abs(p.Point.Row - currentPiece.Point.Row) == 1 && p.Point.Col == currentPiece.Point.Col)
    }
}

fun print(board: MutableList<MutableList<Char>>, pieces: MutableList<Piece>) {
    for (i in 0 until board.size) {
        for (j in 0 until board[i].size) {
            val matchPiece = pieces.filter { p -> p.Point.Row == i && p.Point.Col == j }
            if (matchPiece.count() == 0)
                print(board[i][j])
            else
                print(matchPiece.first().Type)
        }
        println()
    }
}

fun initialiseBoard(input: List<String>): Pair<MutableList<MutableList<Char>>, MutableList<Piece>> {
    val board = mutableListOf<MutableList<Char>>()
    val pieces = mutableListOf<Piece>()

    for (i in 0 until input.size) {
        board.add(mutableListOf())
        for (j in 0 until input[0].length) {
            var current = input[i][j]

            if (current == 'E' || current == 'G') {
                pieces.add(Piece(current, 200, Point(i, j)))
                current = '.'
            }

            board[i].add(current)
        }
    }
    return Pair(board, pieces)
}

data class Piece(val Type: Char, var HP: Int, var Point: Point)
data class Point(var Row: Int, var Col: Int)