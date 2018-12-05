#!/usr/bin/env kotlinc -script

val input = java.io.File("day5.txt").readText().trim()

fun String.react(): String {
	val alphabet = "abcdefghijklmnopqrstuvwxyz"
	var current = this
	for (c in alphabet) {
		current = current.replace("$c${c.toUpperCase()}", "")
		current = current.replace("${c.toUpperCase()}$c", "")
	}
	return current
}

fun String.fullReact(): String {
	var current = this
	do {
		val last = current
		current = current.react()
	} while (current.length != last.length)
	return current
}

val part1 = input.fullReact()

println(part1.length)

val alphabet = "abcdefghijklmnopqrstuvwxyz"
var shortest = input
for (c in alphabet) {
	print(c)
	System.out.flush()
	val test = input.replace(c.toString(), "").replace(c.toString().toUpperCase(), "")
	val result = test.fullReact()
	if (result.length < shortest.length) {
		print(" -> new shortest length")
		shortest = result
	}
	println()
}

println(shortest.length)

