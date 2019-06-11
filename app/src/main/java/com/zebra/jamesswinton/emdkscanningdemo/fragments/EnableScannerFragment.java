package com.zebra.jamesswinton.emdkscanningdemo.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zebra.jamesswinton.emdkscanningdemo.EMDKHelper;
import com.zebra.jamesswinton.emdkscanningdemo.MainActivity;
import com.zebra.jamesswinton.emdkscanningdemo.R;
import com.zebra.jamesswinton.emdkscanningdemo.databinding.FragmentEnableScannerBinding;

public class EnableScannerFragment extends Fragment implements EMDKHelper.ProgressCallback {

  // Debugging
  private static final String TAG = "EnableScannerFragment";

  // Constants


  // Static Variables


  // Non-Static Variables
  private FragmentEnableScannerBinding mDataBinding;
  private OnScannerEnabledListener mCallback;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater li, ViewGroup vg, Bundle savedInstanceState) {
    // Init DataBinding
    mDataBinding = DataBindingUtil.inflate(li, R.layout.fragment_enable_scanner, vg, false);

    // Set Click Listener
    mDataBinding.enableScanner.setOnClickListener(v -> enableScanner());

    // Return View
    return mDataBinding.getRoot();
  }

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);

    if (isVisibleToUser) {
      Log.i(TAG, "Fragment Showing");
    }
  }

  private void enableScanner() {
    MainActivity.mEmdkHelper.setListener(this);
    MainActivity.mEmdkHelper.enableScanner();
  }

  public void setOnScannerEnabledListener(OnScannerEnabledListener callback) {
    mCallback = callback;
  }

  public interface OnScannerEnabledListener {
    void onScannerEnabled();
    void onScannerFailure();
  }

  @Override
  public void complete() {
    mCallback.onScannerEnabled();
  }

  @Override
  public void error(String error) {
    mCallback.onScannerFailure();
  }
}
