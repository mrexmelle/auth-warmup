package id.tanudjaja.authwarmup.session

import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.time.ZonedDateTime
import java.time.ZoneId
import java.time.ZoneOffset;
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.stereotype.Service
import id.tanudjaja.authwarmup.account.AccountService
import id.tanudjaja.authwarmup.config.FilePathConfig

@Service
class SessionService(
	private val accountService: AccountService,
	private val rsaPrivateKey: RSAPrivateKey,
	private val rsaPublicKey: RSAPublicKey
) {
	fun authenticate(
		request: SessionPostRequest
	): Boolean {
		return accountService.authenticate(
			request.phoneNumber,
			request.password
		)
	}
	
	fun generateJwt(
		phoneNumber: String
	): String {
		
		val now = ZonedDateTime.now(ZoneId.of("Asia/Jakarta"))
		return JWT.create()
			.withIssuer("auth-warmup")
			.withSubject(phoneNumber)
			.withIssuedAt(now.toInstant())
			.withNotBefore(now.toInstant())
			.withExpiresAt(now.plusDays(1).toInstant())
			.sign(Algorithm.RSA256(rsaPublicKey, rsaPrivateKey))	
	}
}
