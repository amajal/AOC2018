import java.io.File
import kotlin.math.min

fun main(args: Array<String>) {
    val input = File("Demo.txt").readLines()

    val result = initialiseBoard(input)
    val board = result.first
    val pieces = result.second


    repeat(3)
    {
        println("\nRound $it")
        print(board, pieces)

        pieces.sortBy { p -> p.Col }
        pieces.sortBy { p -> p.Row }

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

    val squaresInRange = mutableMapOf<Pair<Int, Int>, Int>()
    val currentPoint = Pair(currentPiece.Col, currentPiece.Row)

    enemies.forEach {
        for (move in listOf(Pair(0, -1), Pair(0, 1), Pair(-1, 0), Pair(1, 0))) {
            val targetPoint = Pair(it.Col + move.first, it.Row + move.second)
            val distance = findShortestDistance(currentPoint, targetPoint, mutableListOf(), pieces, board)
            squaresInRange[targetPoint] = distance
        }
    }

    val reachableSquares = squaresInRange.filter { it.value < Int.MAX_VALUE }

    if (reachableSquares.isEmpty())
        return

    val nearestSquares = reachableSquares.minBy { it.value }!!

    currentPiece.Row = nearestSquares.key.first
    currentPiece.Col = nearestSquares.key.second

}


fun findShortestDistance(currentPoint: Pair<Int, Int>, targetPoint: Pair<Int, Int>, visited: MutableList<Pair<Int, Int>>, pieces: MutableList<Piece>, board: MutableList<MutableList<Char>>): Int {
    if (currentPoint == targetPoint)
        return 0

    if (board[currentPoint.first][currentPoint.second] != '.')
        return Int.MAX_VALUE

    if (pieces.count { it.Col == currentPoint.first && it.Row == currentPoint.second } != 0)
        return Int.MAX_VALUE

    visited.add(currentPoint)

    val paths = mutableListOf<Int>()

    for (c in listOf(Pair(0, -1), Pair(0, 1), Pair(1, 0), Pair(-1, 0))) {
        val nextPoint = Pair(currentPoint.first + c.first, currentPoint.second + c.second)
        var shortestDistanceFromNextPoint = findShortestDistance(nextPoint, targetPoint, visited.toMutableList(), pieces, board)
        if (shortestDistanceFromNextPoint != Int.MAX_VALUE)
            shortestDistanceFromNextPoint++

        paths.add(shortestDistanceFromNextPoint)
    }

    return paths.min()!!
}

fun attack(currentPiece: Piece, enemies: List<Piece>) {
    val enemiesInRange = getEnemiesInRange(enemies, currentPiece)

    if (enemiesInRange.count() == 0)
        return

    // todo
    val weakestEnemyInRange = enemiesInRange
            .sortedBy { p -> p.HP }
            .sortedBy { p -> p.Row }
            .sortedBy { p -> p.Col }
            .first()

    weakestEnemyInRange.HP -= 3
}

private fun getEnemiesInRange(enemies: List<Piece>, currentPiece: Piece): List<Piece> {
    return enemies.filter { p ->
        (Math.abs(p.Col - currentPiece.Col) == 1 && p.Row == currentPiece.Row) ||
                (Math.abs(p.Row - currentPiece.Row) == 1 && p.Col == currentPiece.Col)
    }
}

fun print(board: MutableList<MutableList<Char>>, pieces: MutableList<Piece>) {
    for (i in 0 until board.size) {
        for (j in 0 until board[i].size) {
            val matchPiece = pieces.filter { p -> p.Row == i && p.Col == j }
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
                pieces.add(Piece(current, 200, i, j))
                current = '.'
            }

            board[i].add(current)
        }
    }
    return Pair(board, pieces)
}

data class Piece(val Type: Char, var HP: Int, var Row: Int, var Col: Int)