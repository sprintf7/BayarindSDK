package net.sprintasia.bayarind.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;

import net.sprintasia.bayarind.Constant;
import net.sprintasia.bayarind.core.BayarindInterface;
import net.sprintasia.bayarind.core.FragmentInterface;
import net.sprintasia.bayarind.listener.OnBayarindPaymentListener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by ops1 on 02/01/2018.
 */

public class ScannerFragment extends Fragment implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    private int mCameraId = -1;

    @Override
    public void onCreate(Bundle state){
        super.onCreate(state);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mScannerView = new ZXingScannerView(getActivity());

        if (ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    Constant.CAMERE_REQ_CODE);
        }

        return mScannerView;
    }

    @Override
    public void handleResult(Result result) {
        if(result != null && result.getText() != null){
            FragmentInterface.getInstance().doPayment();
            mScannerView.stopCamera();
        }else{
            Toast.makeText(getContext(), "Cannot get data from QRCode, please rescan.", Toast.LENGTH_LONG).show();
            mScannerView.resumeCameraPreview(this);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if(mScannerView != null)
            mScannerView.stopCamera();
    }

    @Override
    public void onResume(){
        super.onResume();

        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
        mScannerView.setFlash(false);
        mScannerView.setAutoFocus(true);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mScannerView.stopCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.CAMERE_REQ_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(Constant.TAG, "CAMERA PERMISSION GRANTED");
                return;
            } else {
                BayarindInterface.getInstance().setPermissionDenied();
            }
        }
    }
}
