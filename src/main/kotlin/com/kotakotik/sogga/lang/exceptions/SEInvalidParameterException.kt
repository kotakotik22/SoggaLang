package com.kotakotik.sogga.lang.exceptions

import com.kotakotik.sogga.lang.SoggaException
import java.lang.Exception
import java.security.InvalidParameterException

class SEInvalidParameterException(feature: String, message: String) : SoggaException("Invalid parameter in $feature: $message")