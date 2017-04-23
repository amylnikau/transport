package by.mylnikov.transport

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager

import by.mylnikov.transport.di.component.DaggerTransportAppComponent
import by.mylnikov.transport.di.component.TransportAppComponent
import by.mylnikov.transport.di.module.TransportModule
import by.mylnikov.transport.repository.sqlite.RemoveOldScheduleSpecification


class TransportApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerTransportAppComponent.builder()
                .transportModule(TransportModule(this))
                .build()
        appComponent.scheduleRepository().modify(RemoveOldScheduleSpecification(1))
    }

    companion object {
        //platformStatic allow access it from java code
        @JvmStatic lateinit var appComponent: TransportAppComponent

        @JvmStatic fun isConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
            return isConnected
        }
    }


}
