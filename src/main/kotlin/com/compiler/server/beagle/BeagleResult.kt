package com.compiler.server.beagle

import com.compiler.server.model.ErrorDescriptor
import com.compiler.server.model.ExecutionResult
import com.compiler.server.model.TextInterval
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

private val outputRegex = Regex("$RESULT_PREFIX(.+)$RESULT_SUFFIX")

private fun getCompiledJsons(output: String): Map<String, String> {
    val result = outputRegex.find(output)
    val json = result?.groupValues?.get(1) ?: return mapOf()
    return ObjectMapper().readValue<MutableMap<String, String>>(json)
}

private fun getConsoleOutput(output: String): String {
    return output.replace(outputRegex, "")
}

private fun fixErrorPositions(error: ErrorDescriptor): ErrorDescriptor {
    return ErrorDescriptor(
            imports = error.imports,
            className = error.className,
            message = error.message,
            severity = error.severity,
            interval = TextInterval(
                    start = TextInterval.TextPosition(error.interval.start.line - linesBeforeContent, error.interval.start.ch),
                    end = TextInterval.TextPosition(error.interval.end.line - linesBeforeContent, error.interval.end.ch),
            )
    )
}

private fun getNumberOfLinesInScript(script: String): Int {
    return script.split("\n").size
}

private fun fixErrorMap(errors: Map<String, List<ErrorDescriptor>>, project: Map<String, String>): Map<String, List<ErrorDescriptor>> {
    val fixed = HashMap<String, List<ErrorDescriptor>>()
    errors.forEach() {
        fixed[it.key] = it.value.map() { error ->
            fixErrorPositions(error)
        }.filter { error -> // filter out errors in lines included by us, i.e. that don't exist in the original project
            val script = project[it.key]
            if (script == null) true else error.interval.start.line <= getNumberOfLinesInScript(script)
        }
    }
    return fixed
}

class BeagleResult(executionResult: ExecutionResult, project: Map<String, String>) : ExecutionResult() {
    val files = getCompiledJsons(executionResult.text)

    init {
        this.errors = fixErrorMap(executionResult.errors, project)
        this.exception = executionResult.exception
        this.text = getConsoleOutput(executionResult.text)
    }
}
