package id.tanudjaja.authwarmup.account

import org.springframework.http.ResponseEntity
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@RestController
@RequestMapping("/accounts")
class AccountController(
	private val service: AccountService
) {
    @PostMapping
    fun post(
    	@RequestBody request: AccountPostRequest)
		: AccountPostResponse {
			
		try {
			service.validate(request)
		} catch (e: Exception) {
			return AccountPostResponse(e.message ?: "Unknown error")
		}
		
		service.save(request)
			
    	return AccountPostResponse("OK")
    }
    
    @GetMapping("/name")
    fun getName(
    	@RequestHeader(HttpHeaders.AUTHORIZATION) authorization: String
    ): AccountGetNameResponse {
    	val trimmed = authorization.replace(" ", "")
		
    	val type = trimmed.take(6).lowercase()
		if (type != "bearer") {
			return AccountGetNameResponse("", "Invalid token type")
		}
		
    	val token = trimmed.drop(6)
		try {
			return AccountGetNameResponse(service.authorize(token), "OK")
		} catch (e: Exception) {
			return AccountGetNameResponse("", e.message ?: "Authorization failed") 
		}
    }
}
