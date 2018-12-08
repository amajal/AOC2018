import java.io.File
import java.util.*

fun main(args: Array<String>) {
    val input: Queue<Int> = LinkedList<Int>()
    File("Input.txt").readText().split(" ").forEach { input.add(it.toInt()) }
    println("Part1 -> ${sumMetaDataEntries(LinkedList(input), 0)}")
    println("Part2 -> ${parseTree(LinkedList(input)).value}")
}

fun parseTree(tree: Queue<Int>): TreeNode {
    val numberOfChildren = tree.remove()
    val numberOfMetaEntries = tree.remove()
    val currentNode = TreeNode(mutableListOf(), 0)

    if (numberOfChildren == 0) {
        repeat(numberOfMetaEntries) { currentNode.value += tree.remove() }
        return currentNode
    }

    val listOfChildren = mutableListOf<TreeNode>()
    repeat(numberOfChildren) { listOfChildren.add(parseTree(tree)) }
    repeat(numberOfMetaEntries) {
        val index = tree.remove() - 1
        if (index < listOfChildren.size)
            currentNode.value += listOfChildren.elementAt(index).value
    }

    return currentNode
}

fun sumMetaDataEntries(tree: Queue<Int>, sum: Int): Int {
    val numberOfChildren = tree.remove()
    val numberOfMetaEntries = tree.remove()
    var currentSum = sum

    repeat(numberOfChildren) { currentSum += sumMetaDataEntries(tree, 0) }
    repeat(numberOfMetaEntries) { currentSum += tree.remove() }

    return currentSum
}

data class TreeNode(var children: List<TreeNode>, var value: Int)