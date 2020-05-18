package com.news.service

class AppSettingsService {
    private var appForeground = false
    fun foreground() = appForeground
    fun foreground(foreground: Boolean) {
        appForeground = foreground
    }

}