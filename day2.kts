#!/usr/bin/env kotlinc -script

val file = java.io.File("day2.txt")

val lines = file.readLines()

//val lines = listOf("bababc")

val result = lines.map {
	val sorted = it.toCharArray().sorted()
	var current = sorted[0]
	var count = 0
	val list = mutableListOf<Int>()
	for (c in sorted) {
		if (c != current) {
			list.add(count)
			count = 0
			current = c
		}
		count++
	}
	list.add(count)
	list
}

val final = result.count { 2 in it } * result.count { 3 in it }
println("Part 1: $final")

test@for (test in lines) {
	candidate@ for (candidate in lines) {
		var errors = 0
		for ((i, tc) in test.withIndex()) {
			val cc = candidate[i]
			if (tc != cc)
				errors++
			if (errors > 1)
				continue@candidate
		}
		if (errors == 1) {
			println(test)
			println(candidate)
			break@test
		}
	}
}


