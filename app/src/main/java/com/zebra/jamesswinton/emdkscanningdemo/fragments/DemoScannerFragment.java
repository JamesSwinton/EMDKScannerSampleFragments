package com.zebra.jamesswinton.emdkscanningdemo.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.symbol.emdk.barcode.ScanDataCollection;
import com.zebra.jamesswinton.emdkscanningdemo.EMDKHelper;
import com.zebra.jamesswinton.emdkscanningdemo.MainActivity;
import com.zebra.jamesswinton.emdkscanningdemo.R;
import com.zebra.jamesswinton.emdkscanningdemo.databinding.FragmentDemoScannerBinding;

public class DemoScannerFragment extends Fragment implements EMDKHelper.ProgressCallback,
    EMDKHelper.DataCallback {

  // Debugging
  private static final String TAG = "DemoScannerFragment";

  // Constants


  // Static Variables


  // Non-Static Variables
  private FragmentDemoScannerBinding mDataBinding;

  public DemoScannerFragment() {}

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.i(TAG, "Creating Fragment...");
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater li, ViewGroup vg, Bundle savedInstanceState) {
    // Init DataBinding
    mDataBinding = DataBindingUtil.inflate(li, R.layout.fragment_demo_scanner, vg, false);

    // Set Click Listener
    mDataBinding.startScan.setOnClickListener(v -> startSoftScan());

    // Return View
    return mDataBinding.getRoot();
  }

  private void startSoftScan() {
    MainActivity.mEmdkHelper.setListener(this);
    MainActivity.mEmdkHelper.setDataListener(this);
    MainActivity.mEmdkHelper.startSoftScan();
  }

  @Override
  public void complete() {

  }

  @Override
  public void error(String error) {

  }

  @Override
  public void onData(ScanDataCollection scanDataCollection) {
    // Get Scanner Data as []
    ScanDataCollection.ScanData[] scannedData = scanDataCollection.getScanData().toArray(
        new ScanDataCollection.ScanData[scanDataCollection.getScanData().size()]);

    // Debugging
    for (ScanDataCollection.ScanData scanData : scannedData) {
      Log.i(TAG, "Label Type: " + scanData.getLabelType().name());
      Log.i(TAG, "Barcode: " + scanData.getData());
      Log.i(TAG, "Label Type: " + scanData.getLabelType().toString());
      mDataBinding.scanDataView.append(scanData.getData() + "\n");
    }
  }
}
