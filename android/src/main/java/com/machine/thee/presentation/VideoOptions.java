package com.machine.thee.presentation;

public class VideoOptions {
  public boolean showControls = false;
  public String videoUrl = "";

  VideoOptions(String videoUrl,boolean showControls) {
    this.showControls = showControls;
    this.videoUrl = videoUrl;
  }
}
