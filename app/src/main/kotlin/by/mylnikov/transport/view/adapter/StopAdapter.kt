package by.mylnikov.transport.view.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.mylnikov.transport.api.model.TransportStop
import by.mylnikov.transport.databinding.StopItemBinding
import java.util.*

class StopAdapter(stops: ArrayList<TransportStop>) : BaseAdapter<TransportStop, StopAdapter.StopViewHolder>(stops) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = StopItemBinding.inflate(inflater, parent, false)
        return StopViewHolder(binding.root)
    }

    override fun onBindViewHolder(viewHolder: StopViewHolder, i: Int) {
        super.onBindViewHolder(viewHolder, i)
        viewHolder.binding.transportStop = items[i]
    }


    class StopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: StopItemBinding

        init {
            binding = DataBindingUtil.bind(itemView)
        }
    }
}
