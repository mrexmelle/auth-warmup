package id.tanudjaja.authwarmup.session

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sessions")
@Tag(name = "Session API")
class SessionController(
    private val service: SessionService,
) {
	@Operation(
		summary = "Login by creating a new session",
		operationId = "SessionController.post")
    @PostMapping
    fun post(
        @RequestBody request: SessionPostRequest,
    ): SessionPostResponse {
        if (service.authenticate(request) == false) {
            throw Exception("Authentication failed")
        }
        return SessionPostResponse(
            service.generateJwt(request.phoneNumber),
        )
    }
}
