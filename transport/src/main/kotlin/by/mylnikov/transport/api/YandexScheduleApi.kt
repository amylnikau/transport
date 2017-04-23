package by.mylnikov.transport.api

import by.mylnikov.transport.api.model.SuggestionsResponse
import by.mylnikov.transport.model.Schedule
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*


interface YandexScheduleApi {
    companion object {
        const val ENDPOINT = "https://rasp.yandex.by/"
    }

    @GET("https://suggests.rasp.yandex.net/all_suggests")
    fun getSuggestions(@Query("client_city") city: String,
                       @Query("part") part: String): Observable<SuggestionsResponse>

    @Headers("Content-type: application/json")
    @POST("api/batch")
    fun getSchedule(@Body body: RequestBody): Observable<Schedule>
}