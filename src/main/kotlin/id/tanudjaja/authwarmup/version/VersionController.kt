package id.tanudjaja.authwarmup.version

import org.springframework.boot.info.BuildProperties
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/version")
class VersionController(
	private val buildProperties: BuildProperties
) {
    @GetMapping
    fun get(): VersionGetResponse {		
    	return VersionGetResponse(
			buildProperties.getVersion()
		)
    }
}

