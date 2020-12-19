package yubaca.id.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jaeger.library.StatusBarUtil
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_detail_news.*

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        initDI()
        super.onCreate(savedInstanceState)
    }

    private fun initDI() = AndroidInjection.inject(this);

    fun setTransparentStatusBar(){
        StatusBarUtil.setTranslucentForImageView(this, 0, root_view)
    }

}