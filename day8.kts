val input = java.io.File("day8.txt").readText().trim()

val numbers = input.split(" ").map { it.toInt() }

data class Node(
		val children: List<Node>,
		val metaData: List<Int>
)

var cpos = 0

fun consumeNode(): Node {
	val children = numbers[cpos++]
	val numberOfMetaData = numbers[cpos++]

	val nodes = (0 until children).map {
		consumeNode()
	}

	val metaData = (0 until numberOfMetaData).map {
		numbers[cpos++]
	}

	return Node(nodes, metaData)
}

fun Node.metaSum(): Int = metaData.sum() + children.sumBy { it.metaSum() }

val root = consumeNode()

val sum = root.metaSum()
println(sum)

fun Node.value(): Int = when {
	children.isEmpty() -> metaSum()
	else -> {
		metaData.mapNotNull {
			children.getOrNull(it - 1)
		}.sumBy { it.value() }
	}
}

println(root.value())

