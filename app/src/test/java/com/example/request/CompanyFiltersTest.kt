package com.example.request

import com.example.request.data.local.CompanyEntity
import com.example.request.domain.model.LeadStatus
import com.example.request.ui.companies.filterCompanies
import org.junit.Assert.assertEquals
import org.junit.Test

class CompanyFiltersTest {
    private val companies = listOf(
        CompanyEntity(
            id = 1,
            priority = 1,
            name = "Partner & Co",
            country = "France",
            website = "https://partner.example",
            sector = "Trader / Feed",
            buyingEvidence = "Organic soybean press cake",
            contactRole = "Purchasing Manager",
            status = LeadStatus.NEW,
        ),
        CompanyEntity(
            id = 2,
            priority = 2,
            name = "Agriprotein GmbH",
            country = "Germany",
            website = "https://agriprotein.example",
            sector = "Processor",
            buyingEvidence = "Cold pressing",
            contactRole = "Rohstoffeinkauf",
            status = LeadStatus.READY,
        ),
    )

    @Test
    fun filtersBySearchQueryAcrossCompanyFields() {
        val result = filterCompanies(
            companies = companies,
            searchQuery = "cold",
            country = null,
            status = null,
        )

        assertEquals(listOf("Agriprotein GmbH"), result.map { it.name })
    }

    @Test
    fun filtersByCountryAndStatusTogether() {
        val result = filterCompanies(
            companies = companies,
            searchQuery = "",
            country = "Germany",
            status = LeadStatus.READY,
        )

        assertEquals(listOf("Agriprotein GmbH"), result.map { it.name })
    }
}
