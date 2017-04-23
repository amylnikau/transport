package by.mylnikov.transport.util

import by.mylnikov.transport.model.RouteRecord
import by.mylnikov.transport.model.Schedule
import by.mylnikov.transport.model.ScheduleID
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*


class ScheduleDeserializer : JsonDeserializer<Schedule> {

    companion object {
        val updateTimeFormat = SimpleDateFormat("HH:mm dd-MM", Locale.getDefault())
        val receivedFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())
        val requiredTimeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val requiredDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    }

    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Schedule {
        val search = json.asJsonObject.getAsJsonArray("data")[0].asJsonObject.getAsJsonObject("data").getAsJsonObject("search")
        val routeRecords = ArrayList<RouteRecord>()
        receivedFormat.timeZone = TimeZone.getTimeZone("UTC")
        for (s in search.getAsJsonArray("segments")) {
            val jsonObject = s.asJsonObject
            val title = jsonObject.getAsJsonPrimitive("title").asString
            val number = jsonObject.getAsJsonPrimitive("number").asString
            val arrivalDate = receivedFormat.parse(jsonObject.getAsJsonPrimitive("arrival").asString)
            val departureDate = receivedFormat.parse(jsonObject.getAsJsonPrimitive("departure").asString)
            val duration = jsonObject.getAsJsonPrimitive("duration").asInt
            val regularityObject = jsonObject.getAsJsonObject("daysByTimezone")
            val regularity: String
            if (regularityObject != null)
                regularity = regularityObject.getAsJsonObject("Europe/Minsk").getAsJsonPrimitive("text").asString
            else
                regularity = ""
            val timeOnWay: String
            if (duration / 60 > 60) {
                val hours = duration / 3600
                timeOnWay = "$hours ч ${hours * 60 - duration / 60} мин"
            } else {
                timeOnWay = "${duration / 60} мин"
            }
            routeRecords.add(RouteRecord(title, number, requiredTimeFormat.format(departureDate),
                    requiredTimeFormat.format(arrivalDate), regularity, timeOnWay))
        }
        val scheduleDate: String
        if (search.getAsJsonObject("context").get("latestDatetime").isJsonPrimitive) {
            val date = receivedFormat.parse(search.getAsJsonObject("context").getAsJsonPrimitive("latestDatetime").asString)
            val cal = Calendar.getInstance()
            cal.time = date
            cal.add(Calendar.DATE, -1)
            scheduleDate = requiredDateFormat.format(cal.time)
        } else
            scheduleDate = "all-days"
        val pointFrom = search.getAsJsonObject("context").getAsJsonObject("search").getAsJsonObject("pointFrom")
        val pointTo = search.getAsJsonObject("context").getAsJsonObject("search").getAsJsonObject("pointTo")
        val scheduleId = ScheduleID(pointFrom.getAsJsonPrimitive("key").asString, pointTo.getAsJsonPrimitive("key").asString, scheduleDate)
        val updatedTime = updateTimeFormat.format(Calendar.getInstance().time)
        return Schedule(scheduleId, pointFrom.getAsJsonPrimitive("title").asString,
                pointTo.getAsJsonPrimitive("title").asString, false, updatedTime, routeRecords)
    }
}
