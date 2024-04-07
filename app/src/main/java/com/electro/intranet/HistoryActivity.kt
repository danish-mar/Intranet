package com.electro.intranet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryActivity : AppCompatActivity() {
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var databaseConnector: DatabaseConnector
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        historyRecyclerView = findViewById(R.id.historyRecyclerView)
        databaseConnector = DatabaseConnector(this)

        // Retrieve history entries from the database
        val historyEntries = databaseConnector.getAllHistoryEntries()

        // Initialize RecyclerView and adapter
        historyAdapter = HistoryAdapter(historyEntries)
        historyRecyclerView.adapter = historyAdapter
        historyRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}
