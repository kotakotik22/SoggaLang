package com.kotakotik.sogga.lang

import com.kotakotik.sogga.lang.exceptions.SEInvalidVariableException
import com.kotakotik.sogga.removeSurroundingSpaces

open class SoggaVariable<T>(val type: SoggaType<T>, var value: String?) {
    companion object {

        fun parse(variable1: String): SoggaVariable<*>? {
            val variable = variable1.removeSurroundingSpaces()
           if(variable.startsWith('"') && variable.endsWith('"')) {
               return SoggaVariable(SoggaType.string, variable)
           }
            if(variable == "true" || variable == "false") {
                return SoggaVariable(SoggaType.boolean, variable)
            }
            if(variable.isNotEmpty() && variable[0].isDigit()) {
                return SoggaVariable(SoggaType.integer, variable)
            }
            val foundVar = SoggaRuntime.getVariable(variable)
            if(foundVar != null) {
                return foundVar
            }
            if(variable.startsWith("/") && variable.endsWith("/")) {
                return SoggaVariable(SoggaType.type, variable)
            }
            if(variable == "null" || variable == "nothing") {
                return SoggaVariable(SoggaType.nothing, null)
            }
            SEInvalidVariableException(variable, "Could not find variable, or parse as string, boolean, type, nothing, or integer!").warn()
            return null
        }

        fun <T> parseExpect(variable: String, type: SoggaType<T>): T? {
            var r: SoggaVariable<*> = parse(variable) ?: return null
            return if(r.type == type) r.unwrap() as T
            else {
                SEInvalidVariableException(variable, "$variable is not of expected type ${type.typeName}, is of type ${r.type.typeName}").warn()
                null
            }
        }
   }

    fun unwrap(): T? {
        if(value == "null" || value == null) return null
        return type.converter(value.toString())
    }
    override fun toString() = type.toString.invoke(this)

    object GlobalVariables {
        class GlobalVariable<T>(type: SoggaType<T>, value: String?, val defaultValue: String) : SoggaVariable<T>(type, value) {
            fun reset() {
                value = defaultValue
            }
        }

        val all = hashMapOf<String, GlobalVariable<*>>()

        private fun <T> add(type: SoggaType<T>, name: String, defaultValue: String): GlobalVariable<T> {
            val variable = GlobalVariable(type, defaultValue, defaultValue)
            all[name] = variable
            return variable
        }

        val crashOnWarn = add(SoggaType.boolean, "crashOnWarn", "true")
    }
}