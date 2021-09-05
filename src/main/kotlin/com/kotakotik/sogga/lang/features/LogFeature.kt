package com.kotakotik.sogga.lang.features

import com.kotakotik.sogga.lang.SoggaVariable
import com.kotakotik.sogga.removeAllSurrounding

object LogFeature {
    fun log(args: List<String>) {
        for(arg in args) {
            println(SoggaVariable.parse(arg).toString())
        }
    }
}