package com.zebra.jamesswinton.emdkscanningdemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.zebra.jamesswinton.emdkscanningdemo.fragments.ConfigureScannerFragment;
import com.zebra.jamesswinton.emdkscanningdemo.fragments.DemoScannerFragment;
import com.zebra.jamesswinton.emdkscanningdemo.fragments.EnableScannerFragment;
import com.zebra.jamesswinton.emdkscanningdemo.fragments.InitBarcodeManagerFragment;
import com.zebra.jamesswinton.emdkscanningdemo.fragments.InitEmdkFragment;

public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

  // Debugging
  private static final String TAG = "FragmentPageAdapter";

  // Constants


  // Static Variables


  // Non-Static Variables

  public FragmentPagerAdapter(FragmentManager fragmentManager) {
    super(fragmentManager);
  }

  @Override
  public Fragment getItem(int position) {
    // Change Page
    switch (position) {
      case 0:
        return new InitEmdkFragment();
      case 1:
        return new InitBarcodeManagerFragment();
      case 2:
        return new EnableScannerFragment();
      case 3:
        return new ConfigureScannerFragment();
      case 4:
        return new DemoScannerFragment();
      default:
        return null;
    }
  }

  @Override
  public int getCount() {
    return 5;
  }
}
