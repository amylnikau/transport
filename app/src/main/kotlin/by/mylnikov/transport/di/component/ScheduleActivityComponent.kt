package by.mylnikov.transport.di.component

import by.mylnikov.transport.di.module.ScheduleActivityModule
import by.mylnikov.transport.di.scope.ActivityScope
import by.mylnikov.transport.view.activity.ScheduleActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(ScheduleActivityModule::class))
interface ScheduleActivityComponent {
    fun inject(scheduleActivity: ScheduleActivity)

}
