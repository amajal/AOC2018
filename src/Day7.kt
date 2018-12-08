import java.io.File

fun main(args: Array<String>) {
    val stepMap = mutableMapOf<Char, MutableList<Char>>()
    File("Input.txt").useLines { l ->
        l.forEach {
            val parent = it[5]
            val child = it[it.length - 12]
            if (!stepMap.containsKey(parent))
                stepMap[parent] = mutableListOf()

            stepMap[parent]!!.add(child)
            stepMap[parent]!!.sort()
        }
    }

    doPart1(HashMap(stepMap))
    doPart2(HashMap(stepMap), 5)

}

fun doPart2(map: MutableMap<Char, List<Char>>, numberOfWorkers: Int) {
    val workers = Array(numberOfWorkers) { MyPair('.', -1) }
    val sb = StringBuilder()

    val steps = map.keys.toMutableSet()
    var ticker = 0

    while (steps.any()) {
        for (i in 0 until workers.count()) {
            if (workers[i].tick == 0) {
                sb.append(workers[i].char)
                if (map.containsKey(workers[i].char))
                    steps.addAll(map[workers[i].char]!!.toList())
                steps.remove(workers[i].char)
                map.remove(workers[i].char)
                workers[i].char = '.'
                workers[i].tick = -1
            }

            val availableSteps = steps.filter { s ->
                map.values.filter { it -> it.contains(s) }.none()
                        && !workers.map { it -> it.char }.contains(s)
            }.sorted().toMutableList()

            if (workers[i].char == '.' && availableSteps.any()) {
                val step = availableSteps.first()
                workers[i] = MyPair(step, step.toInt() - 65 + 60)
                availableSteps.removeAt(0)
            } else if (workers[i].tick > 0)
                workers[i].tick--

        }
        ticker ++
    }

    print("Second Part -> ")
    print("time is ${ticker - 1}s ")
    println("and the sequence is $sb")
}


fun doPart1(map: HashMap<Char, List<Char>>) {
    print("First Part -> ")
    while (map.any()) {
        val nextStep = map.keys.filter { s ->
            map.values.filter { it -> it.contains(s) }.none()
        }.toList().sorted()[0]

        print(nextStep)

        if (map.size == 1) {
            map[nextStep]!!.forEach { print(it) }
        }

        map.remove(nextStep)
    }
    println()
}

data class MyPair(var char: Char, var tick: Int)