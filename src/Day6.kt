import java.io.File

fun main(args: Array<String>) {
    val mapOfNearestCoordinate = mutableMapOf<Pair<Int, Int>, MutableSet<Pair<Int, Int>>>()

    File("Input1.txt").bufferedReader().useLines { lines ->
        lines.forEach { l ->
            val tokens: List<String> = l.split(",")
            val pair = Pair(tokens[0].trim().toInt(), tokens[1].trim().toInt())
            mapOfNearestCoordinate[pair] = mutableSetOf(pair)
        }
    }

    val max_x = mapOfNearestCoordinate.keys.maxBy { it -> it.first }!!.first
    val max_y = mapOfNearestCoordinate.keys.maxBy { it -> it.second }!!.second


    for (x in 0..max_x) {
        for (y in 0..max_y) {
            var minDistance: Int? = null
            var closestPair: Pair<Int, Int>? = null
            val currentPair = Pair(x, y)
            mapOfNearestCoordinate.keys.forEach { it ->
                val newDistance = Math.abs(it.first - x) + Math.abs(it.second - y)
                if (minDistance == null || newDistance < minDistance!!) {
                    minDistance = newDistance
                    closestPair = it
                } else if (newDistance == minDistance) {
                    closestPair = null
                    return@forEach
                }
            }

            if (closestPair == null)
                continue

            mapOfNearestCoordinate[closestPair!!]!!.add(currentPair)
        }
    }

    val safeRegion = mapOfNearestCoordinate.values.filter { list ->
        list.none {
            it.first == 0 || it.first == max_x || it.second == 0 || it.second == max_y
        }
    }.maxBy { it.count() }


    var safeCount = 0
    for (x in 0..max_x) {
        for (y in 0..max_y) {
            if (mapOfNearestCoordinate.keys.fold(0) { acc, coordinate ->
                        acc + Math.abs(x - coordinate.first) + Math.abs(y - coordinate.second)
                    } < 10000) {
                safeCount++
            }
        }
    }

    println("Part 1->${safeRegion!!.count()}")
    println("Part 2->${safeCount}")

}