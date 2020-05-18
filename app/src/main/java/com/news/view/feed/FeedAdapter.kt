package com.news.view.feed

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.booquotes.customVIew.SaveImageView
import com.news.R
import com.news.extension.loadImg
import com.news.extension.onClick
import com.news.model.entity.News
import kotlinx.android.synthetic.main.feed_new_item.view.*

private const val VIEW_TYPE_NEW = 1
private const val VIEW_TYPE_DEFAULT = 2

class FeedAdapter(
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemNameClickListener: ((Int) -> Unit)? = null
    var saveArticle: ((News, Int) -> Unit)? = null


    /**
     * I Usually don't use entity for any adapter.
    I will use news entity inside this adapter because there
    is no good reason to create a new model which will consist by this entity
     * */
    var newsList: MutableList<News> = mutableListOf()


    private inner class ViewHolderNewItem internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private var title: AppCompatTextView = itemView.feedItemTitleView
        private var image: AppCompatImageView = itemView.feedItemImage
        private var save: SaveImageView = itemView.saveImageView
        private var date: AppCompatTextView = itemView.publishedDate

        internal fun bind(position: Int) {
            val item = newsList?.get(position)
            item?.let {
                title.text = it.title
                date.text = it.pubDate
                image.loadImg(it.image)
                save.init(it)
            }
            itemView.onClick {
                itemNameClickListener?.invoke(item.id)
                if (item.isNew) {
                    item.isNew = false
                    notifyItemChanged(position)
                }

            }
            save.onClick {
                saveArticle?.invoke(item, position)
            }

        }
    }

    private inner class ViewHolderDefault internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private var title: AppCompatTextView = itemView.feedItemTitleView
        private var image: AppCompatImageView = itemView.feedItemImage
        private var save: SaveImageView = itemView.saveImageView
        private var date: AppCompatTextView = itemView.publishedDate

        internal fun bind(position: Int) {
            val item = newsList?.get(position)
            item?.let {
                title.text = it.title
                date.text = it.pubDate
                image.loadImg(it.image)
                save.init(it)
            }

            itemView.onClick {
                itemNameClickListener?.invoke(item.id)
                if (item.isNew) {
                    item.isNew = false
                    notifyItemChanged(position)
                }
            }
            save.onClick {
                saveArticle?.invoke(item, position)
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_NEW) {
            ViewHolderNewItem(
                LayoutInflater.from(context).inflate(R.layout.feed_new_item, parent, false)
            )
        } else ViewHolderDefault(
            LayoutInflater.from(context).inflate(R.layout.feed_item, parent, false)
        ) //if it's not VIEW_TYPE_ONE then its VIEW_TYPE_TWO
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (newsList != null && newsList?.get(position)?.isNew!!) {
            (holder as ViewHolderNewItem).bind(position)
        } else {
            (holder as ViewHolderDefault).bind(position)
        }
    }

    override fun getItemCount(): Int {
        return if (newsList == null) 0 else newsList?.size!!
    }

    public fun addAll(mutableList: MutableList<News>) {
        this.newsList?.clear()
        this.newsList?.addAll(mutableList)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (newsList != null && newsList?.get(position)!!.isNew) {
            VIEW_TYPE_NEW
        } else VIEW_TYPE_DEFAULT
    }

    fun updateSingleitem(position: Int, news: News) {
        if (newsList[position].id == news.id) {
            newsList[position].allText = news.allText
            newsList[position].isSaved = news.isSaved
            newsList[position].savedImagePath = news.savedImagePath
            notifyItemChanged(position)
        }
    }
}