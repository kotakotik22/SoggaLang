package com.kotakotik.sogga.lang.features

import com.kotakotik.sogga.lang.SoggaRuntime
import com.kotakotik.sogga.lang.SoggaType
import com.kotakotik.sogga.lang.SoggaVariable
import com.kotakotik.sogga.lang.exceptions.SESyntaxException

object IfFeature {
    fun check(args: List<String>): Boolean {
//        val variable = SoggaRuntime.variables[args[0]]
        val variable = SoggaVariable.parseExpect(args[0], SoggaType.boolean) ?: return false
        if(!SoggaRuntime.hasOpeningBracket(args)) {
            SESyntaxException("No { found on if line").warn()
            return false
        }
        return variable
//        SEInvalidParameterException("if", "Variable is not of type ${SoggaType.boolean.typeName}").warn()
    }

    fun addIgnores(ignores: ArrayList<Int>, lines: List<String>): List<String> {
        val cl = SoggaRuntime.currentLine
        val linesAfterIf = lines.drop(cl + 1)
        var i = 1
        val ignoredLines = arrayListOf<String>()
        var scopes = 0
        for(line in linesAfterIf) {
            if(SoggaRuntime.hasOpeningBracket(line)) scopes++
            if(SoggaRuntime.hasClosingBracket(line)) {
                if(scopes == 0) {
                    break
                }
                scopes--
            }
            ignores.add(i + cl)
            ignoredLines.add(line)
            i++
        }
        return ignoredLines
    }
}