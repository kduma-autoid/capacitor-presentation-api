package com.machine.thee.presentation.displays;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.machine.thee.presentation.R;
import com.machine.thee.presentation.VideoOptions;

public class VideoDisplay extends BaseDisplay {
    protected VideoView videoView;

    public VideoDisplay(Context outerContext, Display display) {
        super(outerContext, display);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        videoView = findViewById(R.id.videoView);
        videoView.setVisibility(View.VISIBLE);
    }

    public void start(VideoOptions data) {

        Uri uri = Uri.parse(data.videoUrl);
        videoView.setVideoURI(uri);

        if (data.showControls) {
            MediaController mediaController = new MediaController(this.getContext());
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
        }

        videoView.start();
    }
}
