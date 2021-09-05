package com.kotakotik.sogga.lang.features

import com.kotakotik.sogga.lang.SoggaGlobalFunctions
import com.kotakotik.sogga.lang.SoggaRuntime
import com.kotakotik.sogga.lang.SoggaRuntime.globalVariables
import com.kotakotik.sogga.lang.SoggaType
import com.kotakotik.sogga.lang.SoggaVariable
import com.kotakotik.sogga.lang.exceptions.SEInvalidParameterException
import com.kotakotik.sogga.lang.exceptions.SEInvalidVariableException
import com.kotakotik.sogga.lang.exceptions.SESyntaxException

object PizzerFeature {
    fun define(args: List<String>) {
        if(args.size < 3) {
            SESyntaxException("Not enough args provided for pizzer").warn()
            return
        }
        val name = args[0]
        val variable = when(args.getOrNull(1)) {
            "with" -> {
                SoggaVariable.parse(args.drop(2).joinToString(" "))
            }
            "as" -> {
                SoggaVariable(SoggaType.getAll()[args[2]] ?: SoggaType.nothing, null)
            }
            "getting" -> {
                val foundFunc = SoggaRuntime.getFunction(args[2])
                if(foundFunc == null) {
                    SEInvalidParameterException("pizzer", "Could not find function ${args[2]}").warn()
                    return
                }
                RunFeature.run(args[2], foundFunc, args.drop(3).joinToString(" ").split(",").map { SoggaVariable.parse(it) }.map {
                    it ?: SoggaVariable(SoggaType.nothing, null)
                })
            }
            else -> {
                SESyntaxException("Invalid use of pizzer! must be: pizzer [with/as/getting] [variable/type/(args)], but non with/as/getting got!").warn()
                null
            }
        }

        if (variable != null) {
            var foundVar = SoggaVariable.GlobalVariables.all.getOrDefault(name, null) ?: globalVariables.getOrDefault(name, null)
            if(foundVar != null && foundVar.type != variable.type) {
                SEInvalidVariableException(name, "Attempted to reassign variable of type ${foundVar.type.typeName} to type ${variable.type.typeName}").warn()
            } else {
                if(foundVar is SoggaVariable.GlobalVariables.GlobalVariable) {
                    foundVar.value = variable.value
                } else {
                    globalVariables[name] = variable
                }
            }
        }
    }
}