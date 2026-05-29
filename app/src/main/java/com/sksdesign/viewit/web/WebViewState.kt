package com.sksdesign.viewit.web

data class WebViewState(
    val progress: Int = 0,
    val title: String = "",
    val currentUrl: String = "",
    val canGoBack: Boolean = false,
    val canGoForward: Boolean = false,
    val hasError: Boolean = false,
    val errorMessage: String = ""
)
