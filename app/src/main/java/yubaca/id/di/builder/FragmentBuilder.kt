package yubaca.id.di.builder

import dagger.Module
import dagger.android.ContributesAndroidInjector
import yubaca.id.ui.favorite.FavoriteFragment
import yubaca.id.ui.home.HomeFragment
import yubaca.id.ui.search.SearchFragment

@Module
abstract class FragmentBuilder {

    @ContributesAndroidInjector
    abstract fun contributeNewsFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeFavoriteFragment(): FavoriteFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): SearchFragment
}