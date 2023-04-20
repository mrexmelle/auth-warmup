package id.tanudjaja.authwarmup.account

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = "Account API")
class AccountController(
    private val service: AccountService,
) {
	@Operation(
		summary = "Create new account",
		operationId = "AccountController.post"
	)
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

	@Operation(
		summary = "Get the name of an account",
		operationId = "AccountController.getName"
	)
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

	@Operation(
		summary = "Update the name of an account",
		operationId = "AccountController.patchName"
	)
    @PatchMapping("/name")
    fun patchName(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authorization: String,
        @RequestBody request: AccountPatchNameRequest,
    ): AccountPatchNameResponse {
    	if (request.old == request.new) {
			return AccountPatchNameResponse(
                request.old,
                request.new,
                "Identical old and new names",
            )
		}
        val bearerToken = service.getBearerToken(authorization)
        if (bearerToken.isEmpty()) {
            return AccountPatchNameResponse(
                request.old,
                request.new,
                "Bearer token invalid",
            )
        }
		
        val phoneNumber = try {
            service.authorize(bearerToken)
        } catch (e: Exception) {
            return AccountPatchNameResponse(
                request.old,
                request.new,
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
                request.new,
                "Patching failed",
            )
        }
    }
}
