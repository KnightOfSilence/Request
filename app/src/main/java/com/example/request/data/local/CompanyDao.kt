package com.example.request.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.request.domain.model.LeadStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface CompanyDao {
    @Query("SELECT * FROM companies ORDER BY priority ASC, name ASC")
    fun observeAll(): Flow<List<CompanyEntity>>

    @Query("SELECT * FROM companies WHERE id = :id LIMIT 1")
    fun observeById(id: Long): Flow<CompanyEntity?>

    @Query("SELECT COUNT(*) FROM companies")
    suspend fun count(): Int

    @Upsert
    suspend fun upsert(company: CompanyEntity)

    @Upsert
    suspend fun upsertAll(companies: List<CompanyEntity>)

    @Query("UPDATE companies SET status = :status, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateStatus(
        id: Long,
        status: LeadStatus,
        updatedAt: Long = System.currentTimeMillis(),
    )

    @Query("UPDATE companies SET contactName = :contactName, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateContactName(
        id: Long,
        contactName: String?,
        updatedAt: Long = System.currentTimeMillis(),
    )

    @Query("UPDATE companies SET email = :email, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateEmail(
        id: Long,
        email: String?,
        updatedAt: Long = System.currentTimeMillis(),
    )

    @Query("UPDATE companies SET notes = :notes, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateNotes(
        id: Long,
        notes: String?,
        updatedAt: Long = System.currentTimeMillis(),
    )
}
