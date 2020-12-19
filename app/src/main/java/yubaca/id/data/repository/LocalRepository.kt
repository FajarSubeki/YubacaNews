package yubaca.id.data.repository

import com.github.ajalt.timberkt.d
import yubaca.id.data.db.FavoriteDao
import yubaca.id.data.db.NewsDao
import yubaca.id.data.model.Favorite
import yubaca.id.data.model.News
import javax.inject.Inject

class LocalRepository @Inject constructor(private val newsDao: NewsDao,
                                          private val favoriteDao: FavoriteDao) {

    fun getTopHeadlineFromLocal() = newsDao.getTopHeadlines()

    fun saveToLocal(news: News?) {

        if (news != null) {

            d { "Insert News --> ${news.title}" }
            newsDao.insertNews(news)
        } else {

            d { "article is null" }
        }
    }

    fun getFavoriteList() = favoriteDao.getFavorites()

    fun addFavorite(favorite: Favorite) = favoriteDao.insertFavorite(favorite)

    fun deleteFavorite(url: String) = favoriteDao.deleteFavorite(url)

    fun getFavoriteIsExist(url: String) = favoriteDao.getFavoriteIsExist(url)
}