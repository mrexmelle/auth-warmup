package id.tanudjaja.authwarmup

import id.tanudjaja.authwarmup.config.FilePathConfig
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
import org.bouncycastle.openssl.PEMKeyPair
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.FileReader
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

@Configuration
@OpenAPIDefinition(
    info = Info(
        title = "Auth Warmup Project",
        version = "v0.1.0",
        description = "A weekend project to refresh about authentication",
    ),
)
class ApplicationConfig {

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
