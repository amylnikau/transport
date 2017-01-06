package by.mylnikov.transport.di.module

import by.mylnikov.transport.api.YandexScheduleApi
import by.mylnikov.transport.di.scope.ActivityScope
import by.mylnikov.transport.view.activity.SearchActivity
import by.mylnikov.transport.viewModel.SearchViewModel
import dagger.Module
import dagger.Provides


@Module
class SearchActivityModule(private val searchActivity: SearchActivity) {
    @ActivityScope
    @Provides
    fun provideSearchViewModel(yandexApi: YandexScheduleApi): SearchViewModel {
        return SearchViewModel(searchActivity, yandexApi)
    }
}
