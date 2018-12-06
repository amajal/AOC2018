import java.io.File
import java.io.InputStream

fun main(args: Array<String>) {
    doPart1()
    doPart2()
}

fun doPart2() {
    val inputStream: InputStream = File("Input1.txt").inputStream()
    val boxIDs = mutableListOf<String>()

    inputStream.bufferedReader().useLines { lines -> boxIDs.addAll(lines) }

    boxIDs.forEach { id ->
        boxIDs.filter { bid -> bid != id }.forEach {
            if (diffInCharacters(it, id) == 1) {
                OutputCommonCharacters(it, id)
            }
        }
    }

}

fun OutputCommonCharacters(id1: String, id2: String) {
    val commonCharacters = StringBuilder()

    for (i in 0 until id1.length)
    {
        if (id1[i] == id2[i])
            commonCharacters.append(id1[i])
    }

    println("Common characters for $id1 and $id2 are $commonCharacters")
}

fun diffInCharacters(id1: String, id2: String): Int {
    var diff = 0

    for (i in 0 until id1.length) {
        if (id1[i] != id2[i])
            diff++
    }
    return diff
}

fun doPart1() {
    val inputStream: InputStream = File("Input.txt").inputStream()
    var stringsWithDoubles = 0
    var stringsWithTriples = 0

    inputStream.bufferedReader().useLines { lines ->
        lines.forEach {
            if (hasFrequencyOf(it, 2))
                stringsWithDoubles++

            if (hasFrequencyOf(it, 3))
                stringsWithTriples++
        }
    }
    println("Checksum == ${stringsWithTriples * stringsWithDoubles}")
}

fun hasFrequencyOf(phrase: String, freq: Int): Boolean {
    val listOfLetters = HashSet<Char>()

    for (letter in phrase) {
        if (listOfLetters.contains(letter))
            continue

        if (phrase.filter { s -> s == letter }.count() == freq)
            return true

        listOfLetters.add(letter)
    }

    return false
}


