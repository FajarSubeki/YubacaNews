package yubaca.id.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import yubaca.id.data.api.NewsService
import yubaca.id.data.db.FavoriteDao
import yubaca.id.data.db.NewsDao
import yubaca.id.data.repository.LocalRepository
import yubaca.id.data.repository.RemoteRepository
import yubaca.id.data.repository.Repository
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    fun provideLocalRepository(newsDao: NewsDao, favoriteDao: FavoriteDao) = LocalRepository(newsDao, favoriteDao)

    @Provides
    fun provideRemoteRepository(newsService: NewsService) = RemoteRepository(newsService)

    @Provides
    @Singleton
    fun provideRepository(context: Context,
                          localRepository: LocalRepository,
                          remoteRepository: RemoteRepository): Repository {

        return Repository(context, localRepository, remoteRepository)
    }
}