package com.kotakotik.sogga

fun String.removeAllSurrounding(delimeter: String) =
    removeAllPrefix(delimeter).removeAllSuffix(delimeter)

fun String.removeAllPrefix(prefix: String): String {
    var s = this
    while(s.startsWith(prefix)) {
        s = s.removePrefix(prefix)
    }
    return s
}

fun String.removeAllSuffix(suffix: String): String {
    var s = this
    while(s.endsWith(suffix)) {
        s = s.removeSuffix(suffix)
    }
    return s
}

fun String.removeSurroundingSpaces() = removeAllSurrounding(" ")