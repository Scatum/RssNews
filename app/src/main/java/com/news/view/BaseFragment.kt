package com.news.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.news.MainActivity
import com.news.extension.hide
import com.news.extension.show
import kotlinx.android.synthetic.main.loading_layout.*

/**
 * A simple [Fragment] subclass.
 */
abstract class BaseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)

    }

    abstract fun getLayoutId(): Int

    public fun showLoading(message:String = "") {
        (activity as MainActivity?)?.showLoading(message)
    }

    public fun hideLoading() {
        (activity as MainActivity?)?.hideLoading()
    }

}
