package net.sprintasia.bayarind;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import net.sprintasia.bayarind.fragment.MainFragment;
import net.sprintasia.bayarind.listener.OnBayarindPaymentListener;
import net.sprintasia.bayarind.model.Transaction;

/**
 * Created by ops1 on 02/01/2018.
 */

public class BayarindPayment {

    private OnBayarindPaymentListener onBayarindPaymentListener;
    private Transaction transaction;

    public BayarindPayment(Context context, Transaction trx) {
        transaction = trx;
        try {
            final FragmentActivity activity = (FragmentActivity) context;
            MainFragment mainFragment = new MainFragment();
            Bundle args = new Bundle();
            args.putParcelable("transaction", transaction);
            mainFragment.setArguments(args);
            mainFragment.show(activity.getSupportFragmentManager(), "BAYARIND");
            mainFragment.setOnBayarindPaymentListener(new OnBayarindPaymentListener() {
                @Override
                public void onSuccess() {
                    onBayarindPaymentListener.onSuccess();
                }

                @Override
                public void onFailed() {
                    onBayarindPaymentListener.onFailed();
                }

                @Override
                public void onError() {
                    onBayarindPaymentListener.onError();
                }

                @Override
                public void onCancel() {
                    onBayarindPaymentListener.onCancel();
                }

                @Override
                public void onPermissionDenied() {
                    onBayarindPaymentListener.onPermissionDenied();
                }
            });
        }catch (ClassCastException  e){
            Log.e(Constant.TAG, "Can't get the fragment manager with given context");
            onBayarindPaymentListener.onError();
        }
    }

    //TODO Validate Transaction Data

    public void setOnBayarindPaymentListener(OnBayarindPaymentListener onBayarindPaymentListener){
        this.onBayarindPaymentListener = onBayarindPaymentListener;
    }
}
