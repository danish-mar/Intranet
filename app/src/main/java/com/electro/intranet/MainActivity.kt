package com.electro.intranet


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var urlEditText: EditText
    private lateinit var databaseConnector: DatabaseConnector



    private val ramMonitorServiceIntent by lazy {
        Intent(this, RamMonitorService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Start RamMonitorService
        val serviceIntent = Intent(this, RamMonitorService::class.java)
        startService(serviceIntent)


        webView = findViewById(R.id.wb_webView)
        urlEditText = findViewById(R.id.urlEditText)
        databaseConnector = DatabaseConnector(this)

        webView.settings.javaScriptEnabled = true
        webView.settings.safeBrowsingEnabled = false
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                urlEditText.setText(url)

                // Add history entry when page finishes loading
                url?.let { title ->
                    val timestamp = System.currentTimeMillis()
                    databaseConnector.addHistoryEntry(url, title, timestamp)
                }
            }
        }

        // Check if the activity is started with a URL
        val intent = intent
        val action = intent.action
        val data: Uri? = intent.data
        if (Intent.ACTION_VIEW == action && data != null) {
            val url = data.toString()
            webView.loadUrl(url)
        } else {
            // Load a default URL
            val defaultUrl = "https://www.google.com"
            webView.loadUrl(defaultUrl)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_back -> {
                if (webView.canGoBack()) {
                    webView.goBack()
                }
                return true
            }
            R.id.action_refresh -> {
                webView.reload()
                return true
            }
            R.id.action_forward -> {
                if (webView.canGoForward()) {
                    webView.goForward()
                } else {
                    val query = urlEditText.text.toString()
                    if (isValidUrl(query)) {
                        webView.loadUrl(query)
                    } else {
                        val searchUrl = "https://www.google.com/search?q=$query"
                        webView.loadUrl(searchUrl)
                    }
                }
                return true
            }
            R.id.action_history -> {
                // Start HistoryActivity
                val intent = Intent(this, HistoryActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun isValidUrl(url: String): Boolean {
        return url.startsWith("http://") || url.startsWith("https://") || url.startsWith("www")
    }


    override fun onDestroy() {
        stopService(ramMonitorServiceIntent)
        super.onDestroy()
    }
}
