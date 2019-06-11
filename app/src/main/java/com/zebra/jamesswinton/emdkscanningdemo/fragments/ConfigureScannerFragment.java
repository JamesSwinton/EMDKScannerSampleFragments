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
import com.zebra.jamesswinton.emdkscanningdemo.databinding.FragmentConfigureScannerBinding;
import com.zebra.jamesswinton.emdkscanningdemo.databinding.FragmentEnableScannerBinding;
import com.zebra.jamesswinton.emdkscanningdemo.fragments.InitEmdkFragment.OnEmdkInitialisedListener;

public class ConfigureScannerFragment extends Fragment implements EMDKHelper.ProgressCallback {

  // Debugging
  private static final String TAG = "ConfigureScannerFrag";

  // Constants


  // Static Variables


  // Non-Static Variables
  private FragmentConfigureScannerBinding mDataBinding;

  private OnScannerConfiguredListener mCallback;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.i(TAG, "Creating Fragment...");
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater li, ViewGroup vg, Bundle savedInstanceState) {
    // Init DataBinding
    mDataBinding = DataBindingUtil.inflate(li, R.layout.fragment_configure_scanner, vg, false);

    // Set Click Listener
    mDataBinding.configureScanner.setOnClickListener(v -> configureScanner());

    // Return View
    return mDataBinding.getRoot();
  }

  private void configureScanner() {
    MainActivity.mEmdkHelper.setListener(this);
    MainActivity.mEmdkHelper.configureScanner(mDataBinding);
  }

  public void setOnScannerConfiguredListener(OnScannerConfiguredListener callback) {
    mCallback = callback;
  }

  public interface OnScannerConfiguredListener {
    void onScannerConfigured();
    void onScannerConfigureFailure();
  }

  @Override
  public void complete() {
    mCallback.onScannerConfigured();
  }

  @Override
  public void error(String error) {
    mCallback.onScannerConfigureFailure();
  }
}
