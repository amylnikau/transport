package by.mylnikov.transport.di.component

import by.mylnikov.transport.di.module.MainFragmentModule
import by.mylnikov.transport.di.scope.ActivityScope
import by.mylnikov.transport.view.fragment.MainFragment
import dagger.Subcomponent


@ActivityScope
@Subcomponent(modules = arrayOf(MainFragmentModule::class))
interface MainFragmentComponent {
    fun inject(mainFragment: MainFragment)

}
