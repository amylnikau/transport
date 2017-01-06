package by.mylnikov.transport.model

import java.util.*

class Schedule(var id: ScheduleID = ScheduleID(),
               var departureName: String = "",
               var destinationName: String = "",
               var isFavorite: Boolean = false,
               var records: ArrayList<RouteRecord> = ArrayList(),
               var updateTime: String ="")