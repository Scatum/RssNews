package com.news

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.news.constant.AppTheme
import com.news.extension.hide
import com.news.extension.show
import com.news.service.PreferenceService
import kotlinx.android.synthetic.main.loading_layout.*
import org.koin.android.ext.android.inject

open class BaseActivity : AppCompatActivity() {
    val preferenceService:PreferenceService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkInterfaceMode()

    }

    public fun showLoading(message:String = "") {
        messageTextView?.text = message
        appProgressBarContainer?.show()
    }

    public fun hideLoading() {
        messageTextView?.text = ""
        appProgressBarContainer?.hide()
    }

    private fun checkInterfaceMode() {
        if (preferenceService.getInterfaceMode() == AppTheme.LIGHT) {
            setTheme(R.style.LightTheme)
            // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            setTheme(R.style.DarkTheme)
            //  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }



}
