val input = java.io.File("day10.txt").readLines()

data class Point(
		var x: Int,
		var y: Int,
		val vx: Int,
		val vy: Int
)

val points = input.map {
	val regex = "position=<\\s*(-?\\d+),\\s*(-?\\d+)> velocity=<\\s*(-?\\d+),\\s*(-?\\d+)>".toRegex()
	val match = regex.matchEntire(it)!!
	val x = match.groupValues[1].toInt()
	val y = match.groupValues[2].toInt()
	val vx = match.groupValues[3].toInt()
	val vy = match.groupValues[4].toInt()
	Point(x, y, vx, vy)
}

var smallArea: Int? = null
var seconds = 1

fun outputState() {
	val maxY = points.maxBy { it.y }!!.y
	val minY = points.minBy { it.y }!!.y
	val maxX = points.maxBy { it.x }!!.x
	val minX = points.minBy { it.x }!!.x
	val distance = Point(maxX, maxY, 0, 0).distance(Point(minX, minY, 0, 0))
	val xDist = Math.abs(maxX - minX)
	if (smallArea == null) {
		smallArea = distance
		return
	}
	if (smallArea!! > distance && distance < 150 && xDist < 150) {
		smallArea = distance
		println("writing $minX,$minY;$maxX,$maxY -> $distance, $seconds")
		val writer = System.out
		//file.writer().use { writer ->
			for (y in minY..maxY) {
				for (x in minX..maxX) {
					val point = points.find { it.x == x && it.y == y }
					if (point == null) writer.print(".")
					else writer.print("#")
				}
				writer.println()
			}
		//}
		println("written")
		System.out.flush()
		readLine()
	}
}

fun Point.distance(other: Point) = Math.abs(x - other.x) + Math.abs(y - other.y)

while (true) {
	for (p in points) {
		p.x += p.vx
		p.y += p.vy
	}
	outputState()
	seconds++
}

