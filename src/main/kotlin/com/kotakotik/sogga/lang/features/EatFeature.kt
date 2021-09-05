package com.kotakotik.sogga.lang.features

import com.kotakotik.sogga.lang.SoggaException
import com.kotakotik.sogga.lang.SoggaRuntime
import com.kotakotik.sogga.lang.exceptions.SEInvalidParameterException
import java.security.InvalidParameterException

object EatFeature {
    fun eat(args: List<String>) {
        for(arg in args) {
            if(arg == "*") {
                SoggaRuntime.globalVariables.clear()
                return
            }
            SoggaRuntime.getVariable(arg)
                ?: SEInvalidParameterException("eat", "No variable found matching $arg")

            SoggaRuntime.globalVariables.remove(arg)
        }
    }
}