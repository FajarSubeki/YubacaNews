package yubaca.id.data.model

import androidx.room.Entity
import androidx.room.Index
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import yubaca.id.util.Constant
import java.util.*

@Entity(tableName = Constant.TABLE_FAVORITE,
    primaryKeys = ["url"],
    indices = [(Index("url"))])
@Parcelize
data class Favorite(
    val saveTime: Date?,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val title: String?,
    val url: String,
    val urlToImage: String?
): Parcelable