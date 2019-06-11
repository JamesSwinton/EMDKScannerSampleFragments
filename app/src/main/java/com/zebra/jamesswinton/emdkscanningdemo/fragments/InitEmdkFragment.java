package com.zebra.jamesswinton.emdkscanningdemo.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.symbol.emdk.EMDKManager.EMDKListener;
import com.zebra.jamesswinton.emdkscanningdemo.EMDKHelper;
import com.zebra.jamesswinton.emdkscanningdemo.MainActivity;
import com.zebra.jamesswinton.emdkscanningdemo.R;
import com.zebra.jamesswinton.emdkscanningdemo.databinding.FragmentInitEmdkBinding;

public class InitEmdkFragment extends Fragment implements EMDKHelper.ProgressCallback {

  // Debugging
  private static final String TAG = "InitEmdkFragment";

  // Constants


  // Static Variables


  // Non-Static Variables
  private FragmentInitEmdkBinding mDataBinding;
  private OnEmdkInitialisedListener mCallback;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater li, ViewGroup vg, Bundle savedInstanceState) {
    // Init DataBinding
    mDataBinding = DataBindingUtil.inflate(li, R.layout.fragment_init_emdk, vg, false);

    // Set Click Listener
    mDataBinding.getEmdkButton.setOnClickListener(v -> initEmdk());

    // Return View
    return mDataBinding.getRoot();
  }

  private void initEmdk() {
    MainActivity.mEmdkHelper.setListener(this);
    MainActivity.mEmdkHelper.initialiseEmdk(getContext());
  }

  public void setOnEmdkInitialisedListener(OnEmdkInitialisedListener callback) {
    mCallback = callback;
  }

  public interface OnEmdkInitialisedListener {
    void onEmdkInitialised();
    void onEmdkFailure();
  }

  @Override
  public void complete() {
    mCallback.onEmdkInitialised();
  }

  @Override
  public void error(String error) {
    mCallback.onEmdkFailure();
  }
}
