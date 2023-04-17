package id.tanudjaja.authwarmup.account

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
		
		service.save(Account(
			request.phoneNumber,
			request.name,
			request.password
		))
			
    	return AccountPostResponse("OK")
    }
    
    @GetMapping
    fun get(): AccountPostResponse {
    	return AccountPostResponse("OK") 
    }
}
