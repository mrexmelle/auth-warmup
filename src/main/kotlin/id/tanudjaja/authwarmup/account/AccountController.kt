package id.tanudjaja.authwarmup.account

import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/accounts")
class AccountController(
    private val service: AccountService,
) {
    @PostMapping
    fun post(
        @RequestBody request: AccountPostRequest,
    ): AccountPostResponse {
        try {
            service.validate(request)
        } catch (e: Exception) {
            return AccountPostResponse(e.message ?: "Unknown error")
        }
		
        service.create(request)
			
        return AccountPostResponse("OK")
    }

    @GetMapping("/name")
    fun getName(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authorization: String,
    ): AccountGetNameResponse {
        val bearerToken = service.getBearerToken(authorization)
        if (bearerToken.isEmpty()) {
            return AccountGetNameResponse("", "Bearer token invalid")
        }
		
        val phoneNumber = try {
            service.authorize(bearerToken)
        } catch (e: Exception) {
            return AccountGetNameResponse("", e.message ?: "Authorization failed")
        }
		
        return AccountGetNameResponse(
            service.retrieveNameByPhoneNumber(phoneNumber),
            "OK",
        )
    }

    @PatchMapping("/name")
    fun patchName(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authorization: String,
        @RequestBody request: AccountPatchNameRequest,
    ): AccountPatchNameResponse {
        val bearerToken = service.getBearerToken(authorization)
        if (bearerToken.isEmpty()) {
            return AccountPatchNameResponse(
                request.old,
                request.old,
                "Bearer token invalid",
            )
        }
		
        val phoneNumber = try {
            service.authorize(bearerToken)
        } catch (e: Exception) {
            return AccountPatchNameResponse(
                request.old,
                request.old,
                e.message ?: "Authorization failed",
            )
        }
		
        val result = service.updateName(phoneNumber, request)
        if (result) {
            return AccountPatchNameResponse(
                request.old,
                request.new,
                "OK",
            )
        } else {
            return AccountPatchNameResponse(
                request.old,
                request.old,
                "Patching failed",
            )
        }
    }
}
