package id.tanudjaja.authwarmup.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "file-path")
data class FilePathConfig(
    var privateKey: String = "",
    var publicKey: String = ""
)
