package noonewastaken.tskr.models

import kotlinx.datetime.format.char
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Task(val id: Int, val task: String, val note: String? = null, val due: LocalDateTime, val done: Boolean)
@Serializable
data class TaskRegistry(val lastId: Int = 0, val tasks: List<Task> = listOf<Task>())

val TaskDueFormat = LocalDateTime.Format {
    year()
    char('-')
    monthNumber()
    char('-')
    dayOfMonth()
    char(' ')
    hour()
    char(':')
    minute()
}