package by.mylnikov.transport.di.module

import by.mylnikov.transport.api.YandexScheduleApi
import by.mylnikov.transport.di.scope.ActivityScope
import by.mylnikov.transport.repository.ScheduleRepository
import by.mylnikov.transport.view.activity.ScheduleActivity
import by.mylnikov.transport.viewModel.ScheduleViewModel
import dagger.Module
import dagger.Provides


@Module
class ScheduleActivityModule(private val scheduleActivity: ScheduleActivity) {

    @Provides
    @ActivityScope
    fun provideScheduleViewModel(scheduleRepository: ScheduleRepository,
                                 yandexApi: YandexScheduleApi): ScheduleViewModel {
        return ScheduleViewModel(scheduleActivity, scheduleRepository, yandexApi)
    }
}
