package by.mylnikov.transport.util

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import by.mylnikov.transport.R
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.*


class AddCookiesInterceptor(val context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
                .getStringSet(context.getString(R.string.pref_cookies_key), HashSet())
        for (cookie in preferences) {
            builder.addHeader("Cookie", cookie)
            Log.v("OkHttp", "Adding Header: " + cookie)
        }

        return chain.proceed(builder.build())
    }
}