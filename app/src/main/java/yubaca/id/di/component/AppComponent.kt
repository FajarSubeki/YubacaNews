package yubaca.id.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import yubaca.id.AppDelegate
import yubaca.id.di.builder.ActivityBuilder
import yubaca.id.di.module.AppModule
import yubaca.id.di.module.NetworkModule
import yubaca.id.di.module.RepositoryModule
import yubaca.id.di.module.ViewModelModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    NetworkModule::class,
    RepositoryModule::class,
    ViewModelModule::class,
    ActivityBuilder::class
])
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(appDelegate: AppDelegate)

}