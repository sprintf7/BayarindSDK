package net.sprintasia.bayarind.fragment;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import net.sprintasia.bayarind.R;
import net.sprintasia.bayarind.instance.BayarindFragment;
import net.sprintasia.bayarind.lib.ApiCall;
import net.sprintasia.bayarind.lib.Helper;
import net.sprintasia.bayarind.listener.OnApiListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ops1 on 18/01/2018.
 */

public class DetailFragment extends Fragment {

    private String cashierCode;
    private JSONObject cashierData;
    private View view;
    private EditText amount;
    private LinearLayout merchantBlock;
    private LinearLayout amountField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_amount,
                container, false);
        this.view = view;

        merchantBlock = view.findViewById(R.id.merchant_block);
        amountField = view.findViewById(R.id.amount_field);

        cashierCode = getArguments().getString("cashier_code");
        if(cashierCode == null){
            invalidData("Sorry cannot get data from last scanned QR Code.");
        }else{
            amount = view.findViewById(R.id.trans_amount);
            LinearLayout detailLayout = view.findViewById(R.id.detail_layout);
            detailLayout.setVisibility(View.GONE);
            Button btnPay = view.findViewById(R.id.btn_pay);
            btnPay.setVisibility(View.GONE);
            ApiCall api = new ApiCall("mobile/cashier/"+cashierCode, "GET");
            api.send(new OnApiListener() {
                @Override
                public void onSuccess(Boolean status, String message, JSONObject data) {
                    if(status) {
                        cashierData = data;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setMerchantData();
                            }
                        });
                    }else{
                        invalidData(message);
                    }
                }

                @Override
                public void onSuccess(Boolean status, String message, JSONArray data) {
                    if(!status && message != null)
                        invalidData(message);
                    else
                        invalidData("Invalid response from payment server.");
                }

                @Override
                public void onSuccess(Boolean status, String message, String data) {
                    if(!status && message != null)
                        invalidData(message);
                    else
                        invalidData("Invalid response from payment server.");
                }

                @Override
                public void onFailed() {
                    invalidData("Opps, something error when connecting to payment server.");
                }

            });
        }
        return view;
    }

    private void setMerchantData(){
        final ProgressBar progress = view.findViewById(R.id.progress);
        final LinearLayout detailLayout = view.findViewById(R.id.detail_layout);

        final TextView merchantName = view.findViewById(R.id.merchant_name);
        TextView branchName = view.findViewById(R.id.branch_name);
        TextView cashierName = view.findViewById(R.id.cashier_name);
        TextView categoryName = view.findViewById(R.id.category_name);


        final Button btnPay = view.findViewById(R.id.btn_pay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sAmount = amount.getText().toString();
                if(sAmount != null && Helper.isNumeric(sAmount)){
                    try {
                        int iAmount = Integer.parseInt(sAmount);
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(amount.getWindowToken(), 0);
                        BayarindFragment.getInstance().setPaymentAmount(iAmount);
                        BayarindFragment.getInstance().changePage("CARD_LIST");
                    }catch (NullPointerException e){
                        e.printStackTrace();
                        amount.setError("Opss, Invalid amount.");
                    }
                }else{
                    amount.setError("Opss, please input payment amount.");
                }
            }
        });

        try {
            merchantName.setText(cashierData.getJSONObject("branch").getJSONObject("merchant").getString("merchant_name"));
            branchName.setText(cashierData.getJSONObject("branch").getString("branch_name"));
            cashierName.setText(cashierData.getString("name"));
            categoryName.setText(cashierData.getJSONObject("branch").getJSONObject("merchant").getJSONObject("category").getString("category_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        progress.animate()
                .alpha(0f)
                .setDuration(700)
                .setInterpolator(new LinearInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        merchantBlock.setTranslationY(-1000);
                        amountField.setTranslationY(400);
                        btnPay.setTranslationY(400);

                        progress.setVisibility(View.GONE);
                        detailLayout.setVisibility(View.VISIBLE);
                        btnPay.setVisibility(View.VISIBLE);

                        merchantBlock.animate().translationY(0).start();
                        amountField.animate().translationY(0).start();
                        btnPay.animate().translationY(0).start();

                        amount.requestFocus();
                        try {
                            InputMethodManager imm = (InputMethodManager)   getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
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

    private void invalidData(final String message){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                BayarindFragment.getInstance().changePage("SCAN_QR");
            }
        });
    }
}
