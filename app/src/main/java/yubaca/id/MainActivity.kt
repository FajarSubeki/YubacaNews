package yubaca.id

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import yubaca.id.ui.base.BaseActivity
import yubaca.id.ui.favorite.FavoriteFragment
import yubaca.id.ui.home.HomeFragment
import yubaca.id.ui.search.SearchFragment
import yubaca.id.util.Constant
import javax.inject.Inject

class MainActivity : BaseActivity(), HasSupportFragmentInjector {

    // init inject for fragment
    @Inject
    lateinit var dispatchingAndroidInjector : DispatchingAndroidInjector<Fragment>

    private var currentCategory = Constant.ALL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTransparentStatusBar()

        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_home, HomeFragment.newInstance(currentCategory))
                    .commit()
        }

        initView()

    }

    fun replaceFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.frame_home, fragment, tag).addToBackStack("").commit()
    }

    private fun initView(){

        nav_home.setOnNavigationItemSelectedListener(navListener)

    }

    private fun changeFragmentWithBot(fragment: Fragment) {

        supportFragmentManager.beginTransaction()
                .replace(R.id.frame_home, fragment)
                .commit()

    }


    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener {

        when (it.itemId) {

            R.id.nav_home -> {

                changeFragmentWithBot(HomeFragment.newInstance(currentCategory))
                return@OnNavigationItemSelectedListener true
            }

            R.id.nav_search -> {

                changeFragmentWithBot(SearchFragment.newInstance())
                return@OnNavigationItemSelectedListener true
            }

            R.id.nav_favorite -> {

                changeFragmentWithBot(FavoriteFragment.newInstance())
                return@OnNavigationItemSelectedListener true

            }
        }

        return@OnNavigationItemSelectedListener false
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector

}