package com.sksdesign.viewit.data.repository

import android.content.Context
import com.sksdesign.viewit.data.model.PrivacyDnsProfile
import com.sksdesign.viewit.data.storage.PreferencesStore

class PrivacyRepository(context: Context) {
    private val store = PreferencesStore(context)

    fun profiles(): List<PrivacyDnsProfile> = PrivacyDnsProfile.dnsforgeProfiles()
    fun selectedProfileId(): String = store.getString(KEY_SELECTED_PROFILE, "normal")
    fun saveSelectedProfile(id: String) = store.putString(KEY_SELECTED_PROFILE, id)
    fun customDnsValues(): String = store.getString(KEY_CUSTOM_DNS, "")
    fun saveCustomDnsValues(value: String) = store.putString(KEY_CUSTOM_DNS, value)

    companion object {
        private const val KEY_SELECTED_PROFILE = "privacy_dns_profile"
        private const val KEY_CUSTOM_DNS = "custom_dns_values"
    }
}
