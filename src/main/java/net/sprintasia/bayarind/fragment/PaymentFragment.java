package net.sprintasia.bayarind.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import net.sprintasia.bayarind.R;
import net.sprintasia.bayarind.core.BayarindInterface;
import net.sprintasia.bayarind.core.FragmentInterface;
import net.sprintasia.bayarind.lib.CardType;
import net.sprintasia.bayarind.lib.Helper;
import net.sprintasia.bayarind.model.CardInfo;
import net.sprintasia.bayarind.model.Transaction;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ops1 on 02/01/2018.
 */

public class PaymentFragment extends Fragment {

    private Transaction trx;

    private boolean CARDNUM_STATUS = false;
    private boolean CVC_STATUS = false;
    private boolean EXP_STATUS = false;

    private Button btnCancel;
    private Button btnPay;
    private AlertDialog.Builder builder;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private TextInputLayout expiryDateLayout;
    private AppCompatEditText expiryDate;

    private TextInputLayout cvcLayout;
    private AppCompatEditText cvc;

    private TextInputLayout cardNumberLayout;
    private AppCompatEditText cardNumber;

    private String EXP_MONTH;
    private String EXP_YEAR;
    private String CARD_TYPE;

    private TextView transactionNo_view;
    private TextView amount_view;
    private TextView customerName_view;
    private TextView customerEmail_view;


    private CoordinatorLayout coordinatorLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.payment, container,false);
        try{
            trx = getArguments().getParcelable("transaction");
        }catch (Exception e){
            BayarindInterface.getInstance().setError();
        }
        initView(rootView);
        return rootView;
    }

    private void submitData(){
        if(CARDNUM_STATUS && CVC_STATUS && EXP_STATUS) {
            CardInfo cardInfo = new CardInfo();
            cardInfo.setCardNo(cardNumber.getText().toString());
            cardInfo.setCvc(cvc.getText().toString());
            cardInfo.setCardType(CARD_TYPE);
            cardInfo.setExpMonth(EXP_MONTH);
            cardInfo.setExpYear(EXP_YEAR);
            FragmentInterface.getInstance().doProcess(cardInfo);
        }else{
            if(cardNumber.getText().toString().matches("")){
                cardNumberLayout.setErrorEnabled(true);
                cardNumberLayout.setError("Card Number is Required");
            }
            if(expiryDate.getText().toString().matches("")){
                expiryDateLayout.setErrorEnabled(true);
                expiryDateLayout.setError("Expiry Date is Required");
            }
            if(cvc.getText().toString().matches("")){
                cvcLayout.setErrorEnabled(true);
                cvcLayout.setError("Expiry Date is Required");
            }
            Snackbar.make(coordinatorLayout, R.string.payment_form_error, Snackbar.LENGTH_LONG).show();
        }
    }

    private void initView(View v){
        coordinatorLayout = v.findViewById(R.id.coordinatorLayout);
        collapsingToolbarLayout = v.findViewById(R.id.main_collapsing);
        collapsingToolbarLayout.setTitleEnabled(true);
        toolbar = (Toolbar) v.findViewById(R.id.main_toolbar);

        try {
            collapsingToolbarLayout.setTitle("Test Title");
            toolbar.setTitle(getResources().getString(R.string.payment_title));
        }catch (Exception ex){

        }

        builder = new AlertDialog.Builder(getActivity());
        btnCancel = v.findViewById(R.id.btnCancel);
        btnPay = v.findViewById(R.id.btnPay);

        expiryDate = v.findViewById(R.id.expiryDate);
        expiryDateLayout = v.findViewById(R.id.expiryDateLayout);

        cvc = v.findViewById(R.id.cvc);
        cvcLayout = v.findViewById(R.id.cvcLayout);
        cvc.addTextChangedListener(cvcWatcher());

        cardNumberLayout = v.findViewById(R.id.cardNumberLayout);
        cardNumber = v.findViewById(R.id.cardNumber);
        cardNumber.addTextChangedListener(cardNoWatcher());

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder
                .setMessage(getResources().getString(R.string.cancel_payment))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        BayarindInterface.getInstance().setCancel();
                        dialogInterface.dismiss();
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
        });

        expiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expDialog();
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });

        transactionNo_view = v.findViewById(R.id.transactionNo_view);
        amount_view = v.findViewById(R.id.amount_view);
        customerName_view = v.findViewById(R.id.customerName_view);
        customerEmail_view = v.findViewById(R.id.customerEmail_view);

        transactionNo_view.setText(trx.getTransactionNo());
        amount_view.setText(Helper.formatDecimal(trx.getAmount()));
        customerName_view.setText(trx.getCustomerName());
        customerEmail_view.setText(trx.getCustomerEmail());

    }

    private void expDialog(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.exp_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle("Expiration Date");

        final Spinner months = (Spinner) dialog.findViewById(R.id.month);
        final Spinner years = (Spinner) dialog.findViewById(R.id.year);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, getExpYears());
        years.setAdapter(adapter);

        Button dbtnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
        Button dbtnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        if(EXP_YEAR != null && EXP_MONTH != null){
            Helper.selectSpinnerItemByValue(years, EXP_YEAR);
            Helper.selectSpinnerItemByValue(months, EXP_MONTH);
            adapter.notifyDataSetChanged();
        }
        EXP_YEAR = "";
        EXP_MONTH = "";
        expiryDate.setText("");
        dbtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String m = months.getSelectedItem().toString();
                String y = years.getSelectedItem().toString();
                if(m != null && !m.equalsIgnoreCase("month") && y != null && !y.equalsIgnoreCase("year")){
                    expiryDateLayout.setErrorEnabled(false);
                    expiryDate.setText(m +" / "+y);
                    EXP_YEAR = y;
                    EXP_MONTH = m;
                    EXP_STATUS = true;
                }else{
                    expiryDateLayout.setErrorEnabled(true);
                    expiryDateLayout.setError("Invalid Card Expiry Date");
                    EXP_STATUS = false;
                }
                dialog.dismiss();
            }
        });
        dbtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                expiryDateLayout.setErrorEnabled(true);
                EXP_STATUS = false;
                expiryDateLayout.setError("Card Expiry Date is Required");
            }
        });
        dialog.show();
    }

    private ArrayList<String> getExpYears(){
        ArrayList<String> years = new ArrayList<String>();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        years.add("YEAR");
        years.add(Integer.toString(year));
        for (int i = 1; i <= 10; i++){
            years.add(Integer.toString(year+1));
            year++;
        }
        return years;
    }

    private TextWatcher cardNoWatcher(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                char space = ' ';
                // Remove spacing char
                if (s.length() > 0 && (s.length() % 5) == 0) {
                    final char c = s.charAt(s.length() - 1);
                    if (space == c) {
                        s.delete(s.length() - 1, s.length());
                    }
                }
                // Insert char where needed.
                if (s.length() > 0 && (s.length() % 5) == 0) {
                    char c = s.charAt(s.length() - 1);
                    // Only if its a digit where there should be a space we insert a space
                    if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
                        s.insert(s.length() - 1, String.valueOf(space));
                    }
                }
                if (s.length() > 0) {
                    String cardNum = s.toString().replaceAll("\\s+", "");
                    CARD_TYPE = CardType.detect(cardNum).toString().toUpperCase();
                    int rDrawer = 0;
                    if(cardNum.length() > 14) {
                        switch (CARD_TYPE) {
                            case "VISA":
                                rDrawer = R.drawable.bt_ic_visa;
                                break;
                            case "MASTERCARD":
                                rDrawer = R.drawable.bt_ic_mastercard;
                                break;
                            case "JCB":
                                rDrawer = R.drawable.bt_ic_jcb;
                                break;
                            case "AMERICAN_EXPRESS":
                                rDrawer = R.drawable.bt_ic_amex;
                                break;
                            default:
                                rDrawer = R.drawable.bt_ic_unknown;
                                break;
                        }
                        cardNumber.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_credit_card_black_24dp, 0, rDrawer, 0);
                    }
                    if(rDrawer ==0 || rDrawer == R.drawable.bt_ic_unknown){
                        cardNumberLayout.setErrorEnabled(true);
                        cardNumberLayout.setError("Invalid or Unsupported Card Number");
                        CARDNUM_STATUS = false;
                    }else{
                        cardNumberLayout.setErrorEnabled(false);
                        CARDNUM_STATUS = true;
                    }
                }else{
                    cardNumberLayout.setErrorEnabled(true);
                    CARDNUM_STATUS = false;
                    cardNumberLayout.setError("Card Number is Required");
                }
            }
        };
    }

    private TextWatcher cvcWatcher(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0){
                    cvcLayout.setErrorEnabled(true);
                    cvcLayout.setError("CVC is Required");
                    CVC_STATUS = false;
                }else{
                    if (!s.toString().matches("[0-9]+")){
                        cvcLayout.setErrorEnabled(true);
                        cvcLayout.setError("Invalid CVC Code");
                        CVC_STATUS = false;
                    }else{
                        cvcLayout.setErrorEnabled(false);
                        CVC_STATUS = true;
                    }
                }
            }
        };
    }

}
