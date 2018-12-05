#!/usr/bin/env kotlinc -script

val lines = java.io.File("day1.txt").readLines()

val changes = lines.map { it.toInt() }

val result = changes.sum()
println(result)

var current = 0
val reached = mutableSetOf<Int>(0)

outer@while (true) {
	for (it in changes) {
		current += it
		if (current in reached) {
			println("Result: $current")
			break@outer
		}
		reached.add(current)
	}
}

