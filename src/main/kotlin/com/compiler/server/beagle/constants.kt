package com.compiler.server.beagle

private val imports = listOf(
        "br.com.zup.beagle.widget.action.*",
        "br.com.zup.beagle.widget.context.*",
        "br.com.zup.beagle.widget.core.*",
        "br.com.zup.beagle.widget.expression.*",
        "br.com.zup.beagle.widget.form.*",
        "br.com.zup.beagle.widget.layout.*",
        "br.com.zup.beagle.widget.lazy.*",
        "br.com.zup.beagle.widget.navigation.*",
        "br.com.zup.beagle.widget.pager.*",
        "br.com.zup.beagle.widget.ui.*",
        "br.com.zup.beagle.widget.utils.*",
        "br.com.zup.beagle.action.*",
        "br.com.zup.beagle.analytics.*",
        "br.com.zup.beagle.constants.*",
        "br.com.zup.beagle.context.*",
        "br.com.zup.beagle.builder.widget.*",
        "br.com.zup.beagle.core.*",
        "br.com.zup.beagle.ext.*",
        "br.com.zup.beagle.layout.*",
        "br.com.zup.beagle.action.*"
)

val importsCode =  imports.map {
    "import $it"
}.toList().joinToString("\n")

const val RESULT_PREFIX = ":::START-COMPILER-RESULT:::"
const val RESULT_SUFFIX = ":::END-COMPILER-RESULT:::"
// numbers of lines added before the script's original content to declare its wrapping function
const val LINES_FN_DECLARATION = 2
val linesBeforeContent = LINES_FN_DECLARATION + imports.size
