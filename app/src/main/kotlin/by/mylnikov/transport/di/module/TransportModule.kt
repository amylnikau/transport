package by.mylnikov.transport.di.module


import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import by.mylnikov.transport.TransportApplication
import by.mylnikov.transport.api.YandexScheduleApi
import by.mylnikov.transport.model.Schedule
import by.mylnikov.transport.repository.ScheduleRepository
import by.mylnikov.transport.repository.sqlite.DBHelper
import by.mylnikov.transport.repository.sqlite.SQLiteScheduleRepository
import by.mylnikov.transport.util.AddCookiesInterceptor
import by.mylnikov.transport.util.ReceivedCookiesInterceptor
import by.mylnikov.transport.util.ScheduleDeserializer
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class TransportModule(private val application: TransportApplication) {

    @Provides
    @Singleton
    fun provideApplication(): TransportApplication {
        return application
    }

    @Provides
    @Singleton
    fun provideApplicationContext(): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(context: Context): OkHttpClient {
        val builder = OkHttpClient().newBuilder()
        builder.networkInterceptors().add(AddCookiesInterceptor(context))
        builder.networkInterceptors().add(ReceivedCookiesInterceptor(context))
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideYandexScheduleApi(okHttpClient: OkHttpClient): YandexScheduleApi {
        val gson = GsonBuilder().registerTypeAdapter(Schedule::class.java, ScheduleDeserializer()).create()
        val retrofit = Retrofit.Builder()
                .baseUrl(YandexScheduleApi.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build()
        return retrofit.create(YandexScheduleApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDBHelper(context: Context): SQLiteOpenHelper {
        return DBHelper(context)
    }

    @Provides
    @Singleton
    fun provideScheduleRepository(dbHelper: SQLiteOpenHelper): ScheduleRepository {
        return SQLiteScheduleRepository(dbHelper)
    }
}
