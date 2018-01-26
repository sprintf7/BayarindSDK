package net.sprintasia.bayarind.activity;

import android.Manifest;
import android.animation.Animator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import net.sprintasia.bayarind.R;
import net.sprintasia.bayarind.fragment.CardListFragment;
import net.sprintasia.bayarind.fragment.DetailFragment;
import net.sprintasia.bayarind.fragment.NewCardFragment;
import net.sprintasia.bayarind.fragment.QRLandingFragment;
import net.sprintasia.bayarind.fragment.QRScanFragment;
import net.sprintasia.bayarind.fragment.ResponseFragment;
import net.sprintasia.bayarind.fragment.WebViewFragment;
import net.sprintasia.bayarind.instance.BayarindFragment;
import net.sprintasia.bayarind.instance.BayarindPayProcess;
import net.sprintasia.bayarind.lib.ApiCall;
import net.sprintasia.bayarind.listener.OnApiListener;
import net.sprintasia.bayarind.listener.OnFragmentEvt;
import net.sprintasia.bayarind.model.CardToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ops1 on 17/01/2018.
 */

public class BayarindPayActivity extends AppCompatActivity implements OnFragmentEvt {

    ProgressDialog progressDialog;
    private static final int CAMERA_PERMISSION_CODE = 101;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private ScrollView mainView;

    private Bundle bundle;
    private String cashierCode;
    private int paymentAmount;
    private CardToken cardToken;
    private String paymentCode;
    private String redirectURL;
    private String redirectData;
    private String customerId;
    private String customerPhone;
    private String paymentStatus;
    private String paymentMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        if(getActionBar() != null) {
            getActionBar().hide();
        }
        try {
            customerId = getIntent().getExtras().getString("customer_id");
            customerPhone = getIntent().getExtras().getString("customer_phone");

            setContentView(R.layout.main_layout);
            mainView = findViewById(R.id.main_view);
            mainView.setVisibility(View.GONE);
            progressDialog = new ProgressDialog(BayarindPayActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            checkCameraPermission();
        }catch (NullPointerException e){
            e.printStackTrace();
            BayarindPayProcess.getInstance().onError("Opps, something error when setuping your data");
        }
    }

    private void checkCameraPermission(){
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP) {
            if (!checkIfAlreadyhavePermission()) {
                requestForSpecificPermission();
            }else{
                startPayment();
            }
        }else{
            startPayment();
        }
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        return (result == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startPayment();
                } else {
                    Toast.makeText(BayarindPayActivity.this, "You need to granted camera permission to process your payment.", Toast.LENGTH_LONG).show();
                    BayarindPayProcess.getInstance().onPermissionDenied();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void startPayment(){
        BayarindFragment.getInstance().setListener(this);
        ImageView flashLogo = findViewById(R.id.flash_logo);

        flashLogo.animate()
                .alpha(0f)
                .translationY(flashLogo.getHeight())
                .setDuration(1000)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        mainView.setVisibility(View.VISIBLE);
                        changePage("LANDING_QR");
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .start();

    }

    @Override
    public void changePage(String pageName) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = null;
        String fragmentName = "";
        switch (pageName){
            case "LANDING_QR":
                fragment = new QRLandingFragment();
//                fragmentTransaction.addToBackStack("LANDING_QR");
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentName = "LANDING_QR";
                break;
            case "SCAN_QR":
                fragment = new QRScanFragment();
                fragmentTransaction.addToBackStack("SCAN_QR");
                fragmentName = "SCAN_QR";
                break;
            case "DETAIL":
                fragment = new DetailFragment();
                bundle = new Bundle();
                bundle.putString("cashier_code", cashierCode);
                fragment.setArguments(bundle);
                fragmentTransaction.addToBackStack("DETAIL");
                fragmentName = "DETAIL";
                break;
            case "CARD_LIST":
                fragment = new CardListFragment();
                bundle = new Bundle();
                bundle.putString("customer_id", customerId);
                fragment.setArguments(bundle);
                fragmentTransaction.addToBackStack("CARD_LIST");
                fragmentName = "CARD_LIST";
                break;
            case "NEW_CARD":
                fragment = new NewCardFragment();
                bundle = new Bundle();
                bundle.putParcelable("token", null);
                fragment.setArguments(bundle);
                fragmentTransaction.addToBackStack("NEW_CARD");
                fragmentName = "NEW_CARD";
                break;
            case "TOKEN_DETAIL":
                fragment = new NewCardFragment();
                bundle = new Bundle();
                bundle.putParcelable("token", cardToken);
                fragment.setArguments(bundle);
                fragmentTransaction.addToBackStack("TOKEN_DETAIL");
                fragmentName = "TOKEN_DETAIL";
                break;
            case "WEBVIEW":
                fragment = new WebViewFragment();
                bundle = new Bundle();
                bundle.putString("payment_code", paymentCode);
                bundle.putString("redirect_url", redirectURL);
                bundle.putString("redirect_data", redirectData);
                fragment.setArguments(bundle);
                fragmentName = "WEBVIEW";
                break;
            case "RESPONSE":
                fragment = new ResponseFragment();
                bundle = new Bundle();
                bundle.putString("payment_status", paymentStatus);
                bundle.putString("payment_message", paymentMessage);
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragment.setArguments(bundle);
                fragmentName = "RESPONSE";
                break;
            default:
                fragment = new QRLandingFragment();
                fragmentName = "LANDING_QR";
                break;
        }
        if(fragment != null) {
            fragmentTransaction.replace(R.id.main_frame, fragment, fragmentName);
            fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void setCashierCode(String cashierCode) {
        this.cashierCode = cashierCode;
    }

    @Override
    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    @Override
    public void setToken(CardToken token) {
        this.cardToken = token;
    }

    @Override
    public void pay() {
        progressDialog.setMessage("Registering your payment...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiCall insert = new ApiCall("mobile/pay/" + cashierCode, "POST");

        HashMap<String, String> params = new HashMap<>();
        params.put("amount", Integer.toString(paymentAmount));
        params.put("custName", cardToken.getCustomerName());
        params.put("custPhone", customerPhone);
        params.put("custAccount", customerId);
        params.put("custEmail", cardToken.getCustomerEmail());
        params.put("cardNo", cardToken.getCardNo());
        params.put("cardToken", cardToken.getToken());
        params.put("cardType", cardToken.getCardType());
        params.put("cvv", cardToken.getCvc());
        String[] split = cardToken.getExpDate().split("\\|");
        params.put("cardExpMonth", split[0]);
        params.put("cardExpYear", split[1]);

        insert.setParams(params);
        insert.send(new OnApiListener() {
            @Override
            public void onSuccess(Boolean status, final String message, JSONObject data) {
                if(status) {
                    try {
                        paymentCode = data.getString("paymentCode");
                        JSONObject dataRedirect = data.getJSONObject("data");
                        redirectURL = dataRedirect.getString("redirectURL");
                        redirectData = dataRedirect.getString("redirectData");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                changePage("WEBVIEW");
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                    }
                                }, 1500);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(BayarindPayActivity.this, "Opps, something error when parsing data from payment server.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(BayarindPayActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onSuccess(final Boolean status, final String message, JSONArray data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        String m = (!status && message != null) ? message : "Invalid payment server response.";
                        Toast.makeText(BayarindPayActivity.this, m, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onSuccess(final Boolean status, final String message, String data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        String m = (!status && message != null) ? message : "Invalid payment server response.";
                        Toast.makeText(BayarindPayActivity.this, m, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailed() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    Toast.makeText(BayarindPayActivity.this, "Opps, error when communicating to payment server.", Toast.LENGTH_LONG).show();                    }
            });
            }
        });
    }

    @Override
    public void onPaymentResponse(String status, String message) {
        this.paymentStatus = status;
        this.paymentMessage = message;
        changePage("RESPONSE");
    }

    @Override
    public void paymentDone() {
        finish();
    }

    @Override
    public void onBackPressed() {

        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount == 0) {
            Fragment myFragment = getSupportFragmentManager().findFragmentByTag("RESPONSE");
            if (myFragment != null && myFragment.isVisible()) {
                if(paymentStatus.equalsIgnoreCase("00")){
                    BayarindPayProcess.getInstance().onSuccess();
                }else if(paymentStatus.equalsIgnoreCase("02")){
                    String m = (paymentMessage != null) ? paymentMessage : "Opps, your payment declined by payment server.";
                    BayarindPayProcess.getInstance().onFailed(m);
                }else{
                    BayarindPayProcess.getInstance().onError(paymentMessage);
                }
                finish();
            }else{
                exit();
            }
        } else {
            Fragment qrFragment = getSupportFragmentManager().findFragmentByTag("SCAN_QR");
            if (qrFragment != null && qrFragment.isVisible()) {
                exit();
            }else {
                super.onBackPressed();
            }
        }
    }

    private void exit(){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(BayarindPayActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(BayarindPayActivity.this);
        }
        builder.setTitle("Cancel Payment")
            .setMessage("Are you sure you want to cancel this payment?")
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    BayarindPayProcess.getInstance().onCancel();
                    finish();
                }
            })
            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                }
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }

}
