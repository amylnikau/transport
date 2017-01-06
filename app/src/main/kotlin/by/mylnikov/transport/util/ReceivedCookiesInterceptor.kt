package by.mylnikov.transport.util

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import by.mylnikov.transport.R
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.*


class ReceivedCookiesInterceptor(val context: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            Log.v("OkHttp", "received cookies")
            val cookies = HashSet<String>()

            for (header in originalResponse.headers("Set-Cookie")) {
                cookies.add(header)
            }

            PreferenceManager.getDefaultSharedPreferences(context).edit()
                    .putStringSet(context.getString(R.string.pref_cookies_key), cookies)
                    .apply()
        }

        return originalResponse
    }
}
