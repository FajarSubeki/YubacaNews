package yubaca.id.ui.search

import yubaca.id.data.model.News

sealed class SearchSubmitState

data class GetDataState(val dataList: List<News>): SearchSubmitState()
data class ErrorState(val errorMessage: String) : SearchSubmitState()
object LoadingState : SearchSubmitState()
object EmptyState : SearchSubmitState()