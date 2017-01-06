package by.mylnikov.transport.repository.sqlite

import android.provider.BaseColumns


object DBContract {

    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "schedule.db"


    object ScheduleTable : BaseColumns {
        const val TABLE_NAME = "schedule"
        const val FROM_ID = "fromId"
        const val TO_ID = "toId"
        const val FROM_NAME = "fromName"
        const val TO_NAME = "toName"
        const val DATE = "date"
        const val SCHEDULE = "schedule"
        const val IS_FAVORITE = "isFavorite"
        const val UPDATED_TIME = "updatedTime"

        const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME($FROM_ID TEXT, $TO_ID TEXT, $FROM_NAME TEXT, $TO_NAME TEXT, $DATE TEXT, $IS_FAVORITE INTEGER DEFAULT 0, $SCHEDULE TEXT, $UPDATED_TIME TEXT)"
        const val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

}
