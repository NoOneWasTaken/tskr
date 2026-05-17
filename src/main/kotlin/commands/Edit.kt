package noonewastaken.tskr.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int

import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.terminal.prompt
import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextStyles.*

import kotlinx.datetime.LocalDateTime

import noonewastaken.tskr.models.TaskDueFormat
import noonewastaken.tskr.storage.loadRegistry
import noonewastaken.tskr.storage.saveRegistry
import kotlin.reflect.typeOf

class Edit: CliktCommand() {
    override fun help(context: Context): String = "Edit an existing task."

    val id by argument(help = "ID of the task you want to edit.").int()
    val argTask by option("-t", "--task", help = "The task you want to add to your list.")
    val argNote by option("-n", "--note", help = "Add additional information to your task.")
    val argDue by option("-d", "--due", help = "Due time period for the task (Format: YYYY-MM-DD HH:mm)")

    val t = Terminal()

    override fun run() {
        val taskRegistry = loadRegistry()
        val taskById = taskRegistry.tasks.find { it.id == id }

        if (taskById == null) {
            t.println(brightRed("[ERROR]") + " Task with ID ${bold(underline(id.toString()))} not found.")
            kotlin.system.exitProcess(1)
        } else if (taskById.done) {
            t.println(brightRed("[ERROR]") + " Cannot edit a completed task. Use undo first.")
            kotlin.system.exitProcess(1)
        } else {

            val task = argTask ?: run {
                t.println(dim("Current task: ${taskById.task}"))
                t.prompt("Updated task (enter to keep)", default = null).takeIf { !it.isNullOrEmpty() } ?: taskById.task
            }

            val due = argDue ?: run {
                t.println(dim("Current due: ${taskById.due}"))
                t.prompt("Updated due (enter to keep)", default = null)
            }

            val note = if (taskById.note != null) {
                argNote ?: run {
                    t.println(dim("Current note: ${taskById.note}"))
                    t.prompt("Updated note (enter to keep)", default = null).takeIf { !it.isNullOrEmpty() } ?: taskById.note
                }
            } else argNote

            try {
                val parsedDue = if (due.isNullOrEmpty()) taskById.due else LocalDateTime.parse(due, TaskDueFormat)

                val updatedTaskList = taskRegistry.tasks.map {
                    if (it.id == id) it.copy(task = task, due = parsedDue, note = note) else it
                }
                saveRegistry(taskRegistry.copy(tasks = updatedTaskList))

                t.println(brightGreen("[SUCCESS]") + " Task edited successfully!")
            } catch (_: IllegalArgumentException) {
                t.println(brightRed("[ERROR]") + " Invalid date format, please try again.")
                kotlin.system.exitProcess(1)
            }
        }
    }
}