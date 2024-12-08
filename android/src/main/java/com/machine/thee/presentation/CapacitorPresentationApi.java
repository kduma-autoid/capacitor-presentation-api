package com.machine.thee.presentation;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Display;

import androidx.appcompat.app.AppCompatActivity;

import com.getcapacitor.JSObject;
import com.machine.thee.presentation.displays.BaseDisplay;
import com.machine.thee.presentation.displays.BaseWebViewDisplay;
import com.machine.thee.presentation.displays.HtmlDisplay;
import com.machine.thee.presentation.displays.LocalUrlDisplay;
import com.machine.thee.presentation.displays.UrlDisplay;
import com.machine.thee.presentation.displays.VideoDisplay;


public class CapacitorPresentationApi {
    private final Resolver<Context> contextResolver;
    private final Resolver<AppCompatActivity> activityResolver;
    private final PresentationCallbacks callbacks;

    private BaseDisplay display;
    private DisplayManager displayManager = null;
    private Display[] presentationDisplays = null;

    public CapacitorPresentationApi(Resolver<Context> contextResolver, Resolver<AppCompatActivity> activityResolver, PresentationCallbacks callbacks) {
        this.contextResolver = contextResolver;
        this.activityResolver = activityResolver;
        this.callbacks = callbacks;
    }

    public void terminate() {
        if(display == null) {
            return;
        }

        display.dismiss();
    }

    public void openLink(String url) {
        terminate();

        openSecondDisplay((Context context, Display display, PresentationCallbacks callbacks) -> {
            try {
                UrlDisplay urlDisplay;
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    urlDisplay = new UrlDisplay(context, display, callbacks);
                } else {
                    urlDisplay = new LocalUrlDisplay(context, display, callbacks);
                }

                urlDisplay.show();
                urlDisplay.start(url);

                return urlDisplay;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        });
    }

    public void openHtml(String html) {
        terminate();

        openSecondDisplay((Context context, Display display, PresentationCallbacks callbacks) -> {
            try {
                var htmlDisplay = new HtmlDisplay(context, display, callbacks);

                htmlDisplay.show();
                htmlDisplay.start(html);

                return htmlDisplay;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        });
    }

    public void openVideo(String url, boolean showControls) {
        terminate();

        openSecondDisplay((Context context, Display display, PresentationCallbacks callbacks) -> {
            try {
                var videoDisplay = new VideoDisplay(context, display);

                videoDisplay.show();
                videoDisplay.start(new VideoOptions(url, showControls));

                return videoDisplay;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        });
    }

    public void sendMessage(JSObject data) {
        if(display != null && display instanceof BaseWebViewDisplay) {
            ((BaseWebViewDisplay) display).sendMessage(data);
        }
    }

    public int getDisplaysCount() {
        var displayManager = (DisplayManager) activityResolver.resolve().getSystemService(Context.DISPLAY_SERVICE);

        if (displayManager == null) {
            return 0;
        }

        var presentationDisplays = displayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);

        return presentationDisplays.length;
    }

    private void openSecondDisplay(DisplayCallback callback) {
        new Handler(Looper.getMainLooper()).post(() -> {
            displayManager = (DisplayManager) activityResolver.resolve().getSystemService(Context.DISPLAY_SERVICE);
            if (displayManager != null) {
                presentationDisplays = displayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
                if (presentationDisplays.length > 0) {
                    Log.d("presentationDisplays", String.valueOf(presentationDisplays[0]));
                    display = callback.onDisplayReady(contextResolver.resolve(), presentationDisplays[0], callbacks);
                }
            }
        });
    }

    private interface DisplayCallback {
        BaseDisplay onDisplayReady(Context context, Display display, PresentationCallbacks callbacks);
    }

    public interface Resolver<T> {
        T resolve();
    }
}
