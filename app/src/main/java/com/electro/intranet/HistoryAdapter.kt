package com.electro.intranet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(private val historyEntries: List<HistoryEntry>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val urlTextView: TextView = itemView.findViewById(R.id.urlTextView)
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_entry_item, parent, false)
        return HistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentEntry = historyEntries[position]
        holder.urlTextView.text = currentEntry.url
        holder.titleTextView.text = currentEntry.title
        holder.timestampTextView.text = currentEntry.timestamp.toString()
    }

    override fun getItemCount(): Int {
        return historyEntries.size
    }
}
