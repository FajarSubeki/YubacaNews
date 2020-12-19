package yubaca.id.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import yubaca.id.data.model.News
import yubaca.id.data.repository.Repository
import yubaca.id.util.plusAssign
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val changeStateLiveData = MutableLiveData<SearchChangeState>()
    val submitStateLiveData = MutableLiveData<SearchSubmitState>()

    private val compositeDisposable = CompositeDisposable()

    init {

        changeStateLiveData.value = NoInputState
    }

    fun getSearchListOnSubmit(query: String) {

        compositeDisposable += repository.getEverything(query)
                .toObservable()
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onLoading() }
                .subscribe(this::onReceivedList, this::onError)
    }

    private fun onLoading() {

        submitStateLiveData.value = LoadingState
    }

    private fun onReceivedList(dataList: List<News>) {

        if (dataList.isNotEmpty()) {

            submitStateLiveData.value = GetDataState(dataList)

        } else {

            submitStateLiveData.value = EmptyState
        }
    }

    private fun onError(throwable: Throwable) {

        submitStateLiveData.value = ErrorState(throwable.message.toString())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }
}