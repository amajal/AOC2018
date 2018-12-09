fun main(args: Array<String>) {
    println("Part 1 -> ${findMaxScore(452, 71250)}")
}


fun findMaxScore(players:Int, marbles:Int): Int
{
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

        /*print ("[$i]")
        for (j in 0 until circle.size) {
            if (currentIndex == j)
                print(" (${circle[j]})")
            else
                print(" ${circle[j]}")
        }
        println()*/
    }

    return score.max()!!
}
