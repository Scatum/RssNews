package com.news.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.news.R
import com.news.model.entity.News
import com.news.service.AppDownloadManager
import com.news.view.feed.FeedAdapter
import com.news.view_model.ArchiveViewModel
import kotlinx.android.synthetic.main.fragment_feed.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class ArchiveFragment : BaseFragment() {
    private val archiveViewModel: ArchiveViewModel by viewModel()
    private val appDownloadManager: AppDownloadManager by inject()
    private lateinit var feedAdapter: FeedAdapter
    private var listData = mutableListOf<News>()


    override fun getLayoutId() = R.layout.fragment_archive

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }


    private fun setupRecyclerView() {
        feedAdapter = FeedAdapter(context!!)
        feedRecyclerView.adapter = feedAdapter
        feedRecyclerView.layoutManager = LinearLayoutManager(context)
        setupLocalData()

        feedAdapter.itemNameClickListener = { newsId ->
            navigateToDetailPage(newsId)
        }
        feedAdapter.saveArticle = { news, position ->
            if (!news.isSaved) {
                //download and save
                showLoading("downloading: ${news.title}")
                archiveViewModel.downloadNewBody(news).observe(viewLifecycleOwner, Observer { news ->
                    news?.let { feedAdapter.updateSingleitem(position, it) }
                    hideLoading()
                })
            } else {
                //delete from downloads
                showLoading("removing : ${news.title}")
                archiveViewModel.deleteDownloadedData(news).observe(viewLifecycleOwner, Observer {
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

    private fun setupLocalData() {
        archiveViewModel.getSavedNews().observe(viewLifecycleOwner,
            Observer { t ->
                listData.clear()
                t?.let { listData.addAll(it) }
                feedAdapter.addAll(listData)
            })
    }

}
