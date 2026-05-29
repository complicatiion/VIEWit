package com.sksdesign.viewit.device

import android.content.Context
import com.sksdesign.viewit.data.model.DeviceMode
import com.sksdesign.viewit.data.model.TvModeSettings

object LayoutModeResolver {
    fun shouldUseTvLayout(context: Context, settings: TvModeSettings): Boolean {
        return settings.deviceMode == DeviceMode.TV
    }
}
