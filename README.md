# tskr

> A fast, minimal task manager for your terminal — built with Kotlin.

<!-- Add a demo GIF here -->

## Installation

<!-- Add installation instructions here -->

## Usage

### Add a task
```bash
# Interactive mode
tskr add

# With argument
tskr add "Buy milk"

# With all flags
tskr add "Buy milk" --due "2026-06-17 19:00" --note "2% only"
```

### List tasks
```bash
tskr list             # All tasks
tskr list --pending   # Pending only
tskr list --done      # Completed only
```

### Complete a task
```bash
tskr done <id>
tskr undo <id>
```

### Edit a task
```bash
tskr edit <id>                          # Interactive
tskr edit <id> --task "Updated task"    # Specific field
tskr edit <id> --due "2026-07-01 09:00"
tskr edit <id> --note "New note"
```

### Delete a task
```bash
tskr delete <id>
```

### Export tasks
```bash
tskr export --format json --output ~/exports
tskr export --format csv  --output ~/exports
tskr export --format txt  --output ~/exports
```

### Doctor
```bash
tskr doctor   # Diagnose and repair tskr data directory and files
```

## Architecture

`tskr` stores all tasks locally at `~/.tskr/tasks.json` — no internet, no accounts, no telemetry.

### The Doctor Pattern
Every command runs an integrity check before executing — verifying the data directory and `tasks.json` exist and are valid. If something is wrong, `tskr` tells you and offers to fix it before any data operation runs. The `tskr doctor` command exposes this same check verbosely for manual diagnosis.

### Data Structure
Tasks are stored as a JSON registry with an auto-incrementing ID counter. IDs are permanent — deleting task 3 means ID 3 is gone forever, no shifting. This keeps references stable if you ever script around `tskr`.

```json
{
  "lastId": 3,
  "tasks": [
    {
      "id": 1,
      "task": "Buy milk",
      "note": "2% only",
      "due": "2026-06-17T19:00",
      "done": false
    }
  ]
}
```

## Built With

- [Kotlin](https://kotlinlang.org/)
- [Clikt](https://ajalt.github.io/clikt/) — CLI argument parsing
- [Mordant](https://ajalt.github.io/mordant/) — Terminal styling and tables
- [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) — JSON serialization
- [kotlinx-datetime](https://github.com/Kotlin/kotlinx-datetime) — Date and time handling

## License

MIT