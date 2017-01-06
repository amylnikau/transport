package by.mylnikov.transport.repository.sqlite

import by.mylnikov.transport.model.Schedule
import by.mylnikov.transport.model.ScheduleID
import by.mylnikov.transport.repository.ScheduleSpecification


class ChangeFavoriteSpecification(private val scheduleId: ScheduleID,
                                  isFavorite: Boolean) : ScheduleSpecification, SqlSpecification {
    private val isFavorite = if (isFavorite) 1 else 0

    override fun specified(schedule: Schedule): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun toSqlClauses(): String {
        return "UPDATE ${DBContract.ScheduleTable.TABLE_NAME} SET ${DBContract.ScheduleTable.IS_FAVORITE} = '$isFavorite' WHERE ${DBContract.ScheduleTable.FROM_ID} = '${scheduleId.from}' AND ${DBContract.ScheduleTable.TO_ID} = '${scheduleId.to}'"
    }

}