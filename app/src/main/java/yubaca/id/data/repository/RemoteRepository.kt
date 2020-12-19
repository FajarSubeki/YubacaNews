package yubaca.id.data.repository

import io.reactivex.Observable
import io.reactivex.Single
import yubaca.id.data.api.NewsModel
import yubaca.id.data.api.NewsService
import yubaca.id.data.model.News
import yubaca.id.util.Constant
import java.util.*
import javax.inject.Inject

class RemoteRepository @Inject constructor(private val newsService: NewsService) {

    fun getTopHeadlines(category: String, page: Int, pageSize: Int): Observable<News> {

        return newsService.getTopHeadlines(category = category, country = Constant.COUNTRY_ID, page = page, pageSize = pageSize,  apiKey = Constant.API_KEY)
            .toObservable()
            .flatMapIterable { it.articles }
            .map { articleModelToArticle(it) }
    }

    fun getEverything(query: String): Single<List<News>> {

        return newsService.getEverything(query = query, language = Constant.COUNTRY_ID, apiKey = Constant.API_KEY)
            .toObservable()
            .flatMapIterable { it.articles }
            .map { articleModelToArticle(it) }
            .toList()
    }

    private fun articleModelToArticle(model: NewsModel): News {

        return News(
            saveTime = Date(System.currentTimeMillis()),
            author = model.author,
            content = model.content,
            description = model.description,
            publishedAt = model.publishedAt,
            title = model.title,
            url = model.url,
            urlToImage = model.urlToImage
        )
    }
}