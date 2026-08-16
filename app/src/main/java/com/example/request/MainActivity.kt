package com.example.request

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.request.data.local.AppDatabase
import com.example.request.data.local.CompanyEntity
import com.example.request.data.repository.CompanyRepository
import com.example.request.domain.model.LeadStatus
import com.example.request.ui.companies.filterCompanies
import com.example.request.ui.theme.RequestTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = CompanyRepository(
            AppDatabase.getInstance(applicationContext).companyDao(),
        )

        setContent {
            RequestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    CompaniesRoute(repository = repository)
                }
            }
        }
    }
}

@Composable
private fun CompaniesRoute(repository: CompanyRepository) {
    val companies by repository.observeCompanies().collectAsState(initial = emptyList())
    var selectedCompanyId by remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(repository) {
        repository.seedIfEmpty()
    }

    val selectedCompany = selectedCompanyId?.let { id ->
        companies.firstOrNull { company -> company.id == id }
    }

    if (selectedCompany == null) {
        CompaniesScreen(
            companies = companies,
            onCompanySelected = { selectedCompanyId = it.id },
        )
    } else {
        CompanyDetailScreen(
            company = selectedCompany,
            repository = repository,
            onBack = { selectedCompanyId = null },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CompaniesScreen(
    companies: List<CompanyEntity>,
    onCompanySelected: (CompanyEntity) -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCountry by remember { mutableStateOf<String?>(null) }
    var selectedStatus by remember { mutableStateOf<LeadStatus?>(null) }

    val countries = remember(companies) {
        companies
            .map { it.country }
            .distinct()
            .sorted()
    }
    val visibleCompanies = remember(companies, searchQuery, selectedCountry, selectedStatus) {
        filterCompanies(
            companies = companies,
            searchQuery = searchQuery,
            country = selectedCountry,
            status = selectedStatus,
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = "Companies")
                        Text(
                            text = "${visibleCompanies.size} of ${companies.size}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search by company, website, sector, evidence") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(12.dp))

            FilterSection(
                title = "Country",
                chips = listOf(null to "All") + countries.map { it to it },
                selected = selectedCountry,
                onSelected = { selectedCountry = it },
            )

            Spacer(modifier = Modifier.height(8.dp))

            FilterSection(
                title = "Status",
                chips = listOf(null to "All") + LeadStatus.entries.map { it to it.displayName() },
                selected = selectedStatus,
                onSelected = { selectedStatus = it },
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (visibleCompanies.isEmpty()) {
                EmptyCompaniesState(modifier = Modifier.fillMaxSize())
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(
                        items = visibleCompanies,
                        key = { it.id },
                    ) { company ->
                        CompanyRow(
                            company = company,
                            onClick = { onCompanySelected(company) },
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> FilterSection(
    title: String,
    chips: List<Pair<T?, String>>,
    selected: T?,
    onSelected: (T?) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.horizontalScroll(rememberScrollState()),
        ) {
            chips.forEach { (value, label) ->
                FilterChip(
                    selected = selected == value,
                    onClick = { onSelected(value) },
                    label = { Text(text = label) },
                )
            }
        }
    }
}

@Composable
private fun CompanyRow(
    company: CompanyEntity,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(14.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(horizontal = 9.dp, vertical = 5.dp),
                ) {
                    Text(
                        text = "#${company.priority}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = company.name,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = "${company.country} · ${company.sector}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                StatusPill(status = company.status)
            }

            Text(
                text = company.website,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = company.buyingEvidence,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = company.contactRole,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CompanyDetailScreen(
    company: CompanyEntity,
    repository: CompanyRepository,
    onBack: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var contactName by remember(company.id) { mutableStateOf(company.contactName.orEmpty()) }
    var email by remember(company.id) { mutableStateOf(company.email.orEmpty()) }
    var notes by remember(company.id) { mutableStateOf(company.notes.orEmpty()) }
    var selectedStatus by remember(company.id) { mutableStateOf(company.status) }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text(text = "Back")
                    }
                },
                title = {
                    Column {
                        Text(
                            text = company.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = "${company.country} · ${company.sector}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            DetailSection(title = "Company") {
                DetailField(label = "Company Name", value = company.name)
                DetailField(label = "Country", value = company.country)
                DetailField(label = "Website", value = company.website)
                DetailField(label = "Segment", value = company.sector)
            }

            DetailSection(title = "Buying Evidence") {
                Text(
                    text = company.buyingEvidence,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            DetailSection(title = "Contact") {
                DetailField(label = "Contact Position", value = company.contactRole)
                OutlinedTextField(
                    value = contactName,
                    onValueChange = { contactName = it },
                    label = { Text(text = "Contact Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                    ),
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(text = "Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                )
            }

            DetailSection(title = "Status") {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                ) {
                    LeadStatus.entries.forEach { status ->
                        FilterChip(
                            selected = selectedStatus == status,
                            onClick = { selectedStatus = status },
                            label = { Text(status.displayName()) },
                        )
                    }
                }
            }

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text(text = "Notes") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                ),
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            repository.saveCompany(
                                company.copy(
                                    contactName = contactName.trim().ifBlank { null },
                                    email = email.trim().ifBlank { null },
                                    notes = notes.trim().ifBlank { null },
                                    status = selectedStatus,
                                ),
                            )
                        }
                    },
                ) {
                    Text(text = "Save")
                }
                Button(
                    onClick = {
                        coroutineScope.launch {
                            repository.saveCompany(
                                company.copy(
                                    contactName = contactName.trim().ifBlank { null },
                                    email = email.trim().ifBlank { null },
                                    notes = notes.trim().ifBlank { null },
                                    status = LeadStatus.READY,
                                ),
                            )
                            selectedStatus = LeadStatus.READY
                        }
                    },
                ) {
                    Text(text = "Prepare email")
                }
            }
        }
    }
}

@Composable
private fun DetailSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(14.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
            content()
        }
    }
}

@Composable
private fun DetailField(label: String, value: String?) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value?.takeIf { it.isNotBlank() } ?: "-",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun StatusPill(status: LeadStatus) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(horizontal = 8.dp, vertical = 5.dp),
    ) {
        Text(
            text = status.displayName(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun EmptyCompaniesState(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        Text(
            text = "No companies match the current filters",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private fun LeadStatus.displayName(): String =
    when (this) {
        LeadStatus.NEW -> "New"
        LeadStatus.READY -> "Ready"
        LeadStatus.SENT -> "Sent"
        LeadStatus.REPLIED -> "Replied"
        LeadStatus.NOT_INTERESTED -> "Not interested"
    }
