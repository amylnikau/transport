package by.mylnikov.transport.di.module

import by.mylnikov.transport.di.scope.ActivityScope
import by.mylnikov.transport.view.fragment.MainFragment
import by.mylnikov.transport.viewModel.MainViewModel
import dagger.Module
import dagger.Provides


@Module
class MainFragmentModule(private val mainFragment: MainFragment) {

    @Provides
    @ActivityScope
    fun provideMainViewModel(): MainViewModel {
        return MainViewModel(mainFragment)
    }
}