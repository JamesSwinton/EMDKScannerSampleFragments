package com.zebra.jamesswinton.emdkscanningdemo;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKManager.EMDKListener;
import com.symbol.emdk.EMDKManager.FEATURE_TYPE;
import com.symbol.emdk.barcode.BarcodeManager;
import com.symbol.emdk.barcode.BarcodeManager.DeviceIdentifier;
import com.symbol.emdk.barcode.ScanDataCollection;
import com.symbol.emdk.barcode.Scanner;
import com.symbol.emdk.barcode.Scanner.DataListener;
import com.symbol.emdk.barcode.Scanner.StatusListener;
import com.symbol.emdk.barcode.Scanner.TriggerType;
import com.symbol.emdk.barcode.ScannerConfig;
import com.symbol.emdk.barcode.ScannerConfig.PicklistEx;
import com.symbol.emdk.barcode.ScannerException;
import com.symbol.emdk.barcode.StatusData;
import com.zebra.jamesswinton.emdkscanningdemo.databinding.FragmentConfigureScannerBinding;
import com.zebra.jamesswinton.emdkscanningdemo.databinding.FragmentEnableScannerBinding;

public class EMDKHelper implements EMDKListener, DataListener, StatusListener {

  // Debugging
  private static final String TAG = "EMDKHelper";

  // Constants


  // Static Variables


  // Non-Static Variables
  private Scanner mScanner;
  private EMDKManager mEmdkManager;
  private BarcodeManager mBarcodeManager;

  private DataCallback mDataCallback;
  private ProgressCallback mCallback;

  public void setListener(ProgressCallback callback) {
    mCallback = callback;
  }

  public void setDataListener(DataCallback callback) { mDataCallback = callback; }

  /**
   * EMDK Utility Methods
   */

  public void initialiseEmdk(Context cx) {
    EMDKManager.getEMDKManager(cx, this);
  }

  public void getBarcodeManager() {
    if (mEmdkManager == null) {
      mCallback.error("Emdk Manager is Null");
      return;
    }

    // Get Barcode Manager
    mBarcodeManager = (BarcodeManager) mEmdkManager.getInstance(FEATURE_TYPE.BARCODE);

    if (mBarcodeManager != null) {
      mCallback.complete();
    } else {
      mCallback.error("Barcode Manager is Null");
    }
  }

  public void enableScanner() {
    if (mBarcodeManager == null) {
      mCallback.error("Barcode Manager is Null");
      return;
    }

    // Get Scanner
    mScanner = mBarcodeManager.getDevice(DeviceIdentifier.DEFAULT);
    mScanner.addDataListener(this);
    mScanner.addStatusListener(this);

    // Enable Scanner
    try {
      mScanner.enable();
      mCallback.complete();
    } catch (ScannerException e) {
      Log.e(TAG, "ScannerException: " + e.getMessage());
      mCallback.error("ScannerException: " + e.getMessage());
    }
  }

  public void configureScanner(FragmentConfigureScannerBinding binding) {
    if (mScanner == null) {
      mCallback.error("Scanner is Null!");
      return;
    }

    try {
      ScannerConfig scannerConfig = mScanner.getConfig();
      scannerConfig.scanParams.decodeAudioFeedbackUri = binding.auditoryFeedback.isChecked() ? "system/media/audio/notifications/decode-short.wav" : "";
      scannerConfig.scanParams.decodeHapticFeedback = binding.hapticFeedback.isChecked();
      scannerConfig.decoderParams.code128.enabled = binding.code128.isChecked();
      scannerConfig.decoderParams.code39.enabled = binding.code39.isChecked();
      scannerConfig.decoderParams.qrCode.enabled = binding.codeQr.isChecked();
      scannerConfig.decoderParams.upca.enabled = binding.codeUpca.isChecked();
      mScanner.setConfig(scannerConfig);
      mCallback.complete();
    } catch (ScannerException e) {
      Log.e(TAG, "ScannerException: " + e.getMessage());
      mCallback.error("ScannerException: " + e.getMessage());
    }
  }

  public void startSoftScan() {
    try {
      mScanner.triggerType = TriggerType.SOFT_ONCE;
      if (mScanner.isReadPending()) {
        mScanner.cancelRead();
      } mScanner.read();
    } catch (ScannerException e) {
      Log.e(TAG, "ScannerException: " + e.getMessage());
    }
  }

  /**
   * EMDK Callbacks
   */

  @Override
  public void onOpened(EMDKManager emdkManager) {
    Log.i(TAG, "EMDKManager Opened...");
    mEmdkManager = emdkManager;
    mCallback.complete();
  }

  @Override
  public void onClosed() {
    if (mEmdkManager != null) {
      mEmdkManager.release();
      mEmdkManager = null;
    }

    Log.i(TAG, "EMDKManager Closed...");
    mCallback.error("EMDKManager Closed!");
  }

  /**
   * Interface Callback
   */

  public interface ProgressCallback {
    void complete();
    void error(String error);
  }

  public interface DataCallback {
    void onData(ScanDataCollection scanDataCollection);
  }

  /**
   * Data / Status Listeners
   */

  @Override
  public void onData(ScanDataCollection scanDataCollection) {
    mDataCallback.onData(scanDataCollection);
  }

  @Override
  public void onStatus(StatusData statusData) {
    switch (statusData.getState()) {
      case IDLE:
        enableScannerReadWithDelay();
        break;
      case WAITING:
        Log.i(TAG, "Scanner waiting...");
        break;
      case SCANNING:
        Log.i(TAG, "Scanner scanning...");
        break;
      case DISABLED:
        Log.i(TAG, "Scanner Disabled...");
        break;
      case ERROR:
        Log.i(TAG, "Scanner Error!");
        break;
      default:
        break;
    }
  }

  /**
   * Utility method to re-enable scanner when idle with pre-defined delay
   */
  private void enableScannerReadWithDelay() {
    new Handler().postDelayed(() -> {
      try {
        mScanner.read();
      } catch (ScannerException e) {
        Log.e(TAG, "ScannerException: " + e.getMessage());
      }
    }, 250);
  }

  /**
   * Utility Method to disable scanner
   */

  public void disableScanner() throws ScannerException {
    // Check Scanner is Available
    if (mScanner != null) {
      // Cancel Pending Read
      mScanner.cancelRead();
      // Disable Scanner
      mScanner.disable();
      //Remove Interfaces
      mScanner.removeDataListener(this);
      mScanner.removeStatusListener(this);
      // Release Scanner
      mScanner.release();
      // Remove Member Variable
      mScanner = null;
    }
  }
}
