package yubaca.id.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.ajalt.timberkt.e
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import yubaca.id.data.model.News
import yubaca.id.data.repository.Repository
import yubaca.id.util.plusAssign
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NewsViewModel @Inject constructor(private val repository: Repository) : ViewModel()  {

    val newsListStateLiveData = MutableLiveData<NewsListState>()

    private val compositeDisposable = CompositeDisposable()

    fun updateNews(category: String, page: Int, pageSize: Int) {

        getNewsList(category, page, pageSize)
    }

    private fun getNewsList(category: String, page: Int, pageSize: Int) {

        compositeDisposable += repository.getTopHeadlines(category, page, pageSize)
            .toObservable()
            .debounce(400, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onLoading() }
            .subscribe(this::onReceivedList, this::onError)
    }

    private fun onReceivedList(dataList: List<News>) {

        if (dataList.isNotEmpty()) {

            newsListStateLiveData.value = DefaultState(dataList)

        } else {

            newsListStateLiveData.value = EmptyState(emptyList())
        }
    }

    private fun onLoading() {

        newsListStateLiveData.value = LoadingState(emptyList())
    }

    private fun onError(throwable: Throwable) {

        newsListStateLiveData.value = ErrorState(throwable.message.toString(), emptyList())
        e { "${throwable.message}" }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }

}