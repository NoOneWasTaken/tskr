package noonewastaken.tskr.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.choice

import java.io.File
import kotlinx.datetime.format
import kotlinx.serialization.json.Json

import noonewastaken.tskr.models.TaskDueFormat
import noonewastaken.tskr.storage.loadRegistry

import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.rendering.TextColors.*
import java.io.IOException


class Export: CliktCommand() {
    override fun help(context: Context): String = "Export your tasks to a file."

    val output by option("-o", "--output", help = "Path to output directory.").required()
    val format by option("-f", "--format", help = "Export format (JSON, Text File, CSV)").choice("json", "txt", "csv").required()

    val t = Terminal()

    override fun run() {
        try {
            val outputFile = File(output, "tskr_task_list.$format")
            val taskList = loadRegistry().tasks

            when (format) {
                "json" -> {
                    outputFile.writeText(Json.encodeToString(taskList))
                }
                "txt" -> {
                    val outputTextFormat = taskList.mapIndexed { index, it ->
                        "[${index + 1}] ${it.task} - Due: ${it.due.format(TaskDueFormat)} - ${if (it.done) "Done" else "Pending"} ${if (!it.note.isNullOrEmpty()) "(${it.note})" else ""}"
                    }
                    outputFile.writeText(outputTextFormat.joinToString(separator = "\n"))
                }
                "csv" -> {
                    outputFile.writeText("id,task,note,due,status\n")
                    val outputCSVFormat = taskList.mapIndexed { index, it ->
                        "${index + 1},\"${it.task}\",\"${it.note}\",\"${it.due.format(TaskDueFormat)}\",\"${if (it.done) "Done" else "Pending"}\""
                    }
                    outputFile.appendText(outputCSVFormat.joinToString(separator = "\n"))
                }
            }

            t.println(brightGreen("[SUCCESS]") + " Tasks exported to ${outputFile.absolutePath}")
        }
        catch (_: IOException) {
            t.println(brightRed("[ERROR]") + " Could not write to ${output}, permission denied or invalid path.")
        }
    }
}