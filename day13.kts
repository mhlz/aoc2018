val input = java.io.File("day13.txt").readLines()

enum class Track {
	Horizontal,
	Vertical,
	Intersection,
	NESWTurn,
	NWSETurn
}

enum class Direction {
	North,
	East,
	South,
	West
}

enum class IntersectionTurn {
	Left,
	Straight,
	Right
}

val width = input.first().length
val height = input.size

val tracks = Array<Array<Track?>>(width) { Array<Track?>(height) { null } }

data class Cart(
		var x: Int,
		var y: Int,
		var direction: Direction,
		var nextIntersection: IntersectionTurn = IntersectionTurn.Right
) {
	fun nextPosition(): Pair<Int, Int> = when (direction) {
		Direction.North -> x to y - 1
		Direction.South -> x to y + 1
		Direction.West -> x - 1 to y
		Direction.East -> x + 1 to y
	}

	fun nextDirection(nextTrack: Track) = when (nextTrack) {
		Track.Horizontal -> direction
		Track.Vertical -> direction
		Track.Intersection -> {
			nextIntersection = when (nextIntersection) {
				IntersectionTurn.Left -> IntersectionTurn.Straight
				IntersectionTurn.Straight -> IntersectionTurn.Right
				IntersectionTurn.Right -> IntersectionTurn.Left
			}
			when (direction) {
				Direction.East -> when (nextIntersection) {
					IntersectionTurn.Left -> Direction.North
					IntersectionTurn.Right -> Direction.South
					IntersectionTurn.Straight -> direction
				}
				Direction.West -> when (nextIntersection) {
					IntersectionTurn.Left -> Direction.South
					IntersectionTurn.Right -> Direction.North
					IntersectionTurn.Straight -> direction
				}
				Direction.North -> when (nextIntersection) {
					IntersectionTurn.Left -> Direction.West
					IntersectionTurn.Right -> Direction.East
					IntersectionTurn.Straight -> direction
				}
				Direction.South -> when (nextIntersection) {
					IntersectionTurn.Left -> Direction.East
					IntersectionTurn.Right -> Direction.West
					IntersectionTurn.Straight -> direction
				}
			}
		}
		Track.NESWTurn -> when (direction) {
			Direction.North -> Direction.East
			Direction.South -> Direction.West
			Direction.West -> Direction.South
			Direction.East -> Direction.North
		}
		Track.NWSETurn -> when (direction) {
			Direction.North -> Direction.West
			Direction.South -> Direction.East
			Direction.East -> Direction.South
			Direction.West -> Direction.North
		}
	}

}

val carts = mutableListOf<Cart>()

for ((y, line) in input.withIndex()) {
	for ((x, c) in line.withIndex()) {
		tracks[x][y] = when (c) {
			'-' -> Track.Horizontal
			'|' -> Track.Vertical
			'+' -> Track.Intersection
			'/' -> Track.NESWTurn
			'\\' -> Track.NWSETurn
			'>' -> Track.Horizontal
			'<' -> Track.Horizontal
			'^' -> Track.Vertical
			'v' -> Track.Vertical
			else -> null
		}
		val cart = when (c) {
			'>' -> Cart(x, y, Direction.East)
			'^' -> Cart(x, y, Direction.North)
			'v' -> Cart(x, y, Direction.South)
			'<' -> Cart(x, y, Direction.West)
			else -> null
		}

		if (cart != null) carts.add(cart)
	}
}

fun printState(hx: Int, hy: Int) {
	for (y in 0 until height) {
		y@for (x in 0 until width) {
			if (x == hx && y == hy) {
				print("H")
				continue
			}
			for (c in carts) {
				if (c.x == x && c.y == y) {
					when (c.direction) {
						Direction.North -> print("^")
						Direction.South -> print("v")
						Direction.West -> print("<")
						Direction.East -> print(">")
					}
					continue@y
				}
			}
			when (tracks[x][y]) {
				Track.Horizontal -> print("-")
				Track.Vertical -> print("|")
				Track.NESWTurn -> print("/")
				Track.NWSETurn -> print("\\")
				Track.Intersection -> print("+")
				else -> print(" ")
			}
		}
		println()
	}
}

var ticks = 0
ticks@while (true) {
	carts.sortWith(compareBy<Cart> { it.x }.thenBy { it.y })
	val toRemove = mutableListOf<Cart>()
	for (c in carts) {
		val (nx, ny) = c.nextPosition()
		val nextTrack = tracks[nx][ny]
		if (nextTrack == null) {
			printState(c.x, c.y)
			error(c)
		}
		val nextDirection = c.nextDirection(nextTrack)
		c.direction = nextDirection
		c.x = nx
		c.y = ny
		for (a in carts) {
			for (b in carts) {
				if (a != b && a.x == b.x && a.y == b.y) {
					println("$a,$b")
					toRemove.add(a)
					toRemove.add(b)
				}
			}
		}
	}
	carts.removeAll(toRemove)

	if (carts.size == 1) {
		println(carts)
		break@ticks
	}
	ticks++
}

println(ticks)

