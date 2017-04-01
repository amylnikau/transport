package by.mylnikov.transport.viewModel

import android.app.Activity
import android.content.Intent
import android.databinding.ObservableBoolean
import android.os.Handler
import android.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import by.mylnikov.transport.api.YandexScheduleApi
import by.mylnikov.transport.api.model.TransportStop
import by.mylnikov.transport.view.activity.SearchActivity
import by.mylnikov.transport.view.adapter.StopAdapter
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class SearchViewModel(private val searchActivity: SearchActivity,
                      private val yandexApi: YandexScheduleApi) : ViewModel() {
    val progressVisibility = ObservableBoolean(false)
    val stopAdapter = StopAdapter(ArrayList())
    val queryTextListener: SearchView.OnQueryTextListener
    val layoutManager = LinearLayoutManager(searchActivity)
    private val city: String
    private val mHandler = Handler()

    init {
        val preferences = PreferenceManager.getDefaultSharedPreferences(searchActivity)
        city = preferences.getString("city", "157")
        queryTextListener = object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty()) {
                    mHandler.removeCallbacksAndMessages(null)

                    mHandler.postDelayed({
                        progressVisibility.set(true)
                        yandexApi.getSuggestions(city, newText)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe {
                                    progressVisibility.set(false)
                                    stopAdapter.updateItems(it.getBusSuggestions())
                                }
                    }, 500)
                }
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
        }

        stopAdapter.subscribePositionClicks(object : Observer<TransportStop>{
            override fun onSubscribe(d: Disposable) {
                addToDisposables(d)
            }

            override fun onError(e: Throwable?) {
            }

            override fun onComplete() {
            }

            override fun onNext(transportStop: TransportStop) {
                val result = Intent()
                result.putExtra(MainViewModel.TRANSPORT_STOP, transportStop)
                searchActivity.setResult(Activity.RESULT_OK, result)
                searchActivity.finish()
            }

        })
    }

}
