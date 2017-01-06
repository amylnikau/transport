package by.mylnikov.transport.repository.sqlite

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import by.mylnikov.transport.model.RouteRecord
import by.mylnikov.transport.model.Schedule
import by.mylnikov.transport.model.ScheduleID
import by.mylnikov.transport.repository.ScheduleRepository
import by.mylnikov.transport.repository.ScheduleSpecification
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import rx.Observable
import rx.lang.kotlin.observable
import java.util.*

class SQLiteScheduleRepository(private val mDBHelper: SQLiteOpenHelper) : ScheduleRepository {

    companion object {
        const val SQL_GET_SCHEDULE_BY_ID = "SELECT * FROM ${DBContract.ScheduleTable.TABLE_NAME} WHERE ${DBContract.ScheduleTable.FROM_ID} = '%s' AND ${DBContract.ScheduleTable.TO_ID} = '%s' AND ${DBContract.ScheduleTable.DATE} = '%s'"
        const val SQL_GET_FAVORITE_BY_ID = "SELECT ${DBContract.ScheduleTable.IS_FAVORITE} FROM ${DBContract.ScheduleTable.TABLE_NAME} WHERE ${DBContract.ScheduleTable.FROM_ID} = '%s' AND ${DBContract.ScheduleTable.TO_ID} = '%s'"
    }

    override fun modify(specification: ScheduleSpecification) {
        specification as SqlSpecification
        val db = mDBHelper.writableDatabase
        db.execSQL(specification.toSqlClauses())
        db.close()
    }

    override fun addSchedule(schedule: Schedule) {
        val db = mDBHelper.writableDatabase
        val values = ContentValues()
        val id = schedule.id
        val cursor = db.rawQuery(SQL_GET_FAVORITE_BY_ID.format(id.from, id.to), null)
        if (cursor.moveToFirst() && cursor.getInt(cursor.getColumnIndex(DBContract.ScheduleTable.IS_FAVORITE)) == 1) {
            schedule.isFavorite = true
        }
        cursor.close()
        values.put(DBContract.ScheduleTable.FROM_ID, id.from)
        values.put(DBContract.ScheduleTable.TO_ID, id.to)
        values.put(DBContract.ScheduleTable.DATE, id.date)
        values.put(DBContract.ScheduleTable.FROM_NAME, schedule.departureName)
        values.put(DBContract.ScheduleTable.TO_NAME, schedule.destinationName)
        values.put(DBContract.ScheduleTable.IS_FAVORITE, schedule.isFavorite)
        values.put(DBContract.ScheduleTable.UPDATED_TIME, schedule.updateTime)
        values.put(DBContract.ScheduleTable.SCHEDULE, Gson().toJson(schedule.records).toByteArray())
        db.insert(DBContract.ScheduleTable.TABLE_NAME, null, values)
        db.close()
    }

    override fun updateSchedule(schedule: Schedule) {
        val db = mDBHelper.writableDatabase
        val values = ContentValues()
        val id = schedule.id
        val whereClause = "${DBContract.ScheduleTable.FROM_ID}=? AND ${DBContract.ScheduleTable.TO_ID}=? AND ${DBContract.ScheduleTable.DATE}=?"
        val whereArgs = arrayOf(id.from, id.to, id.date)
        values.put(DBContract.ScheduleTable.FROM_NAME, schedule.departureName)
        values.put(DBContract.ScheduleTable.TO_NAME, schedule.destinationName)
        values.put(DBContract.ScheduleTable.IS_FAVORITE, schedule.isFavorite)
        values.put(DBContract.ScheduleTable.UPDATED_TIME, schedule.updateTime)
        values.put(DBContract.ScheduleTable.SCHEDULE, Gson().toJson(schedule.records).toByteArray())
        db.update(DBContract.ScheduleTable.TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    override fun getSchedule(scheduleId: ScheduleID): Observable<Schedule> {
        return observable {
            val db = mDBHelper.readableDatabase
            val cursor = db.rawQuery(SQL_GET_SCHEDULE_BY_ID.format(scheduleId.from, scheduleId.to, scheduleId.date), null)
            if (cursor.moveToFirst()) {
                val blob = cursor.getBlob(cursor.getColumnIndex(DBContract.ScheduleTable.SCHEDULE))
                val json = String(blob)
                val records: ArrayList<RouteRecord> = Gson().fromJson(json, object : TypeToken<ArrayList<RouteRecord>>() {}.type)
                val isFavorite = cursor.getInt(cursor.getColumnIndex(DBContract.ScheduleTable.IS_FAVORITE)) == 1
                val departureName = cursor.getString(cursor.getColumnIndex(DBContract.ScheduleTable.FROM_NAME))
                val destinationName = cursor.getString(cursor.getColumnIndex(DBContract.ScheduleTable.TO_NAME))
                val updatedTime = cursor.getString(cursor.getColumnIndex(DBContract.ScheduleTable.UPDATED_TIME))
                it.onNext(Schedule(scheduleId, departureName, destinationName, isFavorite, records, updatedTime))
            }
            cursor.close()
            db.close()
            it.onCompleted()
        }
    }
}
