package com.example.request.data.seed

import com.example.request.data.local.CompanyEntity

object CompanySeedData {
    val tierACompanies = listOf(
        CompanyEntity(
            priority = 1,
            name = "Partner & Co",
            country = "France",
            website = "https://www.partnerandco.fr/produit/tourteaux-de-soja-press-bio-import/",
            sector = "Trader / Feed",
            buyingEvidence = """
                Offers organic soybean press cake.
                References Regulation (EU) 2018/848.
                Certification FR-BIO-10.
                Mechanical cold pressing.
                Solvent-free processing.
            """.trimIndent(),
            contactRole = "Purchasing Manager / Responsable Achats Matieres Premieres / Directeur Approvisionnement",
        ),
        CompanyEntity(
            priority = 2,
            name = "UFAB - Union Francaise d'Agriculture Biologique",
            country = "France",
            website = "https://www.ufab-bio.fr/negoce/soja-bio/",
            sector = "Trader / Feed",
            buyingEvidence = """
                Works directly with organic soy.
                States that soybean press cake from pressing is used in organic poultry and cattle feed.
            """.trimIndent(),
            contactRole = "Responsable Negoce / Acheteur Matieres Premieres Bio / Responsable Approvisionnement",
        ),
        CompanyEntity(
            priority = 3,
            name = "Agriprotein GmbH",
            country = "Germany",
            website = "https://agriprotein.de/produkte/",
            sector = "Feed / Trader / Processor",
            buyingEvidence = """
                Organic soybeans are mechanically processed.
                Oil is removed using cold pressing.
                Resulting product is Bio-Sojakuchen.
            """.trimIndent(),
            contactRole = "Rohstoffeinkauf / Leiter Einkauf / Procurement Manager Bio-Rohstoffe",
        ),
        CompanyEntity(
            priority = 4,
            name = "SojaPress / Groupe Terres du Sud",
            country = "France",
            website = "https://www.groupe-terresdusud.fr/acteur-economie-locale/nos-marques/soja-press",
            sector = "Feed / Processor",
            buyingEvidence = """
                Produces organic soybean cake for organic feed.
                Official process is described as mechanical cooking/pressing.
                No chemical products are used.
            """.trimIndent(),
            contactRole = "Responsable Achats Matieres Premieres / Procurement Manager - Nutrition Animale",
        ),
        CompanyEntity(
            priority = 5,
            name = "Bio Futuro Srl",
            country = "Italy",
            website = "https://www.biofuturosrl.it/per-uso-zootecnico/panello-di-soia-biologico",
            sector = "Trader / Feed",
            buyingEvidence = """
                Offers certified organic soybean cake / meal.
                Product is obtained through soybean pressing.
                Intended for high-protein animal feed.
            """.trimIndent(),
            contactRole = "Responsabile Acquisti / Raw Materials Buyer / Direttore Commerciale",
        ),
        CompanyEntity(
            priority = 6,
            name = "Olmuhle Mendler",
            country = "Germany",
            website = "https://www.oelmuehle-mendler.de/futtermittel/",
            sector = "Feed / Processor",
            buyingEvidence = """
                Processes soy and rapeseed into press cake and feed oils.
                Uses cold pressing.
                Handles organic oilseeds and organic processing.
            """.trimIndent(),
            contactRole = "Rohstoffeinkauf / Betriebsleitung Olmuhle / Einkauf Bio-Olsaaten",
        ),
        CompanyEntity(
            priority = 7,
            name = "Semhof",
            country = "Germany",
            website = "https://www.semhof.de/semhof-bio-sojaflocken.html",
            sector = "Feed",
            buyingEvidence = """
                Organic soy cake.
                Explicitly mechanically pressed.
                Explicitly without solvents.
                Explicitly not extraction meal.
            """.trimIndent(),
            contactRole = "Einkauf Futtermittel / Rohstoffeinkauf / Produktmanagement Tierernahrung",
        ),
    )
}
