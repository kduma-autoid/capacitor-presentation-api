package com.machine.thee.presentation.displays;

import android.content.Context;
import android.view.Display;

import com.machine.thee.presentation.PresentationCallbacks;

public class VideoDisplay extends SecondaryDisplay {
    public VideoDisplay(Context outerContext, Display display, PresentationCallbacks callbacks) {
        super(outerContext, display, callbacks);
    }
}
