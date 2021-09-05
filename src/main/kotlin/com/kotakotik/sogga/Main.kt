package com.kotakotik.sogga

fun printUsage() {
    val str = StringBuilder().appendLine("Usage: ")

    fun space(t: Int) = " ".repeat(t)

    fun addCommand(name: String, description: String, usage: String = name) {
        str.appendLine(space(1) + name)
            .appendLine(space(3) + description)
            .appendLine(space(3) + "> sogga $usage")
    }

    addCommand("repl", "Runs REPL where you can test commands")
    addCommand("test", "Runs test scripts")
    addCommand("file", "Runs provided file", "file myfile.sog")

    print(str)
}

fun main(args: Array<String>) {
    if(args.isEmpty()) return printUsage()
    when(args[0]) {
        "repl" -> repl()
        "file" -> {
            if(args.size < 2) return printUsage()
            runFile(args[1])
        }
        "test" -> test()
        else -> printUsage()
    }
}