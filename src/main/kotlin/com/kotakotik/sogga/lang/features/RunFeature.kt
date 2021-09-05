package com.kotakotik.sogga.lang.features

import com.kotakotik.sogga.lang.SoggaFunction
import com.kotakotik.sogga.lang.SoggaRuntime
import com.kotakotik.sogga.lang.SoggaType
import com.kotakotik.sogga.lang.SoggaVariable
import com.kotakotik.sogga.lang.exceptions.SEInvalidParameterException
import com.kotakotik.sogga.lang.exceptions.SESyntaxException

object RunFeature {
    fun run(args: List<String>, currentFunc: String = "run") {
        if(args.isEmpty()) {
            SEInvalidParameterException(currentFunc, "At least 1 argument required").warn()
            return
        }
        val func = SoggaRuntime.getFunction(args[0])
        if(func == null) {
            SESyntaxException("Function ${args[0]} not found").warn()
            return
        }
        run(args[0], func, args.drop(1).joinToString(" ").split(",").filter { it.isNotBlank() }.map { SoggaVariable.parse(it) }.map {
            it ?: SoggaVariable(SoggaType.nothing, null)
        })
    }

    fun run(funcName:String, func: SoggaFunction, passedArguments: List<SoggaVariable<*>>): SoggaVariable<*> {
        val passedTypes = passedArguments.map { it.type }
        if(func.expectedArguments != passedTypes) {
            SEInvalidParameterException(funcName,
                "Expected arguments of types ${func.expectedArguments.map { it.typeName }.toTypedArray().contentToString()}, but got ${passedTypes.map { it.typeName }.toTypedArray().contentToString()}").warn()
            return SoggaVariable(SoggaType.nothing, null)
        }
        return func.func(passedArguments)
    }
}