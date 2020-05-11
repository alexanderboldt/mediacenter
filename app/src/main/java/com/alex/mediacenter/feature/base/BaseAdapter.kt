package com.alex.mediacenter.feature.base

import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

abstract class BaseAdapter<T, VH> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected val items = ArrayList<T>()

    var clickSubject = PublishSubject.create<T>()

    // ----------------------------------------------------------------------------

    fun clearItems() {
        this.items.clear()

        notifyDataSetChanged()
    }

    fun setItems(items: ArrayList<T>) {
        this.items.clear()
        this.items.addAll(items)

        notifyDataSetChanged()
    }

    // ----------------------------------------------------------------------------

    final override fun getItemCount() = items.size

    final override fun getItemViewType(position: Int) = getItemViewType(items[position])

    final override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[holder.adapterPosition]

        holder.itemView.clicks()
            .throttleFirst(1, TimeUnit.SECONDS)
            .map { item }
            .subscribe(clickSubject)

        onBindViewHolder(holder as VH, item)
    }

    // ----------------------------------------------------------------------------

    open fun getItemViewType(item: T) = 0
    open fun onBindViewHolder(holder: VH, item: T) {}
}