package id.tanudjaja.authwarmup.version

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.boot.info.BuildProperties
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/version")
@Tag(name = "Version API")
class VersionController(
    private val buildProperties: BuildProperties,
) {
    @Operation(
        summary = "Get the version of the currently running application",
        operationId = "VersionController.get",
    )
    @GetMapping
    fun get(): VersionGetResponse {
        return VersionGetResponse(
            buildProperties.getVersion(),
        )
    }
}
