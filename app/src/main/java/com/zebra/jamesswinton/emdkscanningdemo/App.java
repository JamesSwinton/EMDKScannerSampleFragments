package com.zebra.jamesswinton.emdkscanningdemo;

import android.app.Application;
import android.util.Log;

public class App extends Application {

  // Debugging
  private static final String TAG = "ApplicationClass";

  // Constants


  // Static Variables


  // Non-Static Variables


  @Override
  public void onCreate() {
    super.onCreate();
    Log.i(TAG, "Application Started");
  }
}
