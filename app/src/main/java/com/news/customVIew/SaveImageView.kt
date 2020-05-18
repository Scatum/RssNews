package com.booquotes.customVIew

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import android.widget.Toast
import com.news.R
import com.news.extension.onClick
import com.news.model.entity.News
import com.news.service.AppDownloadManager
import kotlinx.android.synthetic.main.custom_save_imageview_layout.view.*

import org.koin.core.KoinComponent
import org.koin.core.inject

class SaveImageView : RelativeLayout, KoinComponent {


    constructor(context: Context) : super(context) {
        setup(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setup(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setup(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        setup(context)
    }

    private fun setup(context: Context) {
        inflate(context, R.layout.custom_save_imageview_layout, this)
    }

    fun init(news: News) {
        checkStateOfIcon(news)
    }

    private fun checkStateOfIcon(news: News) {
        if (news.isSaved) {
            appSaveImageView?.setImageResource(R.drawable.saved)
        } else {
            appSaveImageView?.setImageResource(R.drawable.save)
        }
    }

}