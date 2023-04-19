package id.tanudjaja.authwarmup.account

import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service

@Service
class AccountService(
	private val repo: AccountRepository
) {
	fun validate(
		request: AccountPostRequest
	) {
		val phoneNumberRegex = Regex("^08[0-9]{8,11}$")
        val nameMaxLength = 60
        val passwordRegex = Regex("^(?=.*[A-Z])(?=.*[0-9]).{6,16}$")

        if (!phoneNumberRegex.matches(request.phoneNumber)) {
            throw Exception("Phone Number invalid")
        }
        if (request.name.length > nameMaxLength) {
            throw Exception("Name invalid")
        }
        if (!passwordRegex.matches(request.password)) {
            throw Exception("Password invalid")
        } 
	}
	
	fun authenticate(
		phoneNumber: String,
		password: String
	): Boolean {
		val account = repo.findByPhoneNumber(phoneNumber) ?: return false
		return BCrypt.checkpw(password, account.passwordHash)
	}
	
	fun save(
		request: AccountPostRequest
	): Account {		
		return repo.save(Account(
			request.phoneNumber,
			request.name,
			BCrypt.hashpw(request.password, BCrypt.gensalt())
		))
	}
}
