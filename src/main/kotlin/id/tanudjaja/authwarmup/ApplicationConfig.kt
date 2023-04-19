package id.tanudjaja.authwarmup

import java.io.FileReader
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.KeyPair
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.PEMKeyPair
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import id.tanudjaja.authwarmup.config.FilePathConfig

@Configuration
class ApplicationConfiguration {

	@Bean
	fun filePathConfiguration(): FilePathConfig {
		return FilePathConfig()
	}
	
	@Bean
	fun privateKey(): RSAPrivateKey? {
		FileReader(filePathConfiguration().privateKey).use { keyReader ->
            val pemParser = PEMParser(keyReader)
            val converter = JcaPEMKeyConverter()
            val pemObject = pemParser.readObject()
	        return when (pemObject) {
	            is PrivateKeyInfo -> {
	                val privateKeyInfo = pemObject
	                converter.getPrivateKey(privateKeyInfo) as RSAPrivateKey?
	            }
	            is PEMKeyPair -> {
	                val pemKeyPair = pemObject
	                val keyPair = converter.getKeyPair(pemKeyPair)
	                val privateKey = keyPair.private as RSAPrivateKey
	                privateKey
	            }
	            else -> null
	        }
        }
	}
	
	@Bean
	fun publicKey(): RSAPublicKey? {
	    FileReader(filePathConfiguration().publicKey).use { keyReader ->
			val pemParser = PEMParser(keyReader)
			val converter = JcaPEMKeyConverter()
			val publicKeyInfo = SubjectPublicKeyInfo.getInstance(pemParser.readObject())
			return converter.getPublicKey(publicKeyInfo) as RSAPublicKey?
	    }
	}
}
