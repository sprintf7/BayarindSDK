package net.sprintasia.bayarind.core;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import net.sprintasia.bayarind.Constant;
import net.sprintasia.bayarind.fragment.PaymentFragment;
import net.sprintasia.bayarind.fragment.ProcessFragment;
import net.sprintasia.bayarind.fragment.ScannerFragment;

/**
 * Created by ops1 on 02/01/2018.
 */

public class BayarindFragmentAdapter extends FragmentStatePagerAdapter {

    public BayarindFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.i(Constant.TAG, "Current Fragment Position: "+position);
        if(position == 1)
            return new ScannerFragment();
        else if(position == 2)
            return new PaymentFragment();
        else if(position == 3)
            return new ProcessFragment();
        else
            return new ScannerFragment();
    }

    @Override
    public int getCount() {
        return 3;
    }
}
