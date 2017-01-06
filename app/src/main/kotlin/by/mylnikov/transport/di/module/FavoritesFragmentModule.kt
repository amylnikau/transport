package by.mylnikov.transport.di.module

import android.database.sqlite.SQLiteOpenHelper
import by.mylnikov.transport.di.scope.ActivityScope
import by.mylnikov.transport.repository.FavoritesRepository
import by.mylnikov.transport.repository.sqlite.SQLiteFavoritesRepository
import by.mylnikov.transport.view.fragment.FavoritesFragment
import by.mylnikov.transport.viewModel.FavoritesViewModel
import dagger.Module
import dagger.Provides


@Module
class FavoritesFragmentModule(private val favoritesFragment: FavoritesFragment) {

    @Provides
    @ActivityScope
    fun provideFavoritesRepository(dbHelper: SQLiteOpenHelper): FavoritesRepository{
        return SQLiteFavoritesRepository(dbHelper)
    }

    @Provides
    @ActivityScope
    fun provideFavoritesViewModel(favoritesRepository: FavoritesRepository): FavoritesViewModel {
        return FavoritesViewModel(favoritesFragment, favoritesRepository)
    }
}