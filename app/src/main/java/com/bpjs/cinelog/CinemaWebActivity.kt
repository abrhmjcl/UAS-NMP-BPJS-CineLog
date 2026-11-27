package com.bpjs.cinelog

import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.bpjs.cinelog.databinding.ActivityCinemaWebBinding

class CinemaWebActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCinemaWebBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCinemaWebBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val targetUrl = intent.getStringExtra("extra_url") ?: "https://www.youtube.com"

        with(binding.webViewCinema.settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
        }

        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(binding.webViewCinema, true)

        binding.webViewCinema.webViewClient = WebViewClient()
        binding.webViewCinema.webChromeClient = WebChromeClient()
        binding.webViewCinema.loadUrl(targetUrl)
    }
}