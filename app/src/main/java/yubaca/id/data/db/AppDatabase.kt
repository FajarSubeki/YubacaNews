package yubaca.id.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import yubaca.id.data.model.Favorite
import yubaca.id.data.model.News
import yubaca.id.util.Constant

@Database(entities = [News::class, Favorite::class], version = Constant.DB_VER, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun articleDao(): NewsDao

    abstract fun favoriteDao(): FavoriteDao

}