package noonewastaken.tskr.storage

import com.github.ajalt.mordant.input.interactiveSelectList
import java.io.File

import kotlinx.serialization.json.Json

import noonewastaken.tskr.models.TaskRegistry

import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextStyles.*
import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.terminal.prompt

val userDir: String? = System.getProperty("user.home")
val dataDirectory = File(userDir, ".tskr")
val dataFile = File(dataDirectory, "task_registry.json")

val t = Terminal()
val JSONOperator = Json { encodeDefaults = true }

fun doctorDataDirectory(verbose: Boolean = false) {
    if (!dataDirectory.exists()) {
        t.println(brightYellow("[WARN]") + " Data directory seems to be missing, regenerating the data directory.")
        dataDirectory.mkdir()
    }

    if (verbose) {
        t.println(brightGreen("[SUCCESS]") + " Data directory found.")
        doctorDataFile(true)
    }
}

private fun resetRegistry() {
    val initialJSONData = JSONOperator.encodeToString(TaskRegistry())
    dataFile.writeText(initialJSONData)
}

fun doctorDataFile(verbose: Boolean = false) {
    if (!dataFile.exists()) {
        val response = t.interactiveSelectList {
            addEntry("Yes, reset the registry")
            addEntry("No, exit")
            title("task_registry.json was not found. Resetting will delete all previous data. Proceed?")
            showInstructions(false)
        }
        if (response?.startsWith("Yes") == true) resetRegistry() else kotlin.system.exitProcess(0)
    }

    if (verbose) t.println(brightGreen("[SUCCESS]") + " task_registry.json found, reviewing data integrity.")

    runCatching {
        val jsonString = dataFile.readText()
        JSONOperator.decodeFromString<TaskRegistry>(jsonString)
    }
        .onSuccess { _ -> if (verbose) t.println(brightGreen("[SUCCESS]") + " Data integrity holds.\n\nGood to go! :D") }
        .onFailure { _ ->
            val response = t.interactiveSelectList {
                addEntry("Yes, reset the registry")
                addEntry("No, exit")
                title("task_registry.json is corrupt. Resetting will delete all previous data. Proceed?")
                showInstructions(false)
            }
            if (response?.startsWith("Yes") == true) resetRegistry() else kotlin.system.exitProcess(0)
        }
}

fun loadRegistry(): TaskRegistry = JSONOperator.decodeFromString(dataFile.readText())

fun saveRegistry(registry: TaskRegistry) {
    val encodedRegistry = JSONOperator.encodeToString(registry)
    dataFile.writeText(encodedRegistry)
}

fun ensureInitialized(verbose: Boolean = false) = doctorDataDirectory(verbose)