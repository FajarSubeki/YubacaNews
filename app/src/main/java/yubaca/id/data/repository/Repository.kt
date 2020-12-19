package yubaca.id.data.repository

import android.content.Context
import io.reactivex.Single
import yubaca.id.data.model.News
import yubaca.id.util.isConnAvailable
import javax.inject.Inject

class Repository @Inject constructor(private val context: Context,
                                     private val localRepository: LocalRepository,
                                     private val remoteRepository: RemoteRepository) {

    fun getTopHeadlines(category: String, page: Int, pageSize: Int): Single<List<News>> {

        return if (context.isConnAvailable()) {

            remoteRepository.getTopHeadlines(category, page, pageSize)
                .doOnNext { localRepository.saveToLocal(it) }
                .toList()

        } else {

            localRepository.getTopHeadlineFromLocal()
        }
    }

    fun getEverything(query: String): Single<List<News>> {

        return remoteRepository.getEverything(query)
    }

    fun getFavoriteIsExist(url: String) = localRepository.getFavoriteIsExist(url)
}