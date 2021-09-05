package com.kotakotik.sogga.lang.exceptions

import com.kotakotik.sogga.lang.SoggaException

class SESyntaxException(message: String) : SoggaException("Syntax exception: $message")