package com.machine.thee.presentation;

import android.webkit.WebView;

import com.getcapacitor.JSObject;

public interface PresentationCallbacks {
    void onMessage(JSObject jsObject);
    void onSuccessLoadUrl(WebView view, String url);
    void onFailLoadUrl(WebView view, int errorCode);
}
