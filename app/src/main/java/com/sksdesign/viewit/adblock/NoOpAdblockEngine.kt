package com.sksdesign.viewit.adblock

class NoOpAdblockEngine : AdblockEngine {
    override fun isEnabled(): Boolean = false
    override fun shouldBlock(url: String, resourceType: String?): Boolean = false
    override fun updateLists() = Unit
    override fun addUserRule(rule: String) = Unit
    override fun removeUserRule(rule: String) = Unit
}
