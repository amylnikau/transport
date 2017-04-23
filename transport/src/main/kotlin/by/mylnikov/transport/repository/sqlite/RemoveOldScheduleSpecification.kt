package by.mylnikov.transport.repository.sqlite

import by.mylnikov.transport.model.Schedule
import by.mylnikov.transport.repository.ScheduleSpecification


class RemoveOldScheduleSpecification(val days: Int) : ScheduleSpecification, SqlSpecification {
    override fun specified(schedule: Schedule): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun toSqlClauses(): String {
        return "DELETE FROM ${DBContract.ScheduleTable.TABLE_NAME} WHERE ${DBContract.ScheduleTable.DATE} < date('now', '-$days days', 'localtime')"
    }

}