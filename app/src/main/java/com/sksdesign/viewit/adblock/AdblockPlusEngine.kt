package com.sksdesign.viewit.adblock

class AdblockPlusEngine : AdblockEngine {
    override fun isEnabled(): Boolean = false
    override fun shouldBlock(url: String, resourceType: String?): Boolean = false
    override fun updateLists() = Unit
    override fun addUserRule(rule: String) = Unit
    override fun removeUserRule(rule: String) = Unit

    fun integrationStatus(): String = "AdblockPlus integration is prepared as a stub. Add a local AAR or module to enable a real adapter."
}
