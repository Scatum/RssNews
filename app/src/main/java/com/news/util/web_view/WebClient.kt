package com.news.util.web_view

import android.webkit.WebView
import android.webkit.WebViewClient

class WebClient : WebViewClient() {
    override fun shouldOverrideUrlLoading(
        view: WebView,
        url: String
    ): Boolean {
        view.loadUrl(url)
        return true
    }
}