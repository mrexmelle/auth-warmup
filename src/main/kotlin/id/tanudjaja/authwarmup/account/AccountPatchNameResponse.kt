package id.tanudjaja.authwarmup.account

data class AccountPatchNameResponse(
    val old: String,
    val new: String,
    val status: String
)