import java.io.File

fun main(args:Array<String>)
{
    val stepMap = mutableMapOf<Char,MutableList<Char>>()
    File("Demo.txt").useLines { l ->
        l.forEach {
            val parent = it[5]
            val child = it[it.length - 12]
            if (!stepMap.containsKey(parent))
                stepMap[parent] = mutableListOf()

            stepMap[parent]!!.add(child)
            stepMap[parent]!!.sort()
        }
    }
    println(stepMap)


    while(stepMap.any()) {
        val nextStep = stepMap.keys.filter { s ->
            stepMap.values.filter { it -> it.contains(s) }.none()
        }.toList().sorted()[0]

        print(nextStep)

        if (stepMap.size == 1)
        {
            stepMap[nextStep]!!.forEach { print(it) }
        }

        stepMap.remove(nextStep)
    }
}