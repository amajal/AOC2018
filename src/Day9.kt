fun main(args: Array<String>) {
    println("Part 1 -> ${findMaxScore(452, 71250)} ")
    println("Part 2 -> ${findMaxScoreOptimised(452, 71250 * 100)}")
}

fun findMaxScore(players: Int, marbles: Int): Int {
    val score = Array(players) { 0 }
    val circle = mutableListOf(0)
    var currentPlayer = 0
    var currentIndex = 0

    for (i in 1..marbles) {
        if (i % 23 == 0) {
            repeat(7) { currentIndex = (currentIndex - 1 + circle.size) % (circle.size) }
            score[currentPlayer] += i + circle[currentIndex]
            circle.removeAt(currentIndex)
        } else {
            currentIndex = (++currentIndex % circle.size) + 1
            circle.add(currentIndex, i)
        }

        currentPlayer = (currentPlayer + 1) % score.size
    }

    return score.max()!!
}


fun findMaxScoreOptimised(players: Int, marbles: Int): Long {
    val score = Array<Long>(players) { 0 }
    var current = Node(0)
    var currentPlayer = 0

    for (i in 1..marbles) {
        if (i % 23 == 0) {
            repeat(7) {
                current = current.left
            }
            score[currentPlayer] += (i + current.id).toLong()
            current.left.right = current.right
            current.right.left = current.left
            current = current.right
        } else {
            current = current.right
            val newNode = Node(i)

            newNode.left = current
            newNode.right = current.right
            current.right.left = newNode
            current.right = newNode
            current = newNode
        }

        currentPlayer = (currentPlayer + 1) % score.size
    }

    return score.max()!!
}

class Node() {
    var id: Int = 0
    var left: Node = this
    var right: Node = this

    constructor(id: Int) : this() {
        this.id = id
    }
}
