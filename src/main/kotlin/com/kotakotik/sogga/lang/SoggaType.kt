package com.kotakotik.sogga.lang

import com.kotakotik.sogga.lang.exceptions.SESyntaxException

class SoggaType<T>(val typeName: String, val toString: (SoggaVariable<T>) -> String = {it.unwrap().toString()}, val converter: (String) -> T) {
    init {
        all[typeName] = this
    }

    override fun equals(other: Any?): Boolean {
        if(other is SoggaType<*>) {
            return other.typeName == typeName ||
                    other.typeName == "any" || typeName == "any"
        }
        return false
    }

    companion object {
        private val all = hashMapOf<String, SoggaType<*>>()

        fun getAll() = HashMap(all)

        val boolean = SoggaType("boolean") {
            if(it == "true") {
                return@SoggaType true
            }
            if(it == "false") {
                return@SoggaType false
            }
            SESyntaxException("Could not parse boolean $it, must be either true or false!")
            return@SoggaType null
        }
        var string = SoggaType("string") {
            if(it.startsWith('"') && it.endsWith('"')) {
                return@SoggaType it.removeSurrounding("\"")
            }
            SESyntaxException("Could not parse string $it, must end and start with \"!").warn()
            return@SoggaType ""
        }
        var integer = SoggaType("integer") {
            if(it.all { c -> c.isDigit() }) {
                return@SoggaType it.toInt()
            } else {
                SESyntaxException("Could not parse integer $it").warn()
                return@SoggaType 0
            }
        }
        var nothing = SoggaType("nothing") {
            return@SoggaType null
        }
        var type = SoggaType("type", { "/${it.unwrap()?.typeName}/" }) {
            all[it.removeSurrounding("/")]
        }
        var any = SoggaType("any") {
            null
        }
    }
}