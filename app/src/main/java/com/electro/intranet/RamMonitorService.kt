package com.electro.intranet

import android.app.*
import android.content.*
import android.os.*
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Process
import android.util.Log

class RamMonitorService : Service() {
    private val CHANNEL_ID = "RamMonitorServiceChannel"
    private val RAM_REFRESH_INTERVAL = 1000 // 1 seconds

    private val handler = Handler()
    private val ramUsageRunnable = object : Runnable {
        override fun run() {
            refreshRamUsage()
            handler.postDelayed(this, RAM_REFRESH_INTERVAL.toLong())
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("Monitoring RAM Usage")
            .setContentText("RAM Usage: ${getRamUsage()} MB")
            .setSmallIcon(R.drawable.baseline_memory_24)
            .setPriority(Notification.PRIORITY_LOW) // Set priority to low for silent notification
            .build()

        startForeground(1, notification)

        // Start the RAM usage refreshing timer
        handler.postDelayed(ramUsageRunnable, RAM_REFRESH_INTERVAL.toLong())

        return START_STICKY
    }

    override fun onDestroy() {
        // Remove the RAM usage refreshing timer
        handler.removeCallbacks(ramUsageRunnable)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Ram Monitor Service Channel",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun refreshRamUsage() {
        val notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("Monitoring RAM Usage")
            .setContentText("RAM Usage: ${getRamUsage()} MB")
            .setSmallIcon(R.drawable.baseline_memory_24)
            .setPriority(Notification.PRIORITY_LOW) // Set priority to low for silent notification
            .build()

        // Update the notification
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)

        // Retrieve RAM usage of the running MainActivity
        val ramUsage = getRamUsage()
        Log.d("RamMonitorService", "RAM Usage: $ramUsage MB")
    }

    private fun getRamUsage(): Long {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        val totalMemory = memoryInfo.totalMem
        val availableMemory = memoryInfo.availMem
        return (totalMemory - availableMemory) / (1024 * 1024) // Convert bytes to MB
    }
}
