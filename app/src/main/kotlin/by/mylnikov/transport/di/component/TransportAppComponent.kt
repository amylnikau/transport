package by.mylnikov.transport.di.component

import by.mylnikov.transport.di.module.*
import by.mylnikov.transport.repository.ScheduleRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(TransportModule::class))
interface TransportAppComponent {
    fun plus(mainModule: MainFragmentModule): MainFragmentComponent
    fun plus(scheduleModule: ScheduleActivityModule): ScheduleActivityComponent
    fun plus(searchModule: SearchActivityModule): SearchActivityComponent
    fun plus(favoritesModule: FavoritesFragmentModule): FavoritesFragmentComponent
    fun scheduleRepository(): ScheduleRepository
}
