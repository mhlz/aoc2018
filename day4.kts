#!/usr/bin/env kotlinc -script

data class GuardLog(
		val guardId: Int? = null,
		val event: Event,
		val hour: Int,
		val minute: Int,
		val month: Int,
		val day: Int
) : Comparable<GuardLog> {
	enum class Event {
		Starts,
		FallsAsleep,
		WakesUp
	}

	override operator fun compareTo(other: GuardLog): Int {
		if (month != other.month) return month - other.month
		if (day != other.day) return day - other.day
		if (hour != other.hour) return hour - other.hour
		if (minute != other.minute) return minute - other.minute
		if (guardId != other.guardId) return (guardId ?: 0) - (other.guardId ?: 0)
		if (event != other.event) return event.compareTo(other.event)

		return 0
	}
}

data class Guard(
		val guardId: Int,
		val minutesAsleep: Int,
		val minutesAwake: Int,
		val sleepMap: Map<Int, Int>
) {
	enum class Status {
		Awake,
		Asleep
	}
}

val input = java.io.File("day4.txt").readLines().map {
	val regex = "\\[\\d+-(\\d+)-(\\d+) (\\d\\d):(\\d\\d)\\] (Guard #(\\d+) begins shift|falls asleep|wakes up)".toRegex()
	val match = regex.matchEntire(it)!!
	val month = match.groupValues[1].toInt()
	val day = match.groupValues[2].toInt()
	val hour = match.groupValues[3].toInt()
	val minute = match.groupValues[4].toInt()

	val guardId = match.groupValues.getOrNull(6)?.takeIf { it.isNotEmpty() }?.toInt()

	val event = when (match.groupValues[5]) {
		"falls asleep" -> GuardLog.Event.FallsAsleep
		"wakes up" -> GuardLog.Event.WakesUp
		else -> GuardLog.Event.Starts
	}

	GuardLog(guardId, event, hour, minute, month, day)
}.sorted()

var currentGuardId: Int? = null

val logs = input.map {
	if (it.guardId != null) {
		currentGuardId = it.guardId
		it
	} else {
		it.copy(guardId = currentGuardId)
	}
}

val guards: List<Guard> = logs.groupBy { it.guardId }.map { (guardId, allLogs) ->
	allLogs.groupBy { "${it.month}-${it.day}" }.map { (_, logs) ->
		var lastStatus = 0
		var minutesAsleep = 0
		var minutesAwake = 0
		val sleepMap = mutableMapOf<Int, Int>()
		for (log in logs) {
			if (log.event == GuardLog.Event.Starts) continue
			when (log.event) {
				GuardLog.Event.FallsAsleep -> minutesAwake += log.minute - lastStatus
				GuardLog.Event.WakesUp -> {
					minutesAsleep += log.minute - lastStatus
					for (i in lastStatus until log.minute) {
						sleepMap[i] = 1
					}
				}
			}
			lastStatus = log.minute
		}
		Guard(guardId!!, minutesAsleep, minutesAwake, sleepMap)
	}.reduce { acc, it -> acc.copy(
			minutesAsleep = acc.minutesAsleep + it.minutesAsleep, 
			minutesAwake = acc.minutesAwake + it.minutesAwake,
			sleepMap = acc.sleepMap.mapValues { (minute, value) -> (it.sleepMap[minute] ?: 0) + value } + it.sleepMap.filterKeys { it !in acc.sleepMap }
	) }
}.toList()

val sorted = guards.sortedByDescending { it.minutesAsleep }

val result = sorted.first().guardId * sorted.first().sleepMap.entries.sortedByDescending { it.value }.first().key
println(result)

val part2 = guards.sortedByDescending { it.sleepMap.values.max() }.first()
println(part2)

