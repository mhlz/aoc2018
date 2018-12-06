val input = java.io.File("day6.txt")

var c = 'A'

data class Point(
		val id: Char,
		val x: Int,
		val y: Int
) {
	var area = 0
}

val points = input.readLines().map {
	val (x, y) = it.split(", ").map { it.toInt() }
	c++
	Point(id = c, x = x, y = y)
}

val infinitePoints = mutableSetOf<Point>()

val minX = points.minBy { it.x }!!.x
val minY = points.minBy { it.y }!!.y
val maxX = points.maxBy { it.x }!!.x
val maxY = points.maxBy { it.y }!!.y

var region = 0

for (y in 0..maxY + 1) {
	for (x in 0..maxX + 1) {
		val distances = points.map { it to Math.abs(it.x - x) + Math.abs(it.y - y) } 
		val totalDistance = distances.sumBy { (_, it) -> it }
		if (totalDistance < 10000)
			region++
		val closest = distances.fold(listOf<Pair<Point, Int>>()) { acc, (it, dist) ->
			when {
				acc.isEmpty() -> listOf(it to dist)
				acc.first().second == dist -> acc + (it to dist)
				acc.first().second > dist -> listOf(it to dist)
				else -> acc
			}
		}
		if (y == 0 || y > maxY || x == 0 || x > maxX)
			infinitePoints.addAll(closest.map {it.first})
		val point = closest.singleOrNull()?.first ?: continue
		point.area++
	}
}

val finitePoints = points - infinitePoints
val result = finitePoints.maxBy { it.area }!!
println("Infinite Points: $infinitePoints")
println(result)
println(result.area)
println(region)

