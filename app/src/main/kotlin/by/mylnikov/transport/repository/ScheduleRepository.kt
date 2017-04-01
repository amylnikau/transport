package by.mylnikov.transport.repository

import by.mylnikov.transport.model.Schedule
import by.mylnikov.transport.model.ScheduleID
import io.reactivex.Observable

interface ScheduleRepository {
    fun addSchedule(schedule: Schedule)
    fun updateSchedule(schedule: Schedule)
    fun getSchedule(scheduleId: ScheduleID): Observable<Schedule>
    fun modify(specification: ScheduleSpecification)
}
