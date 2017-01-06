package by.mylnikov.transport.repository

import by.mylnikov.transport.model.Schedule


interface ScheduleSpecification {
    fun specified(schedule: Schedule): Boolean
}