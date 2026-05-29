package com.sksdesign.viewit.adblock

interface AdblockEngine {
    fun isEnabled(): Boolean
    fun shouldBlock(url: String, resourceType: String? = null): Boolean
    fun updateLists()
    fun addUserRule(rule: String)
    fun removeUserRule(rule: String)
}
