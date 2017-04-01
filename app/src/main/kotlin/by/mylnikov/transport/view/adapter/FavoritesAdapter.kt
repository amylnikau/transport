package by.mylnikov.transport.view.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.mylnikov.transport.databinding.FavoritesItemBinding
import by.mylnikov.transport.model.Favorite
import java.util.*


class FavoritesAdapter(items: ArrayList<Favorite>): BaseAdapter<Favorite, FavoritesAdapter.FavoritesViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FavoritesItemBinding.inflate(inflater, parent, false)
        return FavoritesViewHolder(binding.root)
    }

    override fun onBindViewHolder(viewHolder: FavoritesViewHolder, i: Int) {
        super.onBindViewHolder(viewHolder,i)
        viewHolder.binding.favorite = items[i]
    }

    class FavoritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: FavoritesItemBinding = DataBindingUtil.bind(itemView)

    }
}