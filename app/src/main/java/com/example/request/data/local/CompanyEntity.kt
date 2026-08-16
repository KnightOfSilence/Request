package com.example.request.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.request.domain.model.LeadStatus

@Entity(tableName = "companies")
data class CompanyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val priority: Int,
    val name: String,
    val country: String,
    val website: String,
    val sector: String,
    val buyingEvidence: String,
    val contactRole: String,
    val contactName: String? = null,
    val email: String? = null,
    val status: LeadStatus = LeadStatus.NEW,
    val notes: String? = null,
    val lastContactAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)
