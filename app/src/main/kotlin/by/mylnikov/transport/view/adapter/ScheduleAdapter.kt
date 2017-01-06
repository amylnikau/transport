package by.mylnikov.transport.view.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.mylnikov.transport.databinding.ScheduleItemBinding
import by.mylnikov.transport.model.RouteRecord
import java.util.*


class ScheduleAdapter(records: ArrayList<RouteRecord>) : BaseAdapter<RouteRecord, ScheduleAdapter.ScheduleViewHolder>(records) {

    private var offset = 0

    private fun calculateOffset(): Int {
        var low = 0
        var high = items.size - 1
        val currentTime = "12:15"//SimpleDateFormat("HH:mm", Locale.getDefault())
        //.format(Calendar.getInstance().time)
        while (low <= high) {
            val mid = (low + high).ushr(1)
            val midVal = items[mid]
            val cmp = midVal.timeDeparture.compareTo(currentTime)

            if (cmp < 0)
                low = mid + 1
            else if (cmp > 0)
                high = mid - 1
            else
                return mid
        }
        return low
    }

    fun changeDisplayMode(showAll:Boolean) {
        if (showAll) {
            offset = 0
        } else {
            offset = calculateOffset()
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return super.getItemCount() - offset
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ScheduleItemBinding.inflate(inflater, parent, false)
        return ScheduleViewHolder(binding.root)
    }

    override fun onBindViewHolder(viewHolder: ScheduleViewHolder, i: Int) {
        super.onBindViewHolder(viewHolder, i)
        viewHolder.binding.routeRecord = items[i + offset]
    }


    class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ScheduleItemBinding

        init {
            binding = DataBindingUtil.bind(itemView)
        }
    }

}