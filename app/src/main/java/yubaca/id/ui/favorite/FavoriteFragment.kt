package yubaca.id.ui.favorite

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
import kotlinx.android.synthetic.main.fragment_favorite.*
import yubaca.id.MainActivity
import yubaca.id.R
import yubaca.id.data.model.Favorite
import yubaca.id.data.model.News
import yubaca.id.ui.base.BaseFragment
import yubaca.id.ui.detail.DetailNewsActivity
import yubaca.id.ui.favorite.adapter.FavoriteAdapter
import yubaca.id.ui.search.SearchFragment
import yubaca.id.util.obtainViewModel
import yubaca.id.util.toGone
import yubaca.id.util.toVisible
import javax.inject.Inject

class FavoriteFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener, FavoriteAdapter.OnFavoriteClickListener {

    // create intance for call in another class
    companion object {

        fun newInstance() = FavoriteFragment()
    }

    // init inject viewmodel
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: FavoriteViewModel

    // init adapter
    private val mAdapter = FavoriteAdapter(this)

    /*
     * state for flow process receving data favorite
     * */
    private val favoriteListStateObserver = Observer<FavoriteListState> { state ->

        when (state) {

            is DefaultState -> {

                d { "Default State" }
                defaultLayoutState()
                favoriteLayout.isRefreshing = false
                mAdapter.clear()
                mAdapter.add(state.dataList)
            }

            is LoadingState -> {

                defaultLayoutState()
                favoriteLayout.isRefreshing = true
            }

            is EmptyState -> {

                emptyLayoutState()
                mAdapter.clear()
            }

            is ErrorState -> {

                errorLayoutState()
                mAdapter.clear()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel().apply {

            favoriteListStateLiveData.observe(viewLifecycleOwner, favoriteListStateObserver)
        }
        viewModel.updateFavorite()
    }

    // init view component favorite screen
    private fun initView() {

        favoriteLayout.setOnRefreshListener(this)
        btnRefresh.setOnClickListener { onRefresh() }

        ivSearch.setOnClickListener { moveFragment() }

        rvFavorite.apply {

            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
    }

    /*
     * method for apply fragment favorite call in another fragment
     * */
    private fun moveFragment() {
        val frag= SearchFragment.newInstance()
        (activity as MainActivity).replaceFragment(frag, SearchFragment.TAG)
    }

    /*
     * Clear dan remove viewdata model list
     * */
    override fun onDestroy() {
        super.onDestroy()
        viewModel.favoriteListStateLiveData.removeObserver(favoriteListStateObserver)
    }

    override fun onRefresh() {

        mAdapter.clear()
        viewModel.updateFavorite()
    }

    override fun onClickItem(favorite: Favorite) {

        val currentNews = News(
                saveTime = favorite.saveTime,
                author = favorite.author,
                content = favorite.content,
                description = favorite.description,
                publishedAt = favorite.publishedAt,
                title = favorite.title,
                url = favorite.url,
                urlToImage = favorite.urlToImage
        )

        val intent = Intent(context, DetailNewsActivity::class.java).apply {

            putExtra(DetailNewsActivity.EXTRA_NEWS, currentNews)
        }
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()

        viewModel.updateFavorite()
    }

    private fun defaultLayoutState() {

        favoriteLayout.toVisible()
        favoriteLayoutEmpty.toGone()
        favoriteLayoutError.toGone()
    }

    private fun emptyLayoutState() {

        favoriteLayout.toGone()
        favoriteLayoutEmpty.toVisible()
        favoriteLayoutError.toGone()
    }

    private fun errorLayoutState() {

        favoriteLayout.toGone()
        favoriteLayoutEmpty.toGone()
        favoriteLayoutError.toVisible()
    }

    private fun obtainViewModel() = obtainViewModel(viewModelFactory, FavoriteViewModel::class.java)

}