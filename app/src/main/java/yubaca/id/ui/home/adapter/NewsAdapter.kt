package yubaca.id.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail_news.*
import kotlinx.android.synthetic.main.item_news.view.*
import yubaca.id.R
import yubaca.id.data.model.News
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class NewsAdapter(private val listener: OnNewsArticleOnClickListener) : RecyclerView.Adapter<NewsAdapter.NewsHolder>() {

    private var articleList: List<News> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return NewsHolder(view)
    }

    override fun getItemCount(): Int = articleList.size

    override fun onBindViewHolder(holder: NewsHolder, position: Int) = holder.bind(articleList[position])

    fun add(dataList: List<News>) {

        articleList = dataList
        notifyDataSetChanged()
    }

    fun clear() {

        articleList = emptyList()
        notifyDataSetChanged()
    }

    inner class NewsHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(news: News) {

            itemView.apply {

                val getDate: String? = news.publishedAt

                tvTitle.text = news.title
                tvDate.text = getDate?.let { convertISOTimeToDate(it) }
                Glide.with(this).load(news.urlToImage).into(imgNews)

                ivFavorite.setOnClickListener { listener.onClickBookmark(news) }

            }.setOnClickListener { listener.onClickNews(news) }
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun convertISOTimeToDate(isoTime: String): String? {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SSS")
        var convertedDate: Date? = null
        var formattedDate: String? = null
        try {
            convertedDate = sdf.parse(isoTime)
            formattedDate = SimpleDateFormat("dd MMMM yyyy").format(convertedDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return formattedDate
    }

    interface OnNewsArticleOnClickListener {

        fun onClickNews(news: News)

        fun onClickShare(url: String)

        fun onClickBookmark(news: News)
    }
}