package by.mylnikov.transport.di.component

import by.mylnikov.transport.di.module.FavoritesFragmentModule
import by.mylnikov.transport.di.scope.ActivityScope
import by.mylnikov.transport.view.fragment.FavoritesFragment
import dagger.Subcomponent


@ActivityScope
@Subcomponent(modules = arrayOf(FavoritesFragmentModule::class))
interface FavoritesFragmentComponent {
    fun inject(favoritesFragment: FavoritesFragment)

}