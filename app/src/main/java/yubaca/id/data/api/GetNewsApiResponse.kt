package yubaca.id.data.api

data class GetNewsApiResponse(
        val articles: List<NewsModel>,
        val status: String,
        val totalResults: Int
)