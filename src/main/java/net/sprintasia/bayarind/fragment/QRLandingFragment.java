package net.sprintasia.bayarind.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.sprintasia.bayarind.R;
import net.sprintasia.bayarind.instance.BayarindFragment;

/**
 * Created by ops1 on 18/01/2018.
 */

public class QRLandingFragment extends Fragment {

    private Button btnScan;
    private LinearLayout qrInfo;
    private ImageView qrLogo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.landing_qr,
                container, false);

        btnScan = view.findViewById(R.id.btn_scan);

        qrInfo = view.findViewById(R.id.qr_info);
        qrLogo = view.findViewById(R.id.qr_logo);
        qrInfo.setAlpha(0.0f);
        qrInfo.setTranslationX(1000);
        qrLogo.setAlpha(0.0f);
        qrLogo.setTranslationY(500);
        btnScan.setAlpha(0.0f);
        btnScan.setTranslationY(500);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BayarindFragment.getInstance().changePage("SCAN_QR");
            }
        });
        animteView();
        return view;
    }

    public void animteView(){
        qrInfo.animate()
                .translationX(0)
                .alpha(1.0f)
                .start();
        qrLogo.animate()
                .translationY(0)
                .alpha(1.0f)
                .start();
        btnScan.animate()
                .translationY(0)
                .alpha(1.0f)
                .start();
    }

}
