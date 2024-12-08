package com.machine.thee.presentation.displays;

import android.content.Context;
import android.view.Display;

import com.machine.thee.presentation.PresentationCallbacks;

public class HtmlDisplay extends BaseWebViewDisplay {
    public HtmlDisplay(Context outerContext, Display display, PresentationCallbacks callbacks) {
        super(outerContext, display, callbacks);
    }

    public void start(String data) {
        if (webView == null) {
            return;
        }

        this.startWebView();

        webView.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);
    }
}
