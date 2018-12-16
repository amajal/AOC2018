import java.io.File

fun main(args: Array<String>) {
    val input = File("Demo.txt").readLines()

    val result = initialiseBoard(input)
    val board = result.first
    val pieces = result.second

    print(board, pieces)

    repeat(1)
    {
        val piecesForLoop = pieces.toMutableList()

        while (piecesForLoop.count() > 0) {
            val currentPiece = piecesForLoop[0]
            val enemies = pieces.filter { p -> p.Type != currentPiece.Type }

            move(currentPiece, enemies, pieces, board)
            attack(currentPiece, enemies)

            piecesForLoop.removeAt(0)
            pieces.removeAll { p -> p.HP < 1 }
            piecesForLoop.removeAll { p -> p.HP < 1 }

            print(board, pieces)
        }

        pieces.sortBy { p -> p.Row }
        pieces.sortBy { p -> p.Col }
    }
}

fun move(currentPiece: Piece, enemies: List<Piece>, pieces: MutableList<Piece>, board: MutableList<MutableList<Char>>) {
    val enemiesInRange = getEnemiesInRange(enemies, currentPiece)
    val reachableEnemies = mutableMapOf<Piece, Pair<Pair<Int, Int>, Int>>()
    val currentPoint = Pair(currentPiece.Col, currentPiece.Row)

    if (enemiesInRange.count() > 0)
        return

    enemies.forEach {
        val targetPoint = Pair(it.Col, it.Row)
        var shortestDistanceToEnemy = Pair(Pair(-1, -1), Int.MAX_VALUE)

        for (c in listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))) {
            val nextPoint = Pair(currentPoint.first + c.first, currentPoint.second + c.second)
            val shortestDistanceForDirection = findShortestDistance(nextPoint, targetPoint, mutableListOf(), pieces, board)

            if (shortestDistanceForDirection < shortestDistanceToEnemy.second)
                shortestDistanceToEnemy = Pair(c, shortestDistanceForDirection)
        }

        if (shortestDistanceToEnemy.second != Int.MAX_VALUE)
            reachableEnemies[it] = shortestDistanceToEnemy
    }

    //todo
    print("done finding enemies")
            //val nearestEnemy = reachableEnemies.minBy { it.value.second }!!.value

            //currentPiece.Col = nearestEnemy[0].first
            //currentPiece.Row = nearestEnemy[0].second
}


fun findShortestDistance(currentPoint: Pair<Int, Int>, targetPoint: Pair<Int, Int>, visited: MutableList<Pair<Int, Int>>, pieces: MutableList<Piece>, board: MutableList<MutableList<Char>>): Int {
    if (currentPoint == targetPoint)
        return 0

    if (currentPoint.first < 0 || currentPoint.first > board.size - 1)
        return Int.MAX_VALUE

    if (currentPoint.second < 0 || currentPoint.second > board[0].size - 1)
        return Int.MAX_VALUE

    if (visited.contains(currentPoint) || board[currentPoint.first][currentPoint.second] != '.')
        return Int.MAX_VALUE

    if (pieces.count { it.Col == currentPoint.first && it.Row == currentPoint.second } != 0)
        return Int.MAX_VALUE

    visited.add(currentPoint)


    val paths = mutableListOf<Int>()

    for (c in listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))) {
        val nextPoint = Pair(currentPoint.first + c.first, currentPoint.second + c.second)
        var shortestDistanceFromNextPoint = findShortestDistance(nextPoint, targetPoint, visited.toMutableList(), pieces, board)
        if (shortestDistanceFromNextPoint != Int.MAX_VALUE)
            shortestDistanceFromNextPoint ++

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