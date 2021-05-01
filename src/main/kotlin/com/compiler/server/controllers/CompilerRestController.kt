package com.compiler.server.controllers

import com.compiler.server.beagle.AutoCompleteRequest
import com.compiler.server.beagle.BeagleResult
import com.compiler.server.beagle.prepareForAutoComplete
import com.compiler.server.beagle.toBeagleProject
import com.compiler.server.model.ErrorDescriptor
import com.compiler.server.model.Project
import com.compiler.server.model.TranslationJSResult
import com.compiler.server.model.bean.VersionInfo
import com.compiler.server.service.KotlinProjectExecutor
import common.model.Completion
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/compiler", "/api/**/compiler"])
class CompilerRestController(private val kotlinProjectExecutor: KotlinProjectExecutor) {
  @PostMapping("/run")
  fun executeKotlinProjectEndpoint(@RequestBody project: Project) = kotlinProjectExecutor.run(project)

  @PostMapping("/run-beagle")
  fun executeBeagleProjectEndpoint(@RequestBody project: Map<String, String>) = BeagleResult(kotlinProjectExecutor.run(toBeagleProject(project)), project)

  @PostMapping("/test")
  fun testKotlinProjectEndpoint(@RequestBody project: Project) = kotlinProjectExecutor.test(project)

  @PostMapping("/translate")
  fun translateKotlinProjectEndpoint(
    @RequestBody project: Project,
    @RequestParam(defaultValue = "false") ir: Boolean
  ): TranslationJSResult {
    return if (ir) kotlinProjectExecutor.convertToJsIr(project)
    else kotlinProjectExecutor.convertToJs(project)
  }

  @PostMapping("/complete")
  fun getKotlinCompleteEndpoint(
    @RequestBody project: Project,
    @RequestParam line: Int,
    @RequestParam ch: Int
  ) = kotlinProjectExecutor.complete(project, line, ch)

  @PostMapping("/complete-beagle")
  fun getBeagleCompleteEndpoint(
    @RequestBody project: Project,
    @RequestParam line: Int,
    @RequestParam ch: Int
  ): List<Completion> {
    val prepared = prepareForAutoComplete(AutoCompleteRequest(project, line, ch))
    return kotlinProjectExecutor.complete(prepared.project, prepared.line, prepared.ch)
  }

  @PostMapping("/highlight")
  fun highlightEndpoint(@RequestBody project: Project) : Map<String, List<ErrorDescriptor>> = kotlinProjectExecutor.highlight(project)
}

@RestController
class VersionRestController(private val kotlinProjectExecutor: KotlinProjectExecutor) {
  @GetMapping("/versions")
  fun getKotlinVersionEndpoint(): List<VersionInfo> = listOf(kotlinProjectExecutor.getVersion())
}