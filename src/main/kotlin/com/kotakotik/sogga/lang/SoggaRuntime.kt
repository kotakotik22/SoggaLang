package com.kotakotik.sogga.lang

import com.kotakotik.sogga.lang.exceptions.SEKotlinException
import com.kotakotik.sogga.lang.features.*
import com.kotakotik.sogga.removeSurroundingSpaces
import kotlin.system.exitProcess

object SoggaRuntime {
    fun isComment(line: String) = line.replace(Regex("\\s"), "").startsWith("//")

    var globalVariables = hashMapOf<String, SoggaVariable<*>>()
    var functions = hashMapOf<String, SoggaFunction>()
    fun getFunction(name: String): SoggaFunction? {
        val globalFunction = SoggaGlobalFunctions.get(name)
        if(globalFunction != null) return globalFunction
        return functions[name]
    }
    var currentVariableProvider: (String) -> SoggaVariable<*>? = defaultVariableProvider()
    fun defaultVariableProvider() = { it: String ->
        getGlobalVariable(it)
    }
    fun getVariable(name: String): SoggaVariable<*>? {
        return currentVariableProvider(name)
    }
    private fun getGlobalVariable(name: String) = SoggaVariable.GlobalVariables.all.getOrDefault(name, null) ?:
        globalVariables.getOrDefault(name, null)
    fun setFunc(name: String, func: SoggaFunction) {
        functions[name] = func
    }

    fun getCurrentLineAsString() = currentLineAsString

    var currentLine = -1
    private var currentLineAsString: String? = null

    fun hasOpeningBracket(line: String) = if(line.isEmpty()) false else !isComment(line) && line.replace(Regex("\\s"), "").last() == '{'
    fun hasOpeningBracket(line: List<String>) = hasOpeningBracket(line.joinToString(""))
    fun hasClosingBracket(line: String) = if(line.isEmpty()) false else !isComment(line) && line.split(" ")[0].replace(" ", "") == "}"

    fun getArgs(line: String) =
        getInfo(line).joinToString(" ").split(",")

    fun getInfo(line: String) =
        line.split(" ").drop(1)

    var globalLines: List<String> = listOf()
    var shouldQuit = false

    fun runLines(lines: List<String>, ignoreLines: ArrayList<Int> = ignoreLines1, initialRun: Boolean = false, lineNum: Int = currentLine): List<SoggaVariable<*>> {
        var lastLine = lineNum
        for(line in lines) {
            if(shouldQuit) break
            lastLine++
            currentLine = lastLine
            currentLineAsString = line
            if(isComment(line) ||
                line.isBlank() ||
                ignoreLines.contains(currentLine)) {
                continue
            }
            val line1 = line.removeSurroundingSpaces()
            var command = line1.split(" ")[0]
//            while(command[0].toString().contains(Regex("\\s"))) {
//                command = line.split(" "
//            }

            fun getArgs() = getArgs(line1)
            fun getInfo() = getInfo(line1)

            try {
                when(command) {
                    "eat" -> {
                        EatFeature.eat(getArgs())
                    }
                    "if" -> {
                        if(!IfFeature.check(getInfo())) {
                            IfFeature.addIgnores(ignoreLines, globalLines)
                        }
                    }
                    "log" -> {
                        LogFeature.log(getArgs())
                    }
                    "pizzer" -> {
                        PizzerFeature.define(getInfo())
                    }
                    "fun" -> {
                        FunctionFeature.define(getInfo(), ignoreLines, globalLines)
                    }
                    "return" -> {
                        return ReturnFeature.return1(getArgs())
                    }
                    "run" -> {
                        RunFeature.run(getInfo())
                    }
                    "quit" -> {
                        if(SoggaPermissions.QuitAccess.isEnabled) {
                            exitProcess(0)
                        } else {
                            shouldQuit = true
                            break
                        }
                    }
                }
            } catch (e: Exception) {
                if(e !is SoggaException.ExceptionInSogga) {
                    SEKotlinException.warn(e)
                }
                throw e
            }
        }
        return arrayListOf()
    }

    private var ignoreLines1 = arrayListOf<Int>()
    fun getIgnoreLines() = ArrayList(ignoreLines1)

    fun clearAll() {
        globalVariables.clear()
        currentVariableProvider = defaultVariableProvider()
        functions.clear()
        ignoreLines1.clear()
        shouldQuit = false

        for(variable in SoggaVariable.GlobalVariables.all.entries) variable.value.reset()
    }

    fun run(code: String) {
        clearAll()
        globalLines = code.replace("\r", "").split("\n", ";")
        currentLine = -1
        runLines(globalLines, initialRun = true)
        currentLine = -1
    }
}