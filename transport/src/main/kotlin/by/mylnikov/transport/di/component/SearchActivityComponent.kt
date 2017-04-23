package by.mylnikov.transport.di.component

import by.mylnikov.transport.di.module.SearchActivityModule
import by.mylnikov.transport.di.scope.ActivityScope
import by.mylnikov.transport.view.activity.SearchActivity
import dagger.Subcomponent


@ActivityScope
@Subcomponent(modules = arrayOf(SearchActivityModule::class))
interface SearchActivityComponent {
    fun inject(searchActivity: SearchActivity)
}