package com.example.noteapp_firebase1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.itemlayout.view.*


class adapteritem(val activity:MainActivity, val list: ArrayList<Note>,) : RecyclerView.Adapter<adapteritem.ItemViewHolder>() {
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.itemlayout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = list[position]

        holder.itemView.apply {
            tv1.text = item.note
            imE.setOnClickListener {
                activity.Alert(item)
            }

            imD.setOnClickListener {
                activity.removeNote(item)
            }
        }
    }

    override fun getItemCount() = list.size
}

