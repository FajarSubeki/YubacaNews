package yubaca.id.di.builder

import dagger.Module
import dagger.android.ContributesAndroidInjector
import yubaca.id.MainActivity
import yubaca.id.ui.detail.DetailNewsActivity
import yubaca.id.ui.splash.SplashActivity

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector
    abstract fun contributeSplashScreen(): SplashActivity

    @ContributesAndroidInjector(modules = [FragmentBuilder::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeDetailActivity(): DetailNewsActivity
}