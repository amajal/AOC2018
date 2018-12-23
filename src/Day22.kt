package Day22

fun main(args: Array<String>) {
    doPart1(Point(10, 10), 510)
    doPart1(Point(9, 796), 6969) //1044,1101
    doPart1(Point(12, 763), 7740) //Right -> 1087

}

fun doPart1(target: Point, depth: Int) {
    val riskMap = mapCaveSystem(target, depth)
    val toolAndTimeMap = mutableMapOf<PointWithTool, Int>()
    val toolsMap = listOf(charArrayOf('C', 'T'), charArrayOf('C', 'N'), charArrayOf('T', 'N'))
    val set = mutableSetOf<PointWithTool>()

    val startingPoint = Point(0, 0)
    val startingTools = toolsMap[riskMap[startingPoint]!!]

    startingTools.forEach { t ->
        val startTime = if (t == 'T') 0 else 7
        val startingPointWithTool = PointWithTool(startingPoint, t)
        toolAndTimeMap[startingPointWithTool] = startTime
        set.add(startingPointWithTool)
    }

    while (set.isNotEmpty()) {
        val current = set.minBy { toolAndTimeMap[it]!! + Math.abs(it.point.X - target.X) + Math.abs(it.point.Y - target.Y) }!!
        set.remove(current)

        // if (current.point == target)
        //   break

        for (adjacent in listOf(Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))) {
            val neighbourPoint = Point(current.point.X + adjacent.first, current.point.Y + adjacent.second)

            if (neighbourPoint.X < 0 || neighbourPoint.Y < 0 || !riskMap.containsKey(neighbourPoint))
                continue


            val acceptableTools = toolsMap[riskMap[neighbourPoint]!!]

            acceptableTools.forEach { t ->
                val newTime = toolAndTimeMap[current]!! + if (t == current.tool) 1 else 8
                val neighbourPointWithTool = PointWithTool(neighbourPoint, t)

                if (toolAndTimeMap.containsKey(neighbourPointWithTool) || (toolAndTimeMap[neighbourPointWithTool]!! > newTime)) {
                    toolAndTimeMap[neighbourPointWithTool] = newTime
                    set.add(neighbourPointWithTool)
                }
            }
        }
    }

    toolAndTimeMap.filter { it.key.point == target }.forEach {
        println(it)
    }
}

fun mapCaveSystem(target: Point, depth: Int): MutableMap<Point, Int> {
    val riskMap = mutableMapOf<Point, Int>()
    val xDim = target.X * 2
    val yDim = target.Y * 2
    val caveSystem = Array(yDim) { intArrayOf(-1) }
    for (y in 0 until yDim) {
        val currentRow = IntArray(xDim)
        for (x in 0 until xDim) {
            val geologicalIndex: Int = if ((x == 0 && y == 0) || (x == target.X && y == target.Y))
                0
            else if (x == 0)
                y * 48271
            else if (y == 0)
                x * 16807
            else
                currentRow[x - 1] * caveSystem[y - 1][x]

            val erosion = (geologicalIndex + depth) % 20183

            currentRow[x] = erosion

            val riskType = erosion % 3

            /*if (x == 0 && y == 0)
        print('M')
    else if (Point(x, y) == target)
        print('X')
    else {
        when (riskType) {
            0 -> print('.')
            1 -> print('=')
            else -> print('|')
        }
    }*/

            riskMap[Point(x, y)] = riskType
        }
        caveSystem[y] = currentRow
        //println()
    }

    // println("Total Risk ${riskMap.values.sum()} for target $target")
    return riskMap
}

fun getRiskType(current: Point, target: Point, depth: Int): Int {
    val erosion = getErosionLevel(current, target, depth)
    return erosion % 3
}

fun getErosionLevel(current: Point, target: Point, depth: Int): Int {
    if (current == Point(0, 0) || current == target)
        return (0 + depth) % 20183
    else if (current.X == 0)
        return ((current.Y * 48271) + depth) % 20183
    else if (current.Y == 0)
        return ((current.X * 16807) + depth) % 20183
    else
        return (getErosionLevel(Point(current.X - 1, current.Y), target, depth) * getErosionLevel(Point(current.X, current.Y - 1), target, depth) + depth) % 20183
}


data class Point(val X: Int, val Y: Int)
data class PointWithTool(val point: Point, val tool: Char)