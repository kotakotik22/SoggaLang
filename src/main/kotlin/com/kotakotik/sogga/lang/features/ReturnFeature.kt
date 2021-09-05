package com.kotakotik.sogga.lang.features

import com.kotakotik.sogga.lang.SoggaVariable

object ReturnFeature {
    fun return1(args: List<String>): List<SoggaVariable<*>> {
        return args.mapNotNull { SoggaVariable.parse(it) }
    }
}