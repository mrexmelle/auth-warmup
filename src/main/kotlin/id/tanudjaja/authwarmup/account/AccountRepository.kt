package id.tanudjaja.authwarmup.account

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : JpaRepository<Account, String> {
	fun findByPhoneNumber(phoneNumber: String): Account?
	
	@Modifying
	@Query("UPDATE Account a SET a.name = ?1 WHERE a.name = ?2 AND a.phoneNumber = ?3")
	fun UpdateNameByNameAndPhoneNumber(
		newName: String,
		oldName: String,
		phoneNumber: String
	): Int
}