package id.tanudjaja.authwarmup.session

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sessions")
class SessionController(
    private val service: SessionService,
) {
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
