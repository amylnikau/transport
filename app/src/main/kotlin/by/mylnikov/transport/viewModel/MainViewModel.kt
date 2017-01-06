package by.mylnikov.transport.viewModel

import android.content.Context
import android.content.Intent
import android.databinding.ObservableField
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.LinearLayout
import by.mylnikov.transport.R
import by.mylnikov.transport.api.model.TransportStop
import by.mylnikov.transport.view.activity.ScheduleActivity
import by.mylnikov.transport.view.activity.SearchActivity
import by.mylnikov.transport.view.fragment.MainFragment
import com.google.gson.Gson
import com.transitionseverywhere.ChangeBounds
import com.transitionseverywhere.TransitionManager
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import java.text.SimpleDateFormat
import java.util.*


class MainViewModel(private val mainFragment: MainFragment) : ViewModel(), DatePickerDialog.OnDateSetListener {

    val tripDate = ObservableField<Date?>()
    val textDeparture = ObservableField<String>(mainFragment.getString(R.string.from))
    val textDestination = ObservableField<String>(mainFragment.getString(R.string.to))
    var departure: TransportStop? = null
        set(value) {
            field = value
            val text = value?.stopName ?: mainFragment.getString(R.string.from)
            if (isSwap) {
                textDestination.set(text)
            } else {
                textDeparture.set(text)
            }
        }
    var destination: TransportStop? = null
        set(value) {
            field = value
            val text = value?.stopName ?: mainFragment.getString(R.string.to)
            if (isSwap) {
                textDeparture.set(text)
            } else {
                textDestination.set(text)
            }
        }
    private var isSwap = false

    companion object {
        private const val DATE_KEY = "date_key"
        private const val DEPARTURE_KEY = "from_key"
        private const val DESTINATION_KEY = "to_key"
        private const val PREF_NAME = "main_preference"
        const val REQUEST_CODE_DEPARTURE = 1
        const val REQUEST_CODE_DESTINATION = 2
        const val TRANSPORT_STOP = "transportStop"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val settings = mainFragment.activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val gson = Gson()
            departure = gson.fromJson(settings.getString(DEPARTURE_KEY, ""), TransportStop::class.java)
            destination = gson.fromJson(settings.getString(DESTINATION_KEY, ""), TransportStop::class.java)
            tripDate.set(Calendar.getInstance().time)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val curDate = tripDate.get()?.time
        if (curDate != null)
            outState.putLong(DATE_KEY, curDate)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            tripDate.set(Date(savedInstanceState.getLong(DATE_KEY)))
        }
    }

    fun onTextClick(v: View) {
        val intent = Intent(mainFragment.activity, SearchActivity::class.java)
        val requestCode: Int
        if (isSwap) {
            if (v.id == R.id.departure)
                requestCode = REQUEST_CODE_DESTINATION
            else
                requestCode = REQUEST_CODE_DEPARTURE
        } else {
            if (v.id == R.id.departure)
                requestCode = REQUEST_CODE_DEPARTURE
            else
                requestCode = REQUEST_CODE_DESTINATION
        }
        mainFragment.startActivityForResult(intent, requestCode)
    }

    fun onSwapClick(v: View) {
        if (departure != null && destination != null) {
            isSwap = !isSwap
            val temp = departure
            departure = destination
            destination = temp
            val rotateAnimation = RotateAnimation(0.0f, 180.0f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f)
            val linearLayout = mainFragment.view.findViewById(R.id.linearLayout) as LinearLayout
            val view = linearLayout.getChildAt(0)
            TransitionManager.beginDelayedTransition(linearLayout, ChangeBounds())

            // Set the animation's parameters
            rotateAnimation.duration = 500
            rotateAnimation.repeatCount = 0
            rotateAnimation.repeatMode = Animation.REVERSE
            rotateAnimation.fillAfter = true

            linearLayout.removeViewAt(0)
            linearLayout.addView(view, 1)
            v.startAnimation(rotateAnimation)
        }


    }

    fun onClickClear(v: View) {
        tripDate.set(null)
    }

    fun onClickDate(v: View) {
        val calendar = Calendar.getInstance()
        val curDate = tripDate.get()
        if (curDate != null)
            calendar.time = curDate
        val dpd = DatePickerDialog.newInstance(
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
        dpd.minDate = Calendar.getInstance()
        dpd.show(mainFragment.fragmentManager, "DatePickerDialog")

    }

    fun onClickSchedule(v: View) {
        if (departure != null && destination != null) {
            val intent = Intent(mainFragment.activity, ScheduleActivity::class.java)
            val extraMap = HashMap<String, String>()
            val gson = Gson()
            val editor = mainFragment.activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
            if (tripDate.get() != null)
                extraMap.put(ScheduleActivity.DEPARTURE_DATE, SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(tripDate.get()))
            departure?.apply {
                extraMap.put(ScheduleActivity.DEPARTURE_KEY, stopId)
                extraMap.put(ScheduleActivity.DEPARTURE_NAME, stopName)
                editor.putString(DEPARTURE_KEY, gson.toJson(this))
            }
            destination?.apply {
                extraMap.put(ScheduleActivity.DESTINATION_KEY, stopId)
                extraMap.put(ScheduleActivity.DESTINATION_NAME, stopName)
                editor.putString(DESTINATION_KEY, gson.toJson(this))
            }
            editor.apply()
            intent.putExtra(ScheduleActivity.EXTRA, extraMap)
            mainFragment.startActivity(intent)
        }

    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val selectedDate = GregorianCalendar(year, monthOfYear, dayOfMonth).time
        tripDate.set(selectedDate)
    }

}
