package com.example.request.ui.companies

import com.example.request.data.local.CompanyEntity
import com.example.request.domain.model.LeadStatus

fun filterCompanies(
    companies: List<CompanyEntity>,
    searchQuery: String,
    country: String?,
    status: LeadStatus?,
): List<CompanyEntity> {
    val normalizedQuery = searchQuery.trim()

    return companies.filter { company ->
        val matchesSearch = normalizedQuery.isBlank() ||
            company.name.contains(normalizedQuery, ignoreCase = true) ||
            company.website.contains(normalizedQuery, ignoreCase = true) ||
            company.sector.contains(normalizedQuery, ignoreCase = true) ||
            company.buyingEvidence.contains(normalizedQuery, ignoreCase = true) ||
            company.contactRole.contains(normalizedQuery, ignoreCase = true)

        val matchesCountry = country == null || company.country == country
        val matchesStatus = status == null || company.status == status

        matchesSearch && matchesCountry && matchesStatus
    }
}
