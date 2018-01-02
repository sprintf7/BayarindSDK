package net.sprintasia.bayarind.fragment;


import android.Manifest;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import net.sprintasia.bayarind.Constant;
import net.sprintasia.bayarind.R;
import net.sprintasia.bayarind.core.BayarindFragmentAdapter;
import net.sprintasia.bayarind.core.BayarindInterface;
import net.sprintasia.bayarind.core.FragmentInterface;
import net.sprintasia.bayarind.listener.OnBayarindPaymentListener;
import net.sprintasia.bayarind.listener.OnFragmentProcessListener;
import net.sprintasia.bayarind.model.CardInfo;
import net.sprintasia.bayarind.model.Transaction;

/**
 * Created by ops1 on 02/01/2018.
 */

public class MainFragment extends DialogFragment implements OnBayarindPaymentListener, OnFragmentProcessListener {

    private View view;
    private BayarindFragmentAdapter mPagerAdapter;
    private AlertDialog.Builder builder;
    private Transaction transaction;

    private Bundle bundle;

    private Fragment fragment;

    private OnBayarindPaymentListener onBayarindPaymentListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        builder = new AlertDialog.Builder(getActivity());
        try{
            transaction = getArguments().getParcelable("transaction");
        }catch (Exception e){
            dismiss();
            onBayarindPaymentListener.onError();
        }
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main, container);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);

        BayarindInterface.getInstance().setListener(this);
        FragmentInterface.getInstance().setListener(this);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels);
        int screenHeight = (int) (metrics.heightPixels);

        getDialog().getWindow().setLayout(screenWidth, screenHeight);
        fragment = new ScannerFragment();
        changePage();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()){
            @Override
            public void onBackPressed() {
                builder
                .setMessage(getResources().getString(R.string.cancel_payment))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        dismiss();
                        onBayarindPaymentListener.onCancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
            }
        };
    }

    public void changePage(){
        if(fragment != null)
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment).commit();

    }

    public void setOnBayarindPaymentListener(OnBayarindPaymentListener onBayarindPaymentListener){
        this.onBayarindPaymentListener = onBayarindPaymentListener;
    }

    @Override
    public void onSuccess() {
        dismiss();
        onBayarindPaymentListener.onSuccess();
    }

    @Override
    public void onFailed() {
        dismiss();
        onBayarindPaymentListener.onFailed();
    }

    @Override
    public void onError() {
        onBayarindPaymentListener.onError();
        dismiss();
    }

    @Override
    public void onCancel() {
        dismiss();
        onBayarindPaymentListener.onCancel();
    }

    @Override
    public void onPermissionDenied() {
        dismiss();
        onBayarindPaymentListener.onPermissionDenied();
    }

    @Override
    public void doPayment() {
        fragment = new PaymentFragment();
        bundle = new Bundle();
        bundle.putParcelable("transaction", transaction);
        fragment.setArguments(bundle);
        changePage();
    }

    @Override
    public void doProcess(CardInfo cardInfo) {
        fragment = new ProcessFragment();
        bundle = new Bundle();
        bundle.putParcelable("cardinfo", cardInfo);
        fragment.setArguments(bundle);
        changePage();
    }
}
