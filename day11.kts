val input = 3214

fun powerLevel(x: Int, y: Int): Int {
	val rackId = x + 10
	var powerLevel = rackId * y
	powerLevel += input
	powerLevel = powerLevel * rackId
	val s = powerLevel.toString()
	val hundredth = s.substring(s.length - 3, s.length - 3 + 1)
	return hundredth.toInt() - 5
}

var maxX = 0
var maxY = 0
var maxSize = 0
var maxPower = Int.MIN_VALUE

for (size in 1..300) {
	for (x in 1..300) {
		if (x + size > 300) break
		y@for (y in 1..300) {
			if (y + size > 300) break
			var power = 0
			for (xi in x..x + size - 1) {
				for (yi in y..y + size - 1) {
					if (xi > 300 || yi > 300) continue@y
					power += powerLevel(xi, yi)
				}
			}
			if (power > maxPower) {
				maxX = x
				maxY = y
				maxPower = power
				maxSize = size
			}
		}
	}
	println("$maxX,$maxY,$maxSize")
}



