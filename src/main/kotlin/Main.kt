package noonewastaken.tskr

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands

import noonewastaken.tskr.commands.Add
import noonewastaken.tskr.commands.Delete
import noonewastaken.tskr.commands.Doctor
import noonewastaken.tskr.commands.Done
import noonewastaken.tskr.commands.Edit
import noonewastaken.tskr.commands.Export
import noonewastaken.tskr.commands.List
import noonewastaken.tskr.commands.Undo
import noonewastaken.tskr.storage.ensureInitialized

class Tskr: CliktCommand() {
    override fun run() {
        val subcommand = currentContext.invokedSubcommand
        if (subcommand != null) ensureInitialized()
    }
}

fun main(argv: Array<String>) {
    System.setOut(java.io.PrintStream(System.out, true, Charsets.UTF_8))
    System.setErr(java.io.PrintStream(System.err, true, Charsets.UTF_8))

    Tskr()
        .subcommands(Add(), Edit(), List(), Done(), Undo(), Delete(), Doctor(), Export())
        .main(argv)
}