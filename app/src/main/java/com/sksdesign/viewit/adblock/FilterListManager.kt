package com.sksdesign.viewit.adblock

import com.sksdesign.viewit.data.model.FilterListEntry

class FilterListManager {
    fun toggle(list: FilterListEntry): FilterListEntry = list.copy(enabled = !list.enabled)
    fun markUpdated(list: FilterListEntry): FilterListEntry = list.copy(lastUpdated = System.currentTimeMillis())
}
