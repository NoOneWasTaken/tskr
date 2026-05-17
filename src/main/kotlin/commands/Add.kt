package noonewastaken.tskr.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.mordant.rendering.AnsiLevel

import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.terminal.prompt
import com.github.ajalt.mordant.rendering.TextColors.*

import kotlinx.datetime.LocalDateTime

import noonewastaken.tskr.models.Task
import noonewastaken.tskr.models.TaskDueFormat
import noonewastaken.tskr.storage.loadRegistry
import noonewastaken.tskr.storage.saveRegistry

class Add: CliktCommand() {
    override fun help(context: Context): String = "Add tasks"

    val argTask by argument(help = "The task you want to add to your list.").optional()
    val argNote by option("-n", "--note", help = "Add additional information to your task.")
    val argDue by option("-d", "--due", help = "Due time period for the task (Format: YYYY-MM-DD HH:mm)")

    val t = Terminal()

    override fun run() {
        val task = argTask ?: t.prompt("Enter your task", default = null)
        val due = argDue ?: t.prompt("Enter due time period for the task (Format: YYYY-MM-DD HH:mm)", default = null)

        if (task.isNullOrEmpty() || due.isNullOrEmpty()) {
            t.println(brightRed("[ERROR]") + " Missing values, please try again (task or due duration might be empty).")
            kotlin.system.exitProcess(1)
        }

        try {
            val parsedDue = LocalDateTime.parse(due as CharSequence, TaskDueFormat)
            val taskRegistry = loadRegistry()
            val newTask = Task(taskRegistry.lastId + 1, task, argNote, parsedDue, false)

            saveRegistry(taskRegistry.copy(tasks = taskRegistry.tasks + newTask, lastId = taskRegistry.lastId + 1))
            t.println(brightGreen("[SUCCESS]") + " Task saved successfully!")
        } catch (_: IllegalArgumentException) {
            t.println(brightRed("[ERROR]") + " Invalid date format, please try again.")
            kotlin.system.exitProcess(1)
        }
    }
}