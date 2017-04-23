package by.mylnikov.transport.repository.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DBHelper(mContext: Context) :
        SQLiteOpenHelper(mContext, DBContract.DATABASE_NAME, null, DBContract.DATABASE_VERSION) {

    private val TAG = "DBHelper.kt"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DBContract.ScheduleTable.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DBContract.ScheduleTable.DROP_TABLE)
        onCreate(db)
    }
}
