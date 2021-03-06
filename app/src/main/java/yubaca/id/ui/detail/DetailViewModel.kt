package yubaca.id.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.ajalt.timberkt.d
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import yubaca.id.data.model.Favorite
import yubaca.id.data.model.News
import yubaca.id.data.repository.LocalRepository
import yubaca.id.util.plusAssign
import java.util.*
import javax.inject.Inject

class DetailViewModel @Inject constructor(private val localRepository: LocalRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private lateinit var currentNews: News
    val detailStateLiveData = MutableLiveData<DetailState>()

    fun receivedArticle(news: News?) {
        currentNews = news!!
        detailStateLiveData.value = DefaultState(currentNews)
    }

    fun addFavorite() {

        val currentFavorite = currentNews.let { Favorite (

                saveTime = Date(System.currentTimeMillis()),
                author = it.author,
                content = it.content,
                description = it.description,
                publishedAt = it.publishedAt,
                title = it.title,
                url = it.url,
                urlToImage = it.urlToImage
        ) }
        compositeDisposable += Observable.fromCallable {

            localRepository.addFavorite(currentFavorite)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                detailStateLiveData.value = ChangeIconState(currentNews, true)
                d { "Save Bookmark" }

            }, this::onError)
    }

    fun deleteFavorite() {

        compositeDisposable += Observable.fromCallable {

            localRepository.deleteFavorite(currentNews.url)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                detailStateLiveData.value = ChangeIconState(currentNews, false)
                d { "Delete Bookmark" }

            }, this::onError)
    }

    fun checkFavorite(url: String) {

        compositeDisposable += localRepository.getFavoriteIsExist(url)
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onReceivedFavoriteList, this::onError)

    }

    private fun onReceivedFavoriteList(bookmark: List<News>) {

        if (bookmark.isNotEmpty()) {

            d { "Favorite Exist" }
            detailStateLiveData.value = ChangeIconState(currentNews, true)

        } else {

            detailStateLiveData.value = ChangeIconState(currentNews, false)
            d { "Favorite Not Exist" }
        }
    }

    private fun onError(throwable: Throwable) {

        d { "${throwable.message}" }
        detailStateLiveData.value = ErrorState(currentNews, throwable.message.toString())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }
}