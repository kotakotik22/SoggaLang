package com.kotakotik.sogga.lang

class SoggaFunction(
    val returnType: SoggaType<*>,
    val name: String,
    val expectedArguments: List<SoggaType<*>>,
    val func: (List<SoggaVariable<*>>) -> SoggaVariable<*>) {
}