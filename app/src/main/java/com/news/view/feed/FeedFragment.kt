package com.news.view.feed

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.news.R
import com.news.model.entity.News
import com.news.service.AppDownloadManager
import com.news.view.BaseFragment
import com.news.view_model.FeedViewModel
import kotlinx.android.synthetic.main.fragment_feed.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class FeedFragment : BaseFragment() {
    private val feedViewModel: FeedViewModel by viewModel()
    private val appDownloadManager: AppDownloadManager by inject()
    private lateinit var feedAdapter: FeedAdapter
    private var listData = mutableListOf<News>()


    override fun getLayoutId() = R.layout.fragment_feed

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        updateNewsStatus()
    }

    private fun updateNewsStatus() {
        feedViewModel.updateNewStatus(false)
    }

    private fun setupRecyclerView() {
        feedAdapter = FeedAdapter(context!!)
        feedRecyclerView.adapter = feedAdapter
        feedRecyclerView.layoutManager = LinearLayoutManager(context)
        setupLocalData()
        feedViewModel.fetchData().observe(viewLifecycleOwner, Observer {
            if (it) {
                setupLocalData()
            }
        })

        feedAdapter.itemNameClickListener = { newsId ->
            navigateToDetailPage(newsId)
        }
        feedAdapter.saveArticle = {news, position ->
            if (!news.isSaved) {
                //download and save
                showLoading("downloading: ${news.title}")
                feedViewModel.downloadNewsBody(news).observe(viewLifecycleOwner, Observer { news->
                    news?.let { feedAdapter.updateSingleitem(position, it) }
                    hideLoading()
                })
            }else{
                //delete from downloads
                showLoading("removing : ${news.title}")
                feedViewModel.deleteDownloadedData(news).observe(viewLifecycleOwner, Observer {
                    feedAdapter.updateSingleitem(position, it)
                    hideLoading()
                })
            }
        }

    }

    private fun navigateToDetailPage(newsId: Int) {
        val bundle = Bundle()
        bundle.putInt("newsId", newsId)
        view?.let {
            Navigation.findNavController(it).navigate(R.id.action_open_detail_fragment, bundle)
        }
    }

    fun setupLocalData() {
        feedViewModel.getData().observe(viewLifecycleOwner,
            Observer { t ->
                listData?.clear()
                t?.let { listData.addAll(it) }
                feedAdapter.addAll(listData)
            })
    }

}
