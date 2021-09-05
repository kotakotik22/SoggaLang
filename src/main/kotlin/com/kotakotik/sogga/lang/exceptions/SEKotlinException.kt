package com.kotakotik.sogga.lang.exceptions

import com.kotakotik.sogga.lang.SoggaException

class SEKotlinException private constructor(e: Exception) :
    SoggaException("Exception running kotlin code: ${e.message}") {
        companion object {
            fun warn(e: Exception) {
                if(e !is ExceptionInSogga) {
                    SEKotlinException(e).warn()
                }
            }
        }
}