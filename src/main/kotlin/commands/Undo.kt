package noonewastaken.tskr.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.mordant.rendering.TextColors.brightRed

import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextStyles.*

import noonewastaken.tskr.storage.loadRegistry
import noonewastaken.tskr.storage.saveRegistry

class Undo: CliktCommand() {
    override fun help(context: Context): String = "Mark a task as undone."

    val id by argument(help = "ID of the task you want to mark as undone.").int()
    val t = Terminal()

    override fun run() {
        val taskRegistry = loadRegistry()
        val taskById = taskRegistry.tasks.find { it.id == id }

        if (taskById == null) {
            t.println(brightRed("[ERROR]") + " Task with ID ${bold(underline(id.toString()))} not found.")
            kotlin.system.exitProcess(1)
        } else {
            val updatedTasks = taskRegistry.tasks.map {
                if (it.id == id) it.copy(done = false) else it
            }
            saveRegistry(taskRegistry.copy(tasks = updatedTasks))
            t.println(brightGreen("[SUCCESS]") + " Task marked as undone!")
        }
    }
}