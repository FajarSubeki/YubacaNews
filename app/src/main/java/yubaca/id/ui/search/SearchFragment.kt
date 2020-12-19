package yubaca.id.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.ajalt.timberkt.d
import kotlinx.android.synthetic.main.fragment_search.*
import yubaca.id.R
import yubaca.id.data.model.News
import yubaca.id.ui.base.BaseFragment
import yubaca.id.ui.detail.DetailNewsActivity
import yubaca.id.ui.search.adapter.SearchAdapter
import yubaca.id.util.obtainViewModel
import yubaca.id.util.toGone
import yubaca.id.util.toVisible
import javax.inject.Inject

class SearchFragment : BaseFragment(), SearchAdapter.OnNewsArticleOnClickListener{

    // init viewmodel
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: SearchViewModel

    private val mAdapter = SearchAdapter(this)

    /*
     * set state for no input in searhview
     * */
    private val onChangeStateObserver = Observer<SearchChangeState> { changeState ->

        when (changeState) {

            is NoInputState -> {

                searchLayout.toGone()
                searchLayoutEmpty.toGone()
                searchLayoutError.toGone()
            }
        }
    }

    /*
     * Condition for state process in search news
     * */
    private val onSubmitStateObserver = Observer<SearchSubmitState> { onSubmitState ->

        when (onSubmitState) {

            is GetDataState -> {

                d { "Get Data State" }
                defaultLayoutState()
                mAdapter.add(onSubmitState.dataList)
                progressBar.toGone()
            }

            is LoadingState -> {

                d { "Loading State" }
                defaultLayoutState()
                progressBar.toVisible()
                mAdapter.clear()
            }

            is EmptyState -> {

                d { "No Item State" }
                emptyLayoutState()
            }

            is ErrorState -> {

                d { "Error State" }
                errorLayoutState()
            }
        }
    }

    companion object {

        val TAG = SearchFragment::class.java.simpleName

        fun newInstance() = SearchFragment()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.
        fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel().apply {
            changeStateLiveData.observe(viewLifecycleOwner, onChangeStateObserver)
            submitStateLiveData.observe(viewLifecycleOwner, onSubmitStateObserver)
        }

    }

    private fun initView() {

        rvSearch.apply {

            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            adapter = mAdapter
        }

        setupSearchView()
    }

    /*
     * Setup search for doing query and call view model function
     * */
    private fun setupSearchView() {

        val closeButton = searchNews.findViewById<View>(androidx.appcompat.R.id.search_close_btn)

        searchNews.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {

                rvSearch.smoothScrollToPosition(0)
                viewModel.getSearchListOnSubmit(query!!)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                d { "onChange: $newText" }
                return false
            }
        })

        closeButton.setOnClickListener {

            searchNews.setQuery("", false)
        }

    }

    private fun defaultLayoutState() {
        searchLayout.toVisible()
        searchLayoutEmpty.toGone()
        searchLayoutError.toGone()
    }

    private fun emptyLayoutState() {
        searchLayout.toGone()
        searchLayoutEmpty.toVisible()
        searchLayoutError.toGone()
    }

    private fun errorLayoutState() {
        searchLayout.toGone()
        searchLayoutEmpty.toGone()
        searchLayoutError.toVisible()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.changeStateLiveData.removeObserver(onChangeStateObserver)
        viewModel.submitStateLiveData.removeObserver(onSubmitStateObserver)
    }

    /*
     * event click to detail screen
     * */
    override fun onClickNews(news: News) {

        val intent = Intent(context, DetailNewsActivity::class.java).apply {
            putExtra(DetailNewsActivity.EXTRA_NEWS, news)
        }
        startActivity(intent)

    }

    override fun onClickShare(url: String) {
        TODO("Not yet implemented")
    }

    override fun onClickBookmark(news: News) {
        TODO("Not yet implemented")
    }

    private fun obtainViewModel() = obtainViewModel(viewModelFactory, SearchViewModel::class.java)


}