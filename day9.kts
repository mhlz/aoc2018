
val players = 416
val marbles = 7197500

val playerScores = LongArray(players) { 0 }

val circle = ArrayList<Int>(marbles)

fun List<Int>.getMarble(index: Int) = get((index + size) % size)
fun MutableList<Int>.addMarble(index: Int, marble: Int) = add((index + size) % size, marble)
circle.add(0)

var currentMarble = 0 
var currentPlayer = 0

var progress = 0
for (i in 1..marbles) {
	progress++
	if (progress == 10_000) {
		print("${i.toFloat() * 100F / marbles.toFloat()}%\r")
		progress = 0
	}
	if (i % 23 == 0) {
		playerScores[currentPlayer] += i.toLong()
		val marble = circle.getMarble(currentMarble - 7)
		currentMarble = ((currentMarble - 7) + circle.size) % circle.size
		circle.removeAt(currentMarble)
		playerScores[currentPlayer] += marble.toLong()
		currentPlayer = (currentPlayer + 1) % players
		continue
	}
	currentMarble = (currentMarble + 2) % circle.size
	circle.addMarble(currentMarble, i)
	currentPlayer = (currentPlayer + 1) % players
}

val winningScore = playerScores.max()!!
val index = playerScores.indexOf(winningScore)

println("$index: $winningScore")

