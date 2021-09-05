package com.kotakotik.sogga.lang

import java.lang.Exception

open class SoggaException(val message: String) {
    companion object {
        fun logPrefix(after: String) = "[SOGGA/${after.uppercase()}]"
    }

    override fun toString() =
        "${javaClass.simpleName} on line #${SoggaRuntime.currentLine + 1}: " +
                "\n$message"

    protected fun withPrefix(prefix: String) = "${logPrefix(prefix)} ${toString()}"
    fun asWarn() = withPrefix("warn")

    fun warn() {
        if(SoggaVariable.GlobalVariables.crashOnWarn.unwrap() == true) throw ExceptionInSogga(toString())
        println("\u001B[33m" + asWarn() + "\u001B[0m")
    }

    class ExceptionInSogga(message: String) : Exception(message)
}