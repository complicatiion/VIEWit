package com.sksdesign.viewit.data.model

data class PrivacyDnsProfile(
    val id: String,
    val name: String,
    val ipv4: List<String> = emptyList(),
    val ipv6: List<String> = emptyList(),
    val dot: String = "",
    val doh: String = "",
    val description: String = ""
) {
    fun copyText(): String = buildString {
        appendLine(name)
        if (ipv4.isNotEmpty()) appendLine("IPv4: ${ipv4.joinToString(", ")}")
        if (ipv6.isNotEmpty()) appendLine("IPv6: ${ipv6.joinToString(", ")}")
        if (dot.isNotBlank()) appendLine("DoT: $dot")
        if (doh.isNotBlank()) appendLine("DoH: $doh")
    }.trim()

    companion object {
        fun dnsforgeProfiles(): List<PrivacyDnsProfile> = listOf(
            PrivacyDnsProfile(
                id = "normal",
                name = "DNSforge Normal",
                ipv4 = listOf("49.12.67.122", "91.99.154.175", "176.9.93.198", "176.9.1.117"),
                ipv6 = listOf("2a01:4f8:c013:29d::122", "2a01:4f8:c010:8c35::175", "2a01:4f8:151:34aa::198", "2a01:4f8:141:316d::117"),
                dot = "dnsforge.de",
                doh = "https://dnsforge.de/dns-query",
                description = "Default DNSforge profile."
            ),
            PrivacyDnsProfile(id = "clean", name = "DNSforge Clean", dot = "clean.dnsforge.de", description = "DNSforge profile with additional filtering."),
            PrivacyDnsProfile(id = "hard", name = "DNSforge Hard", dot = "hard.dnsforge.de", description = "Stricter DNSforge profile."),
            PrivacyDnsProfile(id = "blank", name = "DNSforge Blank", dot = "blank.dnsforge.de", description = "Blank DNSforge profile.")
        )
    }
}
