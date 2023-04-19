package id.tanudjaja.authwarmup.account

import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

@Service
class AccountService(
	private val repo: AccountRepository,
	private val rsaPrivateKey: RSAPrivateKey,
	private val rsaPublicKey: RSAPublicKey
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
	
	@Transactional
	fun create(
		request: AccountPostRequest
	): Account {		
		return repo.save(Account(
			request.phoneNumber,
			request.name,
			BCrypt.hashpw(request.password, BCrypt.gensalt())
		))
	}
	
	fun retrieveNameByPhoneNumber(
		phoneNumber: String
	): String {
		return repo.findByPhoneNumber(phoneNumber)?.name ?: ""
	}
	
	@Transactional
	fun updateName(
		phoneNumber: String,
		request: AccountPatchNameRequest
	): Boolean {
		return repo.UpdateNameByNameAndPhoneNumber(
			request.new,
			request.old,
			phoneNumber
		) > 0
	}
	
	fun authorize(
		token: String
	): String {
		return JWT
			.require(Algorithm.RSA256(rsaPublicKey, null))
			.withIssuer("auth-warmup")
			.build()
			.verify(token)
			.getSubject()
	}
	
	fun getBearerToken(
		authorization: String
	): String {
		val trimmed = authorization.replace(" ", "")
		
    	val type = trimmed.take(6).lowercase()
		if (type != "bearer") {
			return ""
		}
		
    	return trimmed.drop(6)
	}
}
