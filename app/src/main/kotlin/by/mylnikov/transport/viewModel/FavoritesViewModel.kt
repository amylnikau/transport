package by.mylnikov.transport.viewModel

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import by.mylnikov.transport.model.Favorite
import by.mylnikov.transport.repository.FavoritesRepository
import by.mylnikov.transport.view.activity.ScheduleActivity
import by.mylnikov.transport.view.adapter.FavoritesAdapter
import by.mylnikov.transport.view.fragment.FavoritesFragment
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*


class FavoritesViewModel(private val favoritesFragment: FavoritesFragment,
                         favoritesRepository: FavoritesRepository) : ViewModel() {
    val favoritesAdapter = FavoritesAdapter(ArrayList())

    init {
        val favDisposable = favoritesRepository.getFavorites()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter { it.scheduleId.date == "all-days" }
                .subscribe { favoritesAdapter.addItem(it) }
        addToDisposables(favDisposable)

        favoritesAdapter.subscribePositionClicks(object : Observer<Favorite> {
            override fun onNext(favorite: Favorite) {
                val intent = Intent(favoritesFragment.activity, ScheduleActivity::class.java)
                val extraMap = HashMap<String, String>()
                extraMap.put(ScheduleActivity.DEPARTURE_KEY, favorite.scheduleId.from)
                extraMap.put(ScheduleActivity.DESTINATION_KEY, favorite.scheduleId.to)
                extraMap.put(ScheduleActivity.DEPARTURE_NAME, favorite.departureName)
                extraMap.put(ScheduleActivity.DESTINATION_NAME, favorite.destinationName)
                intent.putExtra(ScheduleActivity.EXTRA, extraMap)
                favoritesFragment.startActivity(intent)
            }

            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
                addToDisposables(d)
            }

            override fun onError(e: Throwable?) {
            }

        })
    }

    fun getLayoutManager(): LinearLayoutManager {
        return LinearLayoutManager(favoritesFragment.activity)
    }
}
