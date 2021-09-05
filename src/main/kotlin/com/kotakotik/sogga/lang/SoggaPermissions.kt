package com.kotakotik.sogga.lang

enum class SoggaPermissions(val enabledByDefault: Boolean) {
    FileAccess(false),
    CommandLineAccess(false),
    QuitAccess(true);

    var isEnabled = enabledByDefault

    fun enable() {
        isEnabled = true
    }

    fun disable() {
        isEnabled = false
    }

    companion object {
        fun enableAll() {
            for(value in values()) {
                value.enable()
            }
        }
    }
}