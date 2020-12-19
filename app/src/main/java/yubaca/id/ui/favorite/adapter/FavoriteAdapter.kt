package yubaca.id.ui.favorite.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_favorite.view.*
import yubaca.id.R
import yubaca.id.data.model.Favorite
import yubaca.id.di.module.GlideApp
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class FavoriteAdapter(private val listener: OnFavoriteClickListener) : RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder>() {

    private var favoriteList: List<Favorite> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {

        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_favorite, parent, false)
        return FavoriteHolder(view)
    }

    override fun getItemCount(): Int = favoriteList.size

    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) = holder.bind(favoriteList[position])

    fun add(dataList: List<Favorite>) {

        favoriteList = dataList
        notifyDataSetChanged()
    }

    fun clear() {

        favoriteList = emptyList()
    }

    inner class FavoriteHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(favorite: Favorite) {

            itemView.apply {

                val getDate: String? = favorite.publishedAt

                text_title.text = favorite.title
                text_date.text = getDate?.let { convertISOTimeToDate(it) }
                GlideApp.with(this).load(favorite.urlToImage).into(img_favorite)


            }.setOnClickListener { listener.onClickItem(favorite) }
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun convertISOTimeToDate(isoTime: String): String? {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SSS")
        var convertedDate: Date? = null
        var formattedDate: String? = null
        try {
            convertedDate = sdf.parse(isoTime)
            formattedDate = SimpleDateFormat("dd MMMM yyyy").format(convertedDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return formattedDate
    }

    interface OnFavoriteClickListener {

        fun onClickItem(favorite: Favorite)

    }
}