package com.kotakotik.sogga.lang.exceptions

import com.kotakotik.sogga.lang.SoggaException

class SEInvalidVariableException(variable: String, reason: String) : SoggaException("Invalid variable $variable: $reason")