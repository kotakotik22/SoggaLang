package com.kotakotik.sogga

import com.kotakotik.sogga.lang.SoggaPermissions
import com.kotakotik.sogga.lang.SoggaRuntime

fun repl() {
    SoggaRuntime.clearAll()
    SoggaPermissions.enableAll()
    while(true) {
        print(">>> ")
        val line = readLine() ?: ""
        SoggaRuntime.runLines(line.split(";"))
        println()
    }
}

fun main() {
    repl()
}