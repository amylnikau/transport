package by.mylnikov.transport.repository.sqlite

import android.database.sqlite.SQLiteOpenHelper
import by.mylnikov.transport.model.Favorite
import by.mylnikov.transport.model.ScheduleID
import by.mylnikov.transport.repository.FavoritesRepository
import rx.Observable
import rx.lang.kotlin.observable

class SQLiteFavoritesRepository(private val mDBHelper: SQLiteOpenHelper) : FavoritesRepository {

    companion object{
        const val SQL_GET_FAVORITES = "SELECT * FROM ${DBContract.ScheduleTable.TABLE_NAME} WHERE ${DBContract.ScheduleTable.IS_FAVORITE} = '1' AND ${DBContract.ScheduleTable.DATE} = 'all-days'"
    }

    override fun getFavorites(): Observable<Favorite> {
        return observable {
            val db = mDBHelper.readableDatabase
            val cursor = db.rawQuery(SQL_GET_FAVORITES, null)
            while (cursor.moveToNext()) {
                val from = cursor.getString(cursor.getColumnIndex(DBContract.ScheduleTable.FROM_ID))
                val to = cursor.getString(cursor.getColumnIndex(DBContract.ScheduleTable.TO_ID))
                val date = cursor.getString(cursor.getColumnIndex(DBContract.ScheduleTable.DATE))
                val departureName = cursor.getString(cursor.getColumnIndex(DBContract.ScheduleTable.FROM_NAME))
                val destinationName = cursor.getString(cursor.getColumnIndex(DBContract.ScheduleTable.TO_NAME))
                val id = ScheduleID(from, to, date)
                it.onNext(Favorite(id, departureName, destinationName))
            }
            cursor.close()
            db.close()
            it.onCompleted()
        }
    }
}