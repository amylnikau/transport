package by.mylnikov.transport.model

import java.util.*

class Schedule(var id: ScheduleID = ScheduleID(),
               var departureName: String = "",
               var destinationName: String = "",
               var isFavorite: Boolean = false,
               var updatedTime: String = "",
               private var _records: List<RouteRecord> = ArrayList()) {

    var records
        get() = _records
        set(value) {
            Collections.sort(value)
            _records = value
        }

    init {
        Collections.sort(_records)
    }

    fun isNotEmpty(): Boolean = records.isNotEmpty()
}