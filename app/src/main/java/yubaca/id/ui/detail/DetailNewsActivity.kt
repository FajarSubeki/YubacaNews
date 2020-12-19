package yubaca.id.ui.detail

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.ajalt.timberkt.d
import kotlinx.android.synthetic.main.activity_detail_news.*
import yubaca.id.R
import yubaca.id.data.model.News
import yubaca.id.di.module.GlideApp
import yubaca.id.ui.base.BaseActivity
import yubaca.id.util.Constant
import yubaca.id.util.obtainViewModel
import yubaca.id.util.showToast
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DetailNewsActivity : BaseActivity() {

    // set for key value variable
    companion object {

        const val EXTRA_NEWS = "NEWS"
    }

    // inject view model
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: DetailViewModel

    private lateinit var articleUrl: String
    private var currentFavorite = false
    private val detailStateObserver = Observer<DetailState> { state ->

        when (state) {

            is DefaultState -> {

                d { "Default State" }
                setArticle(state.data)
            }

            is ChangeIconState -> {

                d { "Change Icon State" }
                currentFavorite = if (state.isFavorite) {
                    setFilledIcon()
                    true
                } else {

                    setOutlineIcon()
                    false
                }
            }

            is ErrorState -> {

                d { "Error State" }
                showToast(state.errorMessage)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_news)

        viewModel = obtainViewModel().apply {

            detailStateLiveData.observe(this@DetailNewsActivity, detailStateObserver)
        }
        val article = intent.getParcelableExtra<News>(EXTRA_NEWS)

        // get data article when user click news
        viewModel.receivedArticle(article)

    }

    /*
     * Set data to component ui detail
     * */
    private fun setArticle(news: News?) {

        news?.let {

            GlideApp.with(this).load(it.urlToImage).into(ivImageDetail)
            tvTitleNews.text = it.title
            tvAuthour.text = it.author
            tvDate.text = it.publishedAt?.let { it1 -> convertISOTimeToDate(it1) }
            tvDescription.text = it.content ?: Constant.NO_CONTENT
            articleUrl = it.url

            viewModel.checkFavorite(articleUrl)

        }

        ivLike.setOnClickListener {
            favoriteArticle()
        }

        ivBack.setOnClickListener {
            finish()
        }
    }

    /*
     * Convert date news to simple date format
     * */
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
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

    /*
     * Add and favorite to article list
     * */
    private fun favoriteArticle() {

        if (currentFavorite) {

            viewModel.deleteFavorite()
            showToast(resources.getString(R.string.del_favorite))

        } else {

            viewModel.addFavorite()
            showToast(resources.getString(R.string.add_favorite))
        }
    }

    private fun setOutlineIcon() = ivLike.setImageDrawable(ContextCompat.getDrawable(baseContext, R.drawable.ic_favorite))

    private fun setFilledIcon() = ivLike.setImageDrawable(ContextCompat.getDrawable(baseContext, R.drawable.ic_favorite_fill))

    private fun obtainViewModel() = obtainViewModel(viewModelFactory, DetailViewModel::class.java)

    /*
     * remove and clear viewmodel data
     * */
    override fun onDestroy() {
        super.onDestroy()
        viewModel.detailStateLiveData.removeObserver(detailStateObserver)
    }
}