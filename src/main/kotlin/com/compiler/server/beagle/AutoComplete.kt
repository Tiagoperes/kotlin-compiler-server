package com.compiler.server.beagle

import com.compiler.server.model.Project
import com.compiler.server.model.ProjectFile

data class AutoCompleteRequest(
    val project: Project,
    val line: Int,
    val ch: Int
)

fun prepareForAutoComplete(request: AutoCompleteRequest): AutoCompleteRequest {
    val originalFile = request.project.files[0]
    val fileWithImports = ProjectFile(name = originalFile.name, text = "$importsCode\n${originalFile.text}")
    val project = Project(
        files = listOf(fileWithImports),
        args = request.project.args,
        confType = request.project.confType
    )
    return AutoCompleteRequest(project, request.line + importsSize, request.ch)
}
