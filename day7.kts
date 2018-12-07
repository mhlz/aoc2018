
data class Node(
		val id: Char,

		val descendants: MutableList<Char>
)

val nodes = java.io.File("day7.txt").readLines().filter { it.isNotEmpty() }.map { 
	val regex = "Step (.) must be finished before step (.) can begin.".toRegex()
	val match = regex.matchEntire(it)!!
	val desc = match.groupValues[1].toCharArray().first()
	val id = match.groupValues[2].toCharArray().first()
	Node(id, mutableListOf(desc))
}.groupBy { it.id }.mapValues { (_, nodes) ->
	nodes.reduce { acc, it ->
		acc.copy(descendants = (acc.descendants + it.descendants).distinct().toMutableList())
	}
}

fun Map<Char, Node>.getNode(id: Char) = get(id) ?: Node(id, mutableListOf())

val visited = nodes.mapValues { false }.plus(nodes.values.flatMap { it.descendants }.distinct().map { it to false }.toMap()).toMutableMap()

var result = mutableListOf<Node>()

fun topSort(node: Node) {
	visited[node.id] = true
	
	node.descendants.map { nodes.getNode(it) }.forEach {
		if (!visited[it.id]!!)
			topSort(it)
	}

	result.add(node)
}

for (n in nodes.values)
	if (!visited[n.id]!!)
		topSort(n)

result.sortBy { it.descendants.size }

val temp = result.map { it.copy(descendants = it.descendants.toMutableList()) }.toMutableList()

val final = mutableListOf<Char>()
while (result.isNotEmpty()) {
	val ready = result.filter { it.descendants.isEmpty() }.sortedBy { it.id }
	val next = ready.firstOrNull() ?: error(result)

	final.add(next.id)
	result.remove(next)

	for (r in result)
		r.descendants.remove(next.id)
}

println("Part1: " + final.joinToString(""))

result = temp

fun Node.time() = 60 + id.toInt() - 64

data class Worker(
		val id: Int,
		var workingOn: Node? = null,
		var startTime: Int = 0
)

val workers = Array<Worker>(5) { Worker(id = it) }
var seconds = 0
do {
	for (w in workers) {
		if (w.workingOn == null) continue
		if (w.startTime + w.workingOn!!.time() <= seconds) {
			for (r in result) {
				r.descendants.remove(w.workingOn!!.id)
			}
			w.workingOn = null
		}
	}
	val ready = result.filter { it.descendants.isEmpty() }.sortedBy { it.id }.toMutableList()
	//println("$seconds: Workers: ${workers.map { "${it.id}:${it.workingOn?.id ?: "-"}" }.joinToString(" ")}, Ready: ${ready.map { it.id }.joinToString("")}")
	for (w in workers) {
		if (ready.isEmpty()) break
		if (w.workingOn != null) continue
		w.workingOn = ready.removeAt(0)
		w.startTime = seconds
		result.remove(w.workingOn!!)
	}
	seconds++
} while (workers.any { it.workingOn != null }) 
println("Part2: ${seconds - 1}")

