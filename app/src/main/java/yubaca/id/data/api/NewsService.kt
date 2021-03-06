package yubaca.id.data.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("top-headlines")
    fun getTopHeadlines(@Query("country") country: String,
                        @Query("category") category: String,
                        @Query("page") page: Int,
                        @Query("pageSize") pageSize : Int,
                        @Query("apiKey") apiKey: String): Single<GetNewsApiResponse>

    @GET("everything")
    fun getEverything(@Query("q") query: String,
                      @Query("language") language: String,
                      @Query("apiKey") apiKey: String): Single<GetNewsApiResponse>
}