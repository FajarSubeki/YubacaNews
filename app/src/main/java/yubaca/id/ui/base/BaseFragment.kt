package yubaca.id.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection

abstract class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        initDi()
        super.onCreate(savedInstanceState)
    }

    private fun initDi() = AndroidSupportInjection.inject(this)

}