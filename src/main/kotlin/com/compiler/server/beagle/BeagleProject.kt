package com.compiler.server.beagle

import com.compiler.server.model.Project
import com.compiler.server.model.ProjectFile

private fun toFunctionName(fileName: String): String {
    return "__${fileName.replace(".", "")}"
}

private fun toBeagleFile(name: String, content: String): String {
    // attention, after altering this code, make sure to also update the constant LINES_FN_DECLARATION
    val code = listOf(
            "fun ${toFunctionName(name)}(): Any {",
            "return (",
            content,
            ")",
            "}"
    ).joinToString("\n")
    return "$importsCode\n\n$code"
}

private fun createBeagleMain(fileNames: Collection<String>): String {
    val import = "import br.com.zup.beagle.serialization.jackson.BeagleSerializationUtil"
    val fillPages = fileNames.map() {
        "pages[\"$it\"] = BeagleSerializationUtil.beagleObjectMapper().writeValueAsString(${toFunctionName(it)}())"
    }
    val code = listOf(
            "fun main() {",
            "val pages = HashMap<String, String>()",
            fillPages.joinToString("\n"),
            "println(\"$RESULT_PREFIX\${BeagleSerializationUtil.beagleObjectMapper().writeValueAsString(pages)}$RESULT_SUFFIX\")",
            "}"
    ).joinToString("\n")
    return "$import\n\n$code"
}

fun toBeagleProject(project: Map<String, String>): Project {
    val beagleFiles = project.map() {
        ProjectFile(
                name = it.key,
                text = toBeagleFile(it.key, it.value),
        )
    }
    val mainFile = ProjectFile(
            name = "__main-generated-compiler.kt",
            text = createBeagleMain(project.keys),
    )
    return Project(files = beagleFiles + listOf(mainFile))
}
