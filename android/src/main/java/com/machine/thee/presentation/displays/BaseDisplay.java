package com.machine.thee.presentation.displays;

import android.app.Presentation;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;

import com.machine.thee.presentation.R;

public class BaseDisplay extends Presentation {
  public BaseDisplay(Context outerContext, Display display) {
    super(outerContext, display);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_secondary_display);
  }
}
