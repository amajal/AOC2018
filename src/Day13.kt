import java.io.File

fun main(args: Array<String>) {
    val track = mutableListOf<CharArray>()
    val directionalMap: HashMap<Char, HashMap<Char, Char>> = generateDirectionalMap()
    File("Demo.txt").readLines().forEach { track.add(it.toCharArray()) }

    val trains = mutableListOf<Train>()

    for (x in 0 until track.size) {
        for (y in 0 until track[x].size) {
            if ("<>".contains(track[x][y])) {
                trains.add(Train(x, y, track[x][y]))
                track[x][y] = '-'
            }

            if ("^v".contains(track[x][y])) {
                trains.add(Train(x, y, track[x][y]))
                track[x][y] = '|'
            }
        }
    }

    printTrack(track, trains)

    repeat(1) {
        trains.sortByDescending { t -> t.X }
        trains.sortByDescending { t -> t.Y }
        moveTrains(track, trains, directionalMap)
    }
    printTrack(track, trains)
}

fun generateDirectionalMap(): HashMap<Char, HashMap<Char, Char>> {

    val mainMap = hashMapOf<Char, HashMap<Char, String>>()
    mainMap['^'] = hashMapOf('R' to ">", 'L' to "<", 'X' to "0", 'Y' to "-1")
    mainMap['v'] = hashMapOf('R' to "<", 'L' to ">", 'X' to "0", 'Y' to "1")
    mainMap['>'] = hashMapOf('R' to "v", 'L' to "^", 'X' to "+1", 'Y' to "0")
    mainMap['<'] = hashMapOf('R' to "^", 'L' to "v", 'X' to "-1", 'Y' to "0")

    return mainMap
}

fun moveTrains(track: MutableList<CharArray>, trains: MutableList<Train>, directionalMap: HashMap<Char, HashMap<Char, Char>>) {
    trains.forEach { t ->
        if (t.Direction == 'v') {
            if (track[t.X][t.Y + 1] == '|') {
                t.Y++
            }
            if (track[t.X][t.Y + 1] == '\\') {
                t.Y++
                t.Direction = '>'
            }
            if (track[t.X][t.Y + 1] == '/') {
                t.Y++
                t.Direction = '<'
            }
            if (track[t.X][t.Y + 1] == '+') {
                t.Y++
                t.Direction = '<'
            }


        } else if (t.Direction == '^') {
            if (track[t.X][t.Y - 1] == '|') {
                t.Y++
            }
        }
    }
}

fun printTrack(track: MutableList<CharArray>, trains: MutableList<Train>) {
    for (x in 0 until track.size) {
        for (y in 0 until track[x].size) {
            val currentTrains = trains.filter { it.X == x && it.Y == y }
            when {
                currentTrains.count() > 1 -> print('X')
                currentTrains.count() == 1 -> print(currentTrains.first().Direction)
                else -> print(track[x][y])
            }
        }
        println()
    }

}

class Train() {
    var X: Int = 0
    var Y: Int = 0
    var Direction: Char = ' '

    constructor(X: Int, Y: Int, D: Char) : this() {
        this.X = X
        this.Y = Y
        this.Direction = D
    }
}