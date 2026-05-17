package noonewastaken.tskr.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import noonewastaken.tskr.storage.ensureInitialized

class Doctor: CliktCommand() {
    override fun help(context: Context): String = "Diagnose and repair tskr data directory and files."

    override fun run() = ensureInitialized(true)
}