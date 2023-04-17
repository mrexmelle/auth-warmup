package id.tanudjaja.authwarmup.account

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name="accounts")
class Account(
    @Id
    val phoneNumber: String = "",

    @Column(nullable = false)
    val name: String = "",

    @Column(nullable = false)
    val passwordHash: String = ""
)