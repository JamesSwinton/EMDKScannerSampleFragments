package com.zebra.jamesswinton.emdkscanningdemo;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.zebra.jamesswinton.emdkscanningdemo.databinding.ActivityMainBinding;
import com.zebra.jamesswinton.emdkscanningdemo.fragments.ConfigureScannerFragment;
import com.zebra.jamesswinton.emdkscanningdemo.fragments.EnableScannerFragment;
import com.zebra.jamesswinton.emdkscanningdemo.fragments.InitBarcodeManagerFragment;
import com.zebra.jamesswinton.emdkscanningdemo.fragments.InitEmdkFragment;

public class MainActivity extends AppCompatActivity implements
    InitEmdkFragment.OnEmdkInitialisedListener,
    InitBarcodeManagerFragment.OnBarcodeManagerInitialised,
    EnableScannerFragment.OnScannerEnabledListener,
    ConfigureScannerFragment.OnScannerConfiguredListener {

  // Debugging
  private static final String TAG = "MainActivity";

  // Constants
  private static final int INIT_EMDK_PAGE = 0;
  private static final int INIT_BARCODE_MANAGER_PAGE = 1;
  private static final int ENABLE_SCANNER_PAGE = 2;
  private static final int CONFIGURE_SCANNER_PAGE = 3;
  private static final int DEMO_SCANNER_PAGE = 4;

  // Static Variables
  public static EMDKHelper mEmdkHelper;

  // Non-Static Variables
  private ActivityMainBinding mDataBinding;
  private FragmentPagerAdapter mFragmentPagerAdapter;

  @SuppressLint("ClickableViewAccessibility")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Init DataBinding
    mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

    // Init ViewPager
    mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager());
    mDataBinding.fragmentViewPager.setAdapter(mFragmentPagerAdapter);

    // Disable ViewPager Swiping
    mDataBinding.fragmentViewPager.setOnTouchListener((v, event) -> true);

    // Init ViewPager Indicator
    mDataBinding.stepIndicator.setViewPager(mDataBinding.fragmentViewPager);

    // Init EMDK Helper
    mEmdkHelper = new EMDKHelper();
  }

  @Override
  public void onAttachFragment(Fragment fragment) {
    // Init Callbacks
    if (fragment instanceof InitEmdkFragment) {
      InitEmdkFragment initEmdkFragment = (InitEmdkFragment) fragment;
      initEmdkFragment.setOnEmdkInitialisedListener(this);
    }

    if (fragment instanceof InitBarcodeManagerFragment) {
      InitBarcodeManagerFragment initBarcodeManagerFragment = (InitBarcodeManagerFragment) fragment;
      initBarcodeManagerFragment.setOnBarcodeManagerInitialisedListener(this);
    }

    if (fragment instanceof EnableScannerFragment) {
      EnableScannerFragment enableScannerFragment = (EnableScannerFragment) fragment;
      enableScannerFragment.setOnScannerEnabledListener(this);
    }

    if (fragment instanceof ConfigureScannerFragment) {
      ConfigureScannerFragment configureScannerFragment = (ConfigureScannerFragment) fragment;
      configureScannerFragment.setOnScannerConfiguredListener(this);
    }
//
//    if (fragment instanceof DemoScannerFragment) {
//      DemoScannerFragment demoScannerFragment = (DemoScannerFragment) fragment;
//      demoScannerFragment.setOnScannerDemoListener(this);
//    }
  }

  /**
   * EMDK Interface Callbacks
   */

  @Override
  public void onEmdkInitialised() {
    Toast.makeText(this, "EMDK Initialised Successfully!", Toast.LENGTH_LONG).show();
    mDataBinding.fragmentViewPager.setCurrentItem(INIT_BARCODE_MANAGER_PAGE);
  }

  @Override
  public void onEmdkFailure() {
    Toast.makeText(this, "EMDK Failed!", Toast.LENGTH_LONG).show();
  }

  /**
   * Barcode Manager Callbacks
   */

  @Override
  public void onBarcodeManagerInitialised() {
    Toast.makeText(this, "Barcode Manager Initialised Successfully!",
        Toast.LENGTH_LONG).show();
    mDataBinding.fragmentViewPager.setCurrentItem(ENABLE_SCANNER_PAGE);
  }

  @Override
  public void onBarcodeFailure() {
    Toast.makeText(this, "Barcode Manager Failed!", Toast.LENGTH_LONG).show();
  }

  /**
   * Scanner Enabled Callbacks
   */

  @Override
  public void onScannerEnabled() {
    Toast.makeText(this, "Scanner Enabled Successfully!",
        Toast.LENGTH_LONG).show();
    mDataBinding.fragmentViewPager.setCurrentItem(CONFIGURE_SCANNER_PAGE);
  }

  @Override
  public void onScannerFailure() {
    Toast.makeText(this, "Scanner Enable Failed!", Toast.LENGTH_LONG).show();
  }

  /**
   * Scanner Configured Callbacks
   */

  @Override
  public void onScannerConfigured() {
    Toast.makeText(this, "Scanner Configured Successfully!",
        Toast.LENGTH_LONG).show();
    mDataBinding.fragmentViewPager.setCurrentItem(DEMO_SCANNER_PAGE);
  }

  @Override
  public void onScannerConfigureFailure() {
    Toast.makeText(this, "Scanner Config Failed!", Toast.LENGTH_LONG).show();
  }

  @Override
  protected void onPause() {
    super.onPause();

    try {
      mEmdkHelper.disableScanner();
    } catch(Exception e) {
      Log.e(TAG, "ScanenrExeption: " + e.getMessage());
    }
  }
}
