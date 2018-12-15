import java.io.File

fun main (args: Array<String>){
    val input = File("Demo.txt").readLines()
    val battleGround = Array(input.size) {charArrayOf('.')}
    for (i in 0 until input.size)
        battleGround[i] = input[i].toCharArray()


    battleGround.forEach { println(it) }
}