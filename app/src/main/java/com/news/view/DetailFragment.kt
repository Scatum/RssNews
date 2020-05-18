package com.news.view

import android.R.attr.defaultValue
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.news.R
import com.news.extension.*
import com.news.model.entity.News
import com.news.service.AppDownloadManager
import com.news.util.Utils.isNetworkAvailable
import com.news.util.web_view.WebClient
import com.news.view_model.DetailViewModel
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_detail.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class DetailFragment : BaseFragment() {
    val appDownloadManager: AppDownloadManager by inject()
    val detailViewModel: DetailViewModel by viewModel()
    private var newsId: Int = -1


    override fun getLayoutId() = R.layout.fragment_detail


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        initData()
    /*    webViewDetail?.webViewClient = WebClient()
        webViewDetail?.settings?.javaScriptEnabled = true*/


//class="article-body"

    }

    private fun initData() {
        val bundle = this.arguments
        if (bundle != null) {
            newsId = bundle.getInt("newsId", defaultValue)
        }

        detailViewModel.getNewsById(newsId)?.observe(viewLifecycleOwner,
            Observer {
                setupArchive(it)
                if (it.isSaved) {
                    loadingSavedData(it)
                } else {
                    loadFromNetwork(it)
                }
            })


    }

    fun loadFromNetwork(news: News) {

        if (!isNetworkAvailable(context!!)) {
            newsDetailImage.hide()
            networkError?.show()
        }

        detailViewModel.getNewsBodyData(news).observe(viewLifecycleOwner, object : Observer<News?> {
            override fun onChanged(t: News?) {
//                webViewDetail.loadData(t?.allText, "text/html", "UTF-8")
                t?.allText?.let { newsContent.asHtmlContent(it) }
            }
        })
        newsDetailImage?.loadImg(news.image)
        publishedDate?.text = news.pubDate
        titleTextView?.text = news.title

    }

    fun loadingSavedData(news: News) {
        newsDetailImage?.loadImg(news.savedImagePath)
        publishedDate?.text = news.pubDate
        titleTextView?.text = news.title
        news?.allText?.let { newsContent.asHtmlContent(it) }


    }

    private fun setupToolbar() {
        ivLeftView?.onClick {
            this@DetailFragment.view?.let {
                Navigation.findNavController(it).popBackStack()
            }
        }
    }

    fun setupArchive(news: News) {
        saveImageView.init(news)
        saveImageView?.onClick {
            if (!news.isSaved) {
                //download and save
                showLoading("downloading: ${news.title}")
                detailViewModel.downloadNewBody(news).observe(viewLifecycleOwner, Observer {news->
                    news?.let { saveImageView.init(it) }
                    hideLoading()
                })
            } else {
                //delete from downloads
                showLoading("removing : ${news.title}")
                detailViewModel.deleteDownloadedData(news).observe(viewLifecycleOwner, Observer {
                    saveImageView.init(news)
                    hideLoading()
                })
            }

        }
    }

}
