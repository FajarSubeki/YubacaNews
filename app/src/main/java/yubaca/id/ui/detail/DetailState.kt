package yubaca.id.ui.detail

import yubaca.id.data.model.News


sealed class DetailState {

    abstract val data: News
}

data class DefaultState(override val data: News): DetailState()
data class ChangeIconState(override val data: News, val isFavorite: Boolean): DetailState()
data class ErrorState(override val data: News, val errorMessage: String): DetailState()