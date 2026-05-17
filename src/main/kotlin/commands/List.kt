package noonewastaken.tskr.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.mordant.rendering.BorderType

import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextStyle
import com.github.ajalt.mordant.rendering.TextStyles.*
import com.github.ajalt.mordant.table.table

import noonewastaken.tskr.storage.loadRegistry

class List: CliktCommand() {
    override fun help(context: Context): String = "List all existing tasks"

    val done by option("-d", "--done", help = "List all the tasks that are completed.").flag()
    val pending by option("-p", "--pending", help = "List all the tasks that are pending.").flag()

    val t = Terminal()

    override fun run() {
        if (done && pending) {
            t.println(brightRed("[ERROR]") + " Cannot use both flags together.")
            kotlin.system.exitProcess(1)
        }

        val taskRegistry = loadRegistry()
        val filteredTaskList = when {
            done -> taskRegistry.tasks.filter { it.done }
            pending -> taskRegistry.tasks.filter { !it.done }
            else -> taskRegistry.tasks
        }

        val table = table {
            borderType = BorderType.ASCII
            borderStyle = gray
            header {
                row {
                    cell("ID") { style = bold + cyan }
                    cell("Task") { style = bold + cyan }
                    cell("Note") { style = bold + cyan }
                    cell("Due") { style = bold + cyan }
                    cell("Status") { style = bold + cyan }
                }
            }
            body {
                filteredTaskList.forEach { task ->
                    row {
                        val rowStyle = if (task.done) dim else white
                        cell(task.id)
                        cell(if (task.done) strikethrough(task.task) else task.task)
                        cell(task.note ?: "-")
                        cell(task.due)
                        cell(if (task.done) "Done" else "Pending")
                    }
                }
            }
        }


        t.println(table)
    }
}