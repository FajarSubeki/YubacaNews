package yubaca.id.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.ajalt.timberkt.d
import kotlinx.android.synthetic.main.fragment_home.*
import yubaca.id.MainActivity
import yubaca.id.R
import yubaca.id.data.model.News
import yubaca.id.ui.base.BaseFragment
import yubaca.id.ui.detail.DetailNewsActivity
import yubaca.id.ui.detail.DetailViewModel
import yubaca.id.ui.home.adapter.NewsAdapter
import yubaca.id.ui.search.SearchFragment
import yubaca.id.util.*
import javax.inject.Inject

class HomeFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener, NewsAdapter.OnNewsArticleOnClickListener {

    // create instance for use in another class
    companion object {

        const val HOME_NEWS = "NEWS_HOME"

        fun newInstance(category: String) = HomeFragment().apply {

            arguments = Bundle().apply {
                putString(HOME_NEWS, category)
            }
        }
    }

    // init inject viewmodel
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: NewsViewModel

    private val mAdapter = NewsAdapter(this)
    private var currentCategory = Constant.ALL
    private var page: Int = 1
    private var pageSize: Int = 20

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        updateCategory()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel().apply {
            newsListStateLiveData.observe(viewLifecycleOwner, newsListStateObserver)
        }

        viewModel.updateNews(currentCategory, page, pageSize)
    }

    /*
     * Init view component in main screen
     * */
    private fun initView() {

        newsLayout.setOnRefreshListener(this)
        btnRefresh.setOnClickListener { onRefresh() }
        btnTryAgain.setOnClickListener { onRefresh() }
        ivSearch.setOnClickListener { moveFragment() }
        rvNews.apply {

            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
    }

    /*
     * Method for apply homefragment call in another fragment
     * */
    private fun moveFragment() {
        val frag= SearchFragment.newInstance()
        (activity as MainActivity).replaceFragment(frag,SearchFragment.TAG)
    }

    private fun updateCategory() {

        val category = arguments?.getString(HOME_NEWS) ?: Constant.ALL
        currentCategory = category
    }

    /*
     * remove viewmodel data when destory activity
     * */
    override fun onDestroy() {
        super.onDestroy()
        viewModel.newsListStateLiveData.removeObserver(newsListStateObserver)
    }

    /*
     * state for process flow receiving data news
     * */
    private val newsListStateObserver = Observer<NewsListState> { state ->

        when (state) {

            is LoadingState -> {

                d { "Loading State" }
                defaultLayoutState()
                newsLayout.isRefreshing = true
            }

            is DefaultState -> {

                d { "Default State" }
                defaultLayoutState()

                mAdapter.add(state.dataList)
                newsLayout.isRefreshing = false
            }

            is EmptyState -> {

                d {"No Data State"}
                emptyLayoutState()
            }

            is ErrorState -> {

                d { "Error State" }
                d { state.errorMessage }
                errorLayoutState()
            }

        }
    }

    private fun defaultLayoutState() {

        newsLayout.toVisible()
        newsLayoutEmpty.toGone()
        newsLayoutError.toGone()
    }

    private fun emptyLayoutState() {

        newsLayout.toGone()
        newsLayoutEmpty.toVisible()
        newsLayoutError.toGone()
    }

    private fun errorLayoutState() {

        newsLayout.toGone()
        newsLayoutEmpty.toGone()
        newsLayoutError.toVisible()
    }


    private fun obtainViewModel() = obtainViewModel(viewModelFactory, NewsViewModel::class.java)

    /*
     * method for call news list again
     * */
    override fun onRefresh() {
        mAdapter.clear()
        viewModel.updateNews(currentCategory, page, pageSize)
    }

    /*
     * move intent to detail and send data news choose that
     * */
    override fun onClickNews(news: News) {
        val intent = Intent(context, DetailNewsActivity::class.java)
        intent.putExtra(DetailNewsActivity.EXTRA_NEWS, news)

        startActivity(intent)
    }

    override fun onClickShare(url: String) {
        TODO("Not yet implemented")
    }

    override fun onClickBookmark(news: News) {
        TODO("Not yet implemented")
    }

}