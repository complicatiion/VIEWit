package com.sksdesign.viewit.data.model

enum class DeviceMode {
    MOBILE,
    TV;

    companion object {
        fun fromStored(value: String?): DeviceMode {
            return if (value == TV.name) TV else MOBILE
        }
    }
}
