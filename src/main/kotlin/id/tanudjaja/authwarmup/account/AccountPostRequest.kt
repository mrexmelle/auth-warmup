package id.tanudjaja.authwarmup.account

data class AccountPostRequest(
    val phoneNumber: String,
    val name: String,
    val password: String,
)
