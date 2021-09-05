package com.kotakotik.sogga.lang.features

import com.kotakotik.sogga.lang.SoggaFunction
import com.kotakotik.sogga.lang.SoggaRuntime
import com.kotakotik.sogga.lang.SoggaType
import com.kotakotik.sogga.lang.SoggaVariable
import com.kotakotik.sogga.lang.exceptions.SESyntaxException

object FunctionFeature {
    fun define(args: List<String>, ignores: ArrayList<Int>, lines: List<String>) {
        if(!SoggaRuntime.hasOpeningBracket(args)) {
            SESyntaxException("No { found on function line").warn()
            return
        }
        if(args.size < 2) {
            SESyntaxException("Not enough arguments provided for function, provided ${args.size}").warn()
            return
        }
        val name = args[1]
        val type = SoggaType.type.converter("/${args[0]}/") ?: SoggaType.nothing
        val ignores1 = arrayListOf<Int>()
        val ignored = IfFeature.addIgnores(ignores1, lines)
        val expectedTypes = args.drop(2).filter { !it.contains("{") }.mapNotNull { SoggaType.getAll().getOrDefault(it, null) }
        val funcLine = SoggaRuntime.currentLine
        SoggaRuntime.setFunc(name, SoggaFunction(type, name, expectedTypes) { funcArgs ->
            val currentIgnores = ArrayList(ignores)
            currentIgnores.removeAll(ignores1)
            val oldVarProvider = SoggaRuntime.currentVariableProvider
            SoggaRuntime.currentVariableProvider = {
                var toRet = SoggaRuntime.defaultVariableProvider()(it)
                if(it.startsWith("args.")) {
                    toRet = funcArgs.getOrNull(it.removePrefix("args.").toIntOrNull() ?: -1)
                }
                toRet
            }
            val ret: SoggaVariable<*> = SoggaRuntime.runLines(ignored, currentIgnores, lineNum = funcLine).getOrNull(0)
                ?: return@SoggaFunction SoggaVariable(type, null)
            SoggaRuntime.currentVariableProvider = oldVarProvider
            if(ret.type != type) {
                SoggaVariable(type, null)
            } else {
                ret
            }
        })
        ignores.addAll(ignores1)
//        return variable
//        if(args.size < 3) {
//            SESyntaxException("Not enough args provided for pizzer").warn()
//            return
//        }
//        val name = args[0]
//        val variable = when(args.getOrNull(1)) {
//            "with" -> {
//                SoggaVariable.parse(args.drop(2).joinToString(" "))
//            }
//            "as" -> {
//                SoggaVariable(SoggaType.getAll()[args[2]] ?: SoggaType.nothing, null)
//            }
//            "getting" -> {
//                SoggaGlobalFunction.get(args[2])?.runFunction(SoggaRuntime.getArgs(args.drop(2).joinToString(" ")))
//            }
//            else -> {
//                SESyntaxException("Invalid use of pizzer! must be: pizzer [with/as/getting] [variable/type/(args)], but non with/as/getting got!")
//                null
//            }
//        }
//
//        if (variable != null) {
//            if(SoggaRuntime.variables.containsKey(name) && SoggaRuntime.variables[name]!!.type != variable.type) {
//                SEInvalidVariableException(name, "Attempted to reassign variable of type ${SoggaRuntime.variables[name]!!.type.typeName} to type ${variable.type.typeName}").warn()
//            } else {
//                SoggaRuntime.variables[name] = variable
//            }
//        }
    }
}