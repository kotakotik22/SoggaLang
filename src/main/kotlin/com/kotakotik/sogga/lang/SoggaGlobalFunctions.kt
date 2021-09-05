package com.kotakotik.sogga.lang

import com.kotakotik.sogga.lang.SoggaPermissions.CommandLineAccess
import com.kotakotik.sogga.lang.SoggaPermissions.FileAccess
import com.kotakotik.sogga.lang.exceptions.SEKotlinException
import com.kotakotik.sogga.lang.exceptions.SEPermissionException
import java.io.File


object SoggaGlobalFunctions {
    private val all = hashMapOf<String, SoggaFunction>()

    fun get(name: String) = all[name]
    fun getAll() = HashMap(all)

    private fun <T : SoggaFunction> add(function: T): T {
        all[function.name] = function
        return function
    }

    fun <T> combineTwo(var1: SoggaVariable<T>, var2: SoggaVariable<T>, func: (T?, T?) -> T): SoggaVariable<T> {
        return SoggaVariable(var1.type, func(var1.unwrap(), var2.unwrap()).toString())
    }

    fun <T> combineTwo(list: List<SoggaVariable<*>>, func: (T?, T?) -> T): SoggaVariable<T> =
        combineTwo(list[0] as SoggaVariable<T>, list[1] as SoggaVariable<T>, func)

    fun <T> combining(name: String, type: SoggaType<T>, func: (T?, T?) -> T) =
        add(SoggaFunction(type, name, listOf(type, type)) {
            combineTwo(it, func)
        })
    fun combiningNumbers(name: String, func: (Int, Int) -> Int) = combining(name, SoggaType.integer) { a: Int?, b: Int? ->
        return@combining func(a ?: 0, b ?: 0)
    }
    fun combiningStrings(name: String, func: (String?, String?) -> String) = combining(name, SoggaType.string) {a: String?, b: String? ->
        '"' + func(a, b) + '"'
    }

    val typeOf = add(SoggaFunction(SoggaType.type, "typeof", listOf(SoggaType.any)) {
        SoggaVariable(SoggaType.type, it[0].type.typeName)
    })
    val plus = combiningNumbers("plus") {a, b -> a + b}
    val minus = combiningNumbers("minus") {a, b -> a - b}
    val multiply = combiningNumbers("multiply") {a, b -> a * b}
    val divide = combiningNumbers("divide") {a, b -> a / b}
    val equals = add(SoggaFunction(SoggaType.boolean, "equals", listOf(SoggaType.any, SoggaType.any)) {
        SoggaVariable(SoggaType.boolean, (it[0].unwrap() == it[1].unwrap()).toString())
    })
    val append = combiningStrings("append") { a, b -> a + b}
    val readFile = add(SoggaFunction(SoggaType.string, "readFile", listOf(SoggaType.string)) {
        if(FileAccess.isEnabled) {
            try {
                SoggaVariable(SoggaType.string, '"' + File(it[0].unwrap().toString()).readText() + '"')
            } catch (e: Exception) {
                SEKotlinException.warn(e)
                SoggaVariable(SoggaType.string, null)
            }
        } else {
            SEPermissionException(FileAccess)
            SoggaVariable(SoggaType.string, null)
        }
    })
    val writeFile = add(SoggaFunction(SoggaType.nothing, "writeFile", listOf(SoggaType.string, SoggaType.string)) {
        if(FileAccess.isEnabled) {
            File(it[0].unwrap().toString()).writeText(it[1].unwrap().toString())
        } else {
            SEPermissionException(FileAccess).warn()
        }
        SoggaVariable(SoggaType.nothing, null)
    })
    val not = add(SoggaFunction(SoggaType.boolean, "not", listOf(SoggaType.boolean)) {
        SoggaVariable(SoggaType.boolean, (!(it[0].unwrap() as Boolean)).toString())
    })
    val cmd = add(SoggaFunction(SoggaType.string, "cmd", listOf(SoggaType.string)) {
        if(CommandLineAccess.isEnabled) {
            val p = Runtime.getRuntime().exec((if(System.getProperty("os.name")
                    .lowercase().startsWith("windows")) "cmd.exe /c" else "sh -c") +
                    it[0].toString())
            val out = mutableListOf<String>()
            p.inputStream.reader(Charsets.UTF_8).use { reader ->
                out.add(reader.readText())
            }
            p.waitFor()
            SoggaVariable(SoggaType.string, '"' + out.joinToString("\n") + '"')
        } else {
            SEPermissionException(CommandLineAccess).warn()
            SoggaVariable(SoggaType.string, null)
        }
    })
}