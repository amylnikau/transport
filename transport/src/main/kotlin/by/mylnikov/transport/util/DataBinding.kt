package by.mylnikov.transport.util

import android.databinding.BindingAdapter
import android.support.design.widget.FloatingActionButton
import android.widget.Button
import by.mylnikov.transport.R
import java.text.SimpleDateFormat
import java.util.*


object DataBinding {

    @BindingAdapter("android:text")
    @JvmStatic fun setText(view: Button, date: Date?) {
        val strDate: String
        if (date != null) {
            if (getZeroTimeDate(date).compareTo(getZeroTimeDate(Date())) == 0)
                strDate = view.context.getString(R.string.today)
            else
                strDate = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault()).format(date)
        } else
            strDate = view.context.getString(R.string.all_days)
        view.text = strDate
    }

    @BindingAdapter("app:srcCompat")
    @JvmStatic fun srcCompat(actionButton: FloatingActionButton, resourceId: Int) {
        actionButton.setImageResource(resourceId)
    }

    private @JvmStatic fun getZeroTimeDate(date: Date): Date {
        val calendar = Calendar.getInstance()

        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.time
    }
}
