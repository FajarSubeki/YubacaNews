package yubaca.id.di.module

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import yubaca.id.data.api.NewsService
import yubaca.id.util.Constant
import javax.inject.Singleton
import kotlin.math.log

@Module
class NetworkModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient{

        val logging = HttpLoggingInterceptor().apply {

            level = HttpLoggingInterceptor.Level.BODY

        }

        return OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient) =

            Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build()

    @Provides
    fun provideNewsServices(retrofit: Retrofit) = retrofit.create(NewsService::class.java)

}