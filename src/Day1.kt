import java.io.File
import java.io.InputStream
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    val inputStream: InputStream = File("Input.txt").inputStream()
    val listOfNumbers = mutableListOf<Int>()
    val setOfTotals = mutableSetOf<Int>()
    var total = 0

    inputStream.bufferedReader().useLines { lines ->
        lines.forEach {
            total += it.toInt()
            listOfNumbers.add(it.toInt())
        }
    }

    println("total is $total")

    var index = 0
    total = 0

    println("Calculating repeat frequency ...")
    val time = measureTimeMillis {
        while (true) {
            total += listOfNumbers[index]

            if (total in setOfTotals) {
                println("Repeat at $total")
                break
            }

            setOfTotals.add(total)

            if (++index == listOfNumbers.size)
                index = 0
        }
    }

    println("time taken to calculate repeat $time ms")
}
