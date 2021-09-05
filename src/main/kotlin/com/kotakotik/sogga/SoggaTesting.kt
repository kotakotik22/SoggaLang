package com.kotakotik.sogga

import com.kotakotik.sogga.lang.SoggaPermissions
import com.kotakotik.sogga.lang.SoggaRuntime
import java.io.File

fun runResource(file: String) {
    var file1 = "test/$file"
    if(!file.contains(".")) file1 = "$file1.sog"
    println("Running file $file1:")
    SoggaRuntime.run(File(ClassLoader.getSystemResource(file1).file).readText())
}

fun test() {
    runResource("if")
    runResource("pizzer")
    runResource("eat")
    runResource("typeof")
    runResource("vartype")
    runResource("math")
    runResource("functions")
    runResource("equals")
    runResource("semicolon")
    runResource("append")
    SoggaPermissions.FileAccess.enable()
    runResource("files")
    SoggaPermissions.CommandLineAccess.enable()
    runResource("cmd")

    SoggaPermissions.CommandLineAccess.disable()
    runResource("permissions")

//    runResource("stackoverflow")
}

fun main() {
    test()
}