package com.example.request.data.repository

import com.example.request.data.local.CompanyDao
import com.example.request.data.local.CompanyEntity
import com.example.request.data.seed.CompanySeedData
import com.example.request.domain.model.LeadStatus
import kotlinx.coroutines.flow.Flow

class CompanyRepository(
    private val companyDao: CompanyDao,
) {
    fun observeCompanies(): Flow<List<CompanyEntity>> = companyDao.observeAll()

    fun observeCompany(id: Long): Flow<CompanyEntity?> = companyDao.observeById(id)

    suspend fun seedIfEmpty() {
        if (companyDao.count() == 0) {
            companyDao.upsertAll(CompanySeedData.tierACompanies)
        }
    }

    suspend fun saveCompany(company: CompanyEntity) {
        companyDao.upsert(company.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun updateStatus(id: Long, status: LeadStatus) {
        companyDao.updateStatus(id = id, status = status)
    }

    suspend fun updateContactName(id: Long, contactName: String?) {
        companyDao.updateContactName(id = id, contactName = contactName)
    }

    suspend fun updateEmail(id: Long, email: String?) {
        companyDao.updateEmail(id = id, email = email)
    }

    suspend fun updateNotes(id: Long, notes: String?) {
        companyDao.updateNotes(id = id, notes = notes)
    }
}
