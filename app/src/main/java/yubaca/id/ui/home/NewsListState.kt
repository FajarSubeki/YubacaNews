package yubaca.id.ui.home

import yubaca.id.data.model.News


sealed class NewsListState {
    abstract val dataList: List<News>
}
data class DefaultState(override val dataList: List<News>): NewsListState()
data class LoadingState(override val dataList: List<News>): NewsListState()
data class EmptyState(override val dataList: List<News>): NewsListState()
data class ErrorState(val errorMessage: String, override val dataList: List<News>): NewsListState()