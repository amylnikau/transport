package by.mylnikov.transport.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.reactivex.Observer
import io.reactivex.subjects.PublishSubject


abstract class BaseAdapter<T, K : RecyclerView.ViewHolder>(val items: ArrayList<T>) : RecyclerView.Adapter<K>() {

    private val onClickSubject: PublishSubject<T> = PublishSubject.create()

    fun updateItems(newItems: List<T>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun addItem(newItem: T) {
        items.add(newItem)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): K {
        throw UnsupportedOperationException("Not implemented")
    }

    override fun onBindViewHolder(viewHolder: K, i: Int) {
        viewHolder.itemView.setOnClickListener({ onClickSubject.onNext(items[i]) })
    }

    fun subscribePositionClicks(observer: Observer<T>) {
        onClickSubject.subscribe(observer)
    }
}
