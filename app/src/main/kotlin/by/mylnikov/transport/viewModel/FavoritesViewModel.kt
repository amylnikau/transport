package by.mylnikov.transport.viewModel

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import by.mylnikov.transport.repository.FavoritesRepository
import by.mylnikov.transport.view.activity.ScheduleActivity
import by.mylnikov.transport.view.adapter.FavoritesAdapter
import by.mylnikov.transport.view.fragment.FavoritesFragment
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*


class FavoritesViewModel(private val favoritesFragment: FavoritesFragment,
                         favoritesRepository: FavoritesRepository) : ViewModel() {
    val favoritesAdapter = FavoritesAdapter(ArrayList())

    init {
        favoritesRepository.getFavorites()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter { it.scheduleId.date == "all-days" }
                .subscribe { favoritesAdapter.addItem(it) }
        favoritesAdapter.getPositionClicks()
                .subscribe {
                    val intent = Intent(favoritesFragment.activity, ScheduleActivity::class.java)
                    val extraMap = HashMap<String, String>()
                    extraMap.put(ScheduleActivity.DEPARTURE_KEY, it.scheduleId.from)
                    extraMap.put(ScheduleActivity.DESTINATION_KEY, it.scheduleId.to)
                    extraMap.put(ScheduleActivity.DEPARTURE_NAME, it.departureName)
                    extraMap.put(ScheduleActivity.DESTINATION_NAME, it.destinationName)
                    intent.putExtra(ScheduleActivity.EXTRA, extraMap)
                    favoritesFragment.startActivity(intent)
                }
    }

    fun getLayoutManager(): LinearLayoutManager{
        return LinearLayoutManager(favoritesFragment.activity)
    }
}
