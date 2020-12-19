package yubaca.id.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import yubaca.id.data.model.News

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNews(news: News)

    @Query("SELECT * FROM news ORDER BY saveTime DESC")
    fun getTopHeadlines(): Single<List<News>>

}