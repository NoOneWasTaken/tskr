package noonewastaken.tskr.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.mordant.input.interactiveSelectList
import com.github.ajalt.mordant.rendering.TextColors.brightRed

import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextStyles.*
import noonewastaken.tskr.storage.ensureInitialized

import noonewastaken.tskr.storage.loadRegistry
import noonewastaken.tskr.storage.saveRegistry

class Delete: CliktCommand() {
    override fun help(context: Context): String = "Delete a task from the task list."

    val id by argument(help = "ID of the task you want to mark as done.").int()
    val t = Terminal()

    override fun run() {
        val taskRegistry = loadRegistry()
        val taskById = taskRegistry.tasks.find { it.id == id }

        if (taskById == null) {
            t.println(brightRed("[ERROR]") + " Task with ID ${bold(underline(id.toString()))} not found.")
            kotlin.system.exitProcess(1)
        } else {
            val confirmation = t.interactiveSelectList {
                addEntry("Yes, delete it")
                addEntry("No, keep it")
                title("Delete \"${taskById.task}\"?")
                showInstructions(false)
            }
            if (confirmation == null || confirmation.startsWith("No")) {
                t.println("Cancelled.")
                kotlin.system.exitProcess(0)
            }

            saveRegistry(taskRegistry.copy(tasks = taskRegistry.tasks.filter { it.id != id }))
            t.println(brightGreen("[SUCCESS]") + " Task deleted!")
        }
    }
}