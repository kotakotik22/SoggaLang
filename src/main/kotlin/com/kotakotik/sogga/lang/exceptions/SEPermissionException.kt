package com.kotakotik.sogga.lang.exceptions

import com.kotakotik.sogga.lang.SoggaException
import com.kotakotik.sogga.lang.SoggaPermissions

class SEPermissionException(permission: SoggaPermissions) : SoggaException("Attempted to run code that requires ${permission.name}")