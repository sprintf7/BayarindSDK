package net.sprintasia.bayarind.fragment;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import net.sprintasia.bayarind.R;
import net.sprintasia.bayarind.instance.BayarindFragment;

/**
 * Created by ops1 on 18/01/2018.
 */

public class QRScanFragment extends Fragment implements QRCodeReaderView.OnQRCodeReadListener {

    private QRCodeReaderView qrCodeReaderView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qr_scanner,
                container, false);

        qrCodeReaderView = (QRCodeReaderView) view.findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);
        qrCodeReaderView.setQRDecodingEnabled(true);
        qrCodeReaderView.setAutofocusInterval(2000L);
        qrCodeReaderView.setBackCamera();

        return view;
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        qrCodeReaderView.stopCamera();
        BayarindFragment.getInstance().setCashierCode(text);
        BayarindFragment.getInstance().changePage("DETAIL");
    }

    @Override
    public void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }
}
