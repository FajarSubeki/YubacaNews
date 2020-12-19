package yubaca.id.di.module

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import yubaca.id.data.db.AppDatabase
import yubaca.id.util.Constant
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideContext(application: Application) = application.applicationContext!!

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase{

        return Room.databaseBuilder(context, AppDatabase::class.java, Constant.DB_NAME).build()

    }

    @Provides
    fun provideArticleDao(appDatabase: AppDatabase) = appDatabase.articleDao()

    @Provides
    fun provideFavoriteDao(appDatabase: AppDatabase) = appDatabase.favoriteDao()

}