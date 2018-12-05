#!/usr/bin/env kotlinc -script

val input = java.io.File("day3.txt")

data class Square(
	val id: Set<Int>,
	val x: Int,
	val y: Int,
	val width: Int,
	val height: Int
) {
	val area = width * height

	fun overlap(other: Square): Square? {
		val left1 = x
		val right2 = other.x + other.width
		val right1 = x + width
		val left2 = other.x

		val top1 = y
		val bottom2 = other.y + other.height
		val bottom1 = y + height
		val top2 = other.y

		if (left1 < right2 &&
			right1 > left2 &&
			top1 < bottom2 &&
			bottom1 > top2
		) {
			val left = if (left1 in (left2..right2))
				left1
			else left2
			val top = if (top1 in (top2..bottom2))
				top1
			else top2

			val right = if (right2 in (left1..right1))
				right2
			else right1
			val bottom = if (bottom2 in (top1..bottom1))
				bottom2
			else bottom1

			val width = right - left
			val height = bottom - top

			return Square(id + other.id, left, top, width, height)
		}

		return null
	}

	fun split(): List<Square> {
		val ret = mutableListOf<Square>()
		for (x in x until x + width) {
			for (y in y until y + height) {
				ret.add(Square(id, x, y, 1, 1))
			}
		}
		return ret
	}
}

val squares = input.readLines().map {
	val regex = "#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)".toRegex()
	val match = regex.matchEntire(it)!!
	val id = match.groupValues[1].toInt()
	val x = match.groupValues[2].toInt()
	val y = match.groupValues[3].toInt()
	val width = match.groupValues[4].toInt()
	val height = match.groupValues[5].toInt()

	Square(setOf(id), x, y, width, height)
}

val overlaps = mutableSetOf<Square>()

for (a in squares) {
	for (b in squares) {
		if (a == b) continue
		val overlap = a.overlap(b) ?: continue
		overlaps.addAll(overlap.split())
	}
}

val t = overlaps.toList().distinctBy { "${it.x}-${it.y}" }

println(t.size)

val overlappingIds = overlaps.flatMap { it.id }.toSet()
val ids = squares.flatMap { it.id }.toSet()

val result = ids - overlappingIds

println(result)

