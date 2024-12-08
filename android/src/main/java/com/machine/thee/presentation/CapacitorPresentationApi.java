package com.machine.thee.presentation;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Display;

import androidx.appcompat.app.AppCompatActivity;

import com.getcapacitor.JSObject;
import com.machine.thee.presentation.displays.SecondaryDisplay;


public class CapacitorPresentationApi {
    private final Resolver<Context> contextResolver;
    private final Resolver<AppCompatActivity> activityResolver;
    private final PresentationCallbacks callbacks;

    private SecondaryDisplay display;
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
        openSecondDisplay(display -> {
            try {
                if(display != null) {
                    display.show();
                    display.init(OpenType.URL, url);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void openHtml(String html) {
        openSecondDisplay(display -> {
            try {
                if(display != null) {
                    display.show();
                    display.init(OpenType.HTML, html);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void openVideo(String url, boolean showControls) {
        openSecondDisplay(display -> {
            try {
                if(display != null) {
                    display.show();
                    display.init(OpenType.VIDEO, new VideoOptions(url, showControls));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void sendMessage(JSObject data) {
        if(display != null) {
            display.sendMessage(data);
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
                    display = new SecondaryDisplay(contextResolver.resolve(), presentationDisplays[0], callbacks);
                    callback.onDisplayReady(display);
                }
            }
        });
    }

    private interface DisplayCallback {
        void onDisplayReady(SecondaryDisplay display);
    }

    public interface Resolver<T> {
        T resolve();
    }
}
