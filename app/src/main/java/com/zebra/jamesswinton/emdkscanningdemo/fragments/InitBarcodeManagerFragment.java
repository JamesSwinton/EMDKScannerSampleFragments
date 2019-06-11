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
import com.zebra.jamesswinton.emdkscanningdemo.databinding.FragmentInitBarcodeManagerBinding;

public class InitBarcodeManagerFragment extends Fragment implements EMDKHelper.ProgressCallback {

  // Debugging
  private static final String TAG = "BarcodeManagerFragment";

  // Constants


  // Static Variables


  // Non-Static Variables
  private FragmentInitBarcodeManagerBinding mDataBinding;

  private OnBarcodeManagerInitialised mCallback;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.i(TAG, "Creating Fragment...");
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater li, ViewGroup vg, Bundle savedInstanceState) {
    // Init DataBinding
    mDataBinding = DataBindingUtil.inflate(li, R.layout.fragment_init_barcode_manager, vg, false);

    // Set Click Listener
    mDataBinding.getBarcodeManager.setOnClickListener(v -> initBarcodeManager());

    // Return View
    return mDataBinding.getRoot();
  }

  private void initBarcodeManager() {
    MainActivity.mEmdkHelper.setListener(this);
    MainActivity.mEmdkHelper.getBarcodeManager();
  }

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);

    if (isVisibleToUser) {
      Log.i(TAG, "Fragment Showing");
    }
  }

  public void setOnBarcodeManagerInitialisedListener(OnBarcodeManagerInitialised callback) {
    mCallback = callback;
  }

  public interface OnBarcodeManagerInitialised {
    void onBarcodeManagerInitialised();
    void onBarcodeFailure();
  }

  @Override
  public void complete() {
    mCallback.onBarcodeManagerInitialised();
  }

  @Override
  public void error(String error) {
    mCallback.onBarcodeFailure();
  }
}
