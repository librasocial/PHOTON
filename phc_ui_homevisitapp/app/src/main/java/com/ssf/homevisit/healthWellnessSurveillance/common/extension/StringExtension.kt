package com.ssf.homevisit.healthWellnessSurveillance.common.extension


fun String?.hasValueOtherThan(ignoreValue: String = "select"): Boolean {
    return !isNullOrEmpty() && !startsWith(ignoreValue, true)
}

fun String?.hasValueEqualTo(value: String = "done"): Boolean {
    return !isNullOrEmpty() && startsWith(value, true)
}