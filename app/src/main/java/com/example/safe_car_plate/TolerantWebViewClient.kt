package com.example.safe_car_plate

import android.net.http.SslError

import android.webkit.SslErrorHandler

import android.webkit.WebView

import android.webkit.WebViewClient




class TolerantWebViewClient : WebViewClient() {
    override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
        handler.proceed()
    }

}