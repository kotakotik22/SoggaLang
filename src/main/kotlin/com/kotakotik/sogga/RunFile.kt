package com.kotakotik.sogga

import com.kotakotik.sogga.lang.SoggaRuntime
import java.io.File

fun runFile(file: String) {
    SoggaRuntime.run(File(file).readText())
}