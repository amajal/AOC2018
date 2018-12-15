import java.io.File

fun main(args: Array<String>) {
    val input = File("Demo.txt").readLines()

    val result = initialiseBoard(input)
    val board = result.first
    val pieces = result.second

    print(board, pieces)

    repeat(1)
    {
        pieces.forEach { currentPiece ->
            if (currentPiece.HP > 0) {
                val enemies = pieces.filter { p -> p.HP > 0 && p.Type != currentPiece.Type }
                move(currentPiece, enemies)
                attack(currentPiece, enemies)
            }
        }

        pieces.removeAll { p -> p.HP < 1 }
        pieces.sortBy { p -> p.Row }
        pieces.sortBy { p -> p.Col }
    }
}

fun move(currentPiece: Piece, enemies: List<Piece>) {

}

fun attack(currentPiece: Piece, enemies: List<Piece>) {
    val enemiesInRange = enemies.filter { p ->
        (Math.abs(p.Col - currentPiece.Col) == 1 && p.Row == currentPiece.Row) ||
                (Math.abs(p.Row - currentPiece.Row) == 1 && p.Col == currentPiece.Col)
    }

    if (enemiesInRange.count() == 0)
        return

    val weakestEnemyInRange = enemiesInRange
            .sortedBy { p -> p.HP }
            .sortedBy { p -> p.Row }
            .sortedBy { p -> p.Col }
            .first()

    weakestEnemyInRange.HP -= 3

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