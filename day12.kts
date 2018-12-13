val input = java.io.File("day12.txt").readLines()

val inStateStr = input.first().removePrefix("initial state: ")
println(inStateStr)

val initialState = input.first().removePrefix("initial state: ").map { it == '#' }
val rules = input.drop(2).map {
	val (initial, result) = it.split(" => ").map { it.map { it == '#' } }
	initial to result.first()
}.toMap()
println(rules)

var state = initialState
var zero = 0
var g = 0
var stop = false
var count20 = 0

while (true) {
	val padStart = if (!state[0] && !state[1]) listOf() else {
		zero += 2
		listOf(false, false)
	}
	val padEnd = if (!state.last() && !state[state.size - 2]) listOf() else listOf(false, false)
	val padState = padStart + state + padEnd
	val nextState = padState.toMutableList()
	for (i in 0 until padState.size) {
		val slice = (listOf(false, false) + padState + listOf(false, false)).slice(i..i + 4)
		val result = rules[slice] ?: false
		nextState[i] = result
	}
	val stateStr = state.dropWhile { !it }.dropLastWhile { !it }.joinToString("") { if(it) "#" else "." }
	val nextStateStr = nextState.dropWhile { !it }.dropLastWhile { !it }.joinToString("") { if(it) "#" else "." }
	println(stateStr)
	println(nextStateStr)
	state = nextState
	println()
	g++
	val count = state.mapIndexed { i, it -> if (it) i - zero else 0 }.sum()
	if (g == 20) count20 = count
	if (stateStr == nextStateStr) break
}

val count = state.mapIndexed { i, it -> if (it) i - zero else 0 }.sum()
val left = 50000000000 - g
val finalCount = count.toLong() + left.toLong() * 45.toLong()
println("Part 1: $count20")
println("Part 2: $finalCount")

