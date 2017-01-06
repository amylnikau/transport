package by.mylnikov.transport.viewModel

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import by.mylnikov.transport.R
import by.mylnikov.transport.api.YandexScheduleApi
import by.mylnikov.transport.model.ScheduleID
import by.mylnikov.transport.repository.ScheduleRepository
import by.mylnikov.transport.repository.sqlite.ChangeFavoriteSpecification
import by.mylnikov.transport.view.activity.ScheduleActivity
import by.mylnikov.transport.view.adapter.ScheduleAdapter
import okhttp3.MediaType
import okhttp3.RequestBody
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*


class ScheduleViewModel(private val scheduleActivity: ScheduleActivity,
                        private val scheduleRepository: ScheduleRepository,
                        private val yandexApi: YandexScheduleApi) : ViewModel() {

    val layoutManager = LinearLayoutManager(scheduleActivity)
    val isFavoriteRoute = ObservableBoolean(false)
    val errorMessage = ObservableField<String>()
    val updatedTime = ObservableField<String>()
    val departureName: String
    val destinationName: String
    private val requestBody: String
    private val scheduleId: ScheduleID
    private val scheduleAdapter = ScheduleAdapter(ArrayList())
    private var mLayoutState: Parcelable? = null
    private var showAll = true
    private var subscription: Subscription? = null

    companion object {
        private const val LAYOUT_STATE_KEY = "LAYOUT_STATE_KEY"
        private const val SHOW_ALL_KEY = "SHOW_ALL_KEY"
        private const val ANIMATION_DURATION = 1000L
        private const val REQUEST_BODY_TEMPLATE = "{\"methods\":[{\"method\":\"search\",\"params\":{\"context\":{\"userInput\":{\"from\":{\"title\":\"%1\$s\",\"key\":\"%2\$s\"},\"to\":{\"title\":\"%3\$s\",\"key\":\"%4\$s\"}},\"transportType\":\"all\",\"from\":{\"key\":\"%2\$s\",\"title\":\"%1\$s\",\"timezone\":\"Europe/Minsk\",\"country\":{\"code\":\"BY\",\"railwayTimezone\":\"Europe/Minsk\"}},\"to\":{\"key\":\"%4\$s\",\"title\":\"%3\$s\",\"timezone\":\"Europe/Minsk\",\"country\":{\"code\":\"BY\",\"railwayTimezone\":\"Europe/Minsk\"}},\"searchNext\":false,\"when\":{%5\$s},\"language\":\"ru\",\"searchForPastDate\":false,\"sameSuburbanZone\":true},\"page\":{\"location\":{},\"fullUrl\":\"\"},\"nationalVersion\":\"by\"}}]}"
    }

    init {
        @Suppress("UNCHECKED_CAST")
        val requestData = scheduleActivity.intent.extras.getSerializable(ScheduleActivity.EXTRA) as HashMap<String, String>
        departureName = requestData[ScheduleActivity.DEPARTURE_NAME]!!
        destinationName = requestData[ScheduleActivity.DESTINATION_NAME]!!
        val departureId = requestData[ScheduleActivity.DEPARTURE_KEY]!!
        val destinationId = requestData[ScheduleActivity.DESTINATION_KEY]!!
        val departureDate: String
        if (requestData.containsKey(ScheduleActivity.DEPARTURE_DATE)) {
            val date = requestData[ScheduleActivity.DEPARTURE_DATE]!!
            departureDate = "\"date\":\"$date\""
            scheduleId = ScheduleID(departureId, destinationId, date)
        } else {
            departureDate = "\"special\":\"all-days\""
            scheduleId = ScheduleID(departureId, destinationId, "all-days")
        }
        requestBody = REQUEST_BODY_TEMPLATE.format(departureName.replace("\"", "\\\""), departureId, destinationName.replace("\"", "\\\""), destinationId, departureDate)

        val swipeRefreshLayout = scheduleActivity.findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            yandexApi.getSchedule(RequestBody.create(MediaType.parse("application/json"), requestBody))
                    .doOnNext { scheduleRepository.updateSchedule(it) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it.records.size > 0) {
                            scheduleAdapter.updateItems(it.records)
                        }
                        swipeRefreshLayout.isRefreshing = false
                    }, {
                        swipeRefreshLayout.isRefreshing = false
                        val coordinatorLayout = scheduleActivity.findViewById(R.id.coordinatorLayout) as CoordinatorLayout
                        val snackBar = Snackbar.make(coordinatorLayout, R.string.no_internet, Snackbar.LENGTH_LONG)
                        (snackBar.view.findViewById(android.support.design.R.id.snackbar_text) as TextView).setTextColor(Color.WHITE)
                        snackBar.show()
                    })
        }
    }

    fun getAdapter(): ScheduleAdapter {
        val network = yandexApi.getSchedule(RequestBody.create(MediaType.parse("application/json"), requestBody))
                .doOnNext { scheduleRepository.addSchedule(it) }
        val disk = scheduleRepository.getSchedule(scheduleId)
        subscription = Observable.concat(disk, network).first()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val progressBar = scheduleActivity.findViewById(R.id.progressBar)
                    val recyclerView = scheduleActivity.findViewById(R.id.recyclerView)
                    val height = recyclerView.height.toFloat()
                    if (mLayoutState == null) {
                        progressBar.animate()
                                .translationY(-height / 2)
                                .alpha(0f)
                                .setDuration(ANIMATION_DURATION)
                                .setListener(object : AnimatorListenerAdapter() {
                                    override fun onAnimationEnd(animation: Animator?) {
                                        super.onAnimationEnd(animation)
                                        progressBar.visibility = View.GONE
                                    }
                                })
                                .start()
                    } else {
                        progressBar.visibility = View.GONE
                    }
                    if (it.records.size > 0) {
                        updatedTime.set(it.updateTime)
                        isFavoriteRoute.set(it.isFavorite)
                        scheduleAdapter.updateItems(it.records)
                        if (mLayoutState == null) {
                            recyclerView.translationY = height
                            recyclerView.alpha = 0f
                            recyclerView.animate()
                                    .translationY(0f)
                                    .alpha(1f)
                                    .setDuration(ANIMATION_DURATION)
                                    .start()
                        } else {
                            scheduleAdapter.changeDisplayMode(showAll)
                            layoutManager.onRestoreInstanceState(mLayoutState)
                        }
                    } else {
                        errorMessage.set(scheduleActivity.resources.getString(R.string.no_schedule))
                    }
                }, {
                    val progressBar = scheduleActivity.findViewById(R.id.progressBar)
                    progressBar.visibility = View.GONE
                    errorMessage.set(scheduleActivity.resources.getString(R.string.no_internet))
                })
        return scheduleAdapter
    }

    fun onFavoriteClick(v: View) {
        val isFavorite = isFavoriteRoute.get()
        val coordinatorLayout = scheduleActivity.findViewById(R.id.coordinatorLayout) as CoordinatorLayout
        val snackBar: Snackbar
        if (isFavorite) {
            snackBar = Snackbar.make(coordinatorLayout, R.string.remove_favorite, Snackbar.LENGTH_LONG)
                    .setAction(scheduleActivity.getString(R.string.undo), {
                        scheduleRepository.modify(ChangeFavoriteSpecification(scheduleId, true))
                        isFavoriteRoute.set(true)
                    })
            scheduleRepository.modify(ChangeFavoriteSpecification(scheduleId, false))
            isFavoriteRoute.set(false)
        } else {
            snackBar = Snackbar.make(coordinatorLayout, R.string.add_favorite, Snackbar.LENGTH_SHORT)
            scheduleRepository.modify(ChangeFavoriteSpecification(scheduleId, true))
            isFavoriteRoute.set(true)
        }
        (snackBar.view.findViewById(android.support.design.R.id.snackbar_text) as TextView).setTextColor(Color.WHITE)
        snackBar.show()
    }


    fun onDisplayModeClick(v: View) {
        showAll = !showAll
        scheduleAdapter.changeDisplayMode(showAll)
        layoutManager.scrollToPosition(0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mLayoutState = layoutManager.onSaveInstanceState()
        outState.putParcelable(LAYOUT_STATE_KEY, mLayoutState)
        outState.putBoolean(SHOW_ALL_KEY, showAll)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null) {
            mLayoutState = savedInstanceState.getParcelable(LAYOUT_STATE_KEY)
            showAll = savedInstanceState.getBoolean(SHOW_ALL_KEY)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val sub = subscription
        if (sub != null && !sub.isUnsubscribed)
            sub.unsubscribe()
    }

}