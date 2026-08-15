package com.example.request

import com.example.request.data.seed.CompanySeedData
import com.example.request.domain.model.LeadStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun tierASeedDataContainsSevenNewCompaniesOrderedByPriority() {
        val companies = CompanySeedData.tierACompanies

        assertEquals(7, companies.size)
        assertEquals((1..7).toList(), companies.map { it.priority })
        assertTrue(companies.all { it.status == LeadStatus.NEW })
        assertTrue(companies.all { it.website.startsWith("https://") })
    }
}
