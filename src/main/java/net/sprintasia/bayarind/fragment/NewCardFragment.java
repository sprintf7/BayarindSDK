package net.sprintasia.bayarind.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.sprintasia.bayarind.R;
import net.sprintasia.bayarind.instance.BayarindFragment;
import net.sprintasia.bayarind.lib.CardType;
import net.sprintasia.bayarind.lib.Helper;
import net.sprintasia.bayarind.model.CardToken;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ops1 on 18/01/2018.
 */

public class NewCardFragment extends Fragment {

    private View view;
    private CardToken cardToken;
    private String CARD_TYPE;

    private EditText cardNo;
    private Spinner expMonth;
    private Spinner expYear;
    private EditText cvc;
    private EditText customerName;
    private EditText customerEmail;
    private TextView token;
    private boolean IS_USE_TOKEN = false;
    private String CARD_TOKEN;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_card,
                container, false);
        this.view = view;

        cardNo = view.findViewById(R.id.card_no);
        expMonth = view.findViewById(R.id.exp_month);
        expYear = view.findViewById(R.id.exp_year);
        cvc = view.findViewById(R.id.cvc);
        customerName = view.findViewById(R.id.customer_name);
        customerEmail = view.findViewById(R.id.customer_email);
        token = view.findViewById(R.id.token);
        token.setVisibility(View.GONE);

        RelativeLayout blockForm = view.findViewById(R.id.block_form);
        blockForm.setTranslationY(1500);
        blockForm.animate().translationY(0).start();

        cardNo.addTextChangedListener(cardNoWatcher());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, getExpYears());
        expYear.setAdapter(adapter);

        Button btnPay = view.findViewById(R.id.btn_pay);
        btnPay.setTranslationY(500);
        btnPay.animate().translationY(0).start();
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkForm()) {
                    insertPayment();
                }else{
                    Toast.makeText(getContext(), "Opps, there are some error in your card data.", Toast.LENGTH_LONG).show();
                }
            }
        });

        try {
            cardToken = getArguments().getParcelable("token");
            if(cardToken != null){
                token.setVisibility(View.VISIBLE);
                cardNo.setVisibility(View.GONE);
                cardNo.setEnabled(false);
                CARD_TOKEN = cardToken.getToken();
                CARD_TYPE = cardToken.getCardType();
                int rDrawer = getCardTypeIcon(cardToken.getCardType());
                token.setText(cardToken.getMaskCardNo());
                token.setCompoundDrawablesWithIntrinsicBounds(0, 0, rDrawer, 0);
                customerName.setText(cardToken.getCustomerName());
                customerEmail.setText(cardToken.getCustomerEmail());
                String exp = cardToken.getExpDate();
                if(exp != null){
                    String[] e = exp.split("\\|");
                    int yPos = adapter.getPosition(e[0]);
                    expYear.setSelection(yPos);
                    expMonth.setSelection(((ArrayAdapter<String>) expMonth.getAdapter()).getPosition(e[1]));
                    expYear.setEnabled(false);
                    expMonth.setEnabled(false);
                }
                IS_USE_TOKEN = true;
                cvc.requestFocus();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }

    public boolean checkForm(){
        boolean check = true;
        String sCCNo = cardNo.getText().toString().replaceAll("[^\\d.]", "");
        cardNo.setError(null);
        cvc.setError(null);
        customerName.setError(null);
        customerEmail.setError(null);
        if(!IS_USE_TOKEN) {
            if (sCCNo == null || sCCNo.matches("")) {
                cardNo.setError("Opps, please input your credit card number.");
                check = false;
                cardNo.requestFocus();
            }
            if (!Helper.luhnCheck(sCCNo)) {
                cardNo.setError("Opps, Invalid card number.");
                check = false;
                cardNo.requestFocus();
            }
        }
        if(cvc.getText().toString().matches("")){
            cvc.setError("Opps, please input your card CVC.");
            check = false;
            cvc.requestFocus();
        }

        if(customerName.getText().toString() == null || customerName.getText().toString().matches("")){
            customerName.setError("Opps, please input your name.");
            check = false;
            customerName.requestFocus();
        }
        if(customerEmail.getText().toString() == null || customerEmail.getText().toString().matches("")){
            customerEmail.setError("Opps, please input your email.");
            check = false;
            customerEmail.requestFocus();
        }
        if(!Helper.isValidEmail(customerEmail.getText().toString())){
            customerEmail.setError("Opps, Invalid email.");
            check = false;
            customerEmail.requestFocus();
        }
        if(expMonth.getSelectedItem().toString() == null || expMonth.getSelectedItem().toString().equalsIgnoreCase("month")){
            TextView errorText = (TextView) expMonth.getSelectedView();
            errorText.setError("anything here, just to add the icon");
            errorText.setTextColor(Color.RED);
            errorText.setText("Please select expiry date month");
            check = false;
        }
        if(expYear.getSelectedItem().toString() == null || expYear.getSelectedItem().toString().equalsIgnoreCase("year")){
            TextView errorText = (TextView) expYear.getSelectedView();
            errorText.setError("anything here, just to add the icon");
            errorText.setTextColor(Color.RED);
            errorText.setText("Please select expiry date month");
            check = false;
        }


        return check;

    }

    public void insertPayment(){
        cardToken = new CardToken();
        String cardNum = cardNo.getText().toString().toString().replaceAll("\\s+", "");
        cardToken.setCardNo(cardNum);
        if(IS_USE_TOKEN == true && CARD_TOKEN != null){
            cardToken.setToken(CARD_TOKEN);
        }
        String exp = expMonth.getSelectedItem().toString()+"|"+expYear.getSelectedItem().toString();
        cardToken.setExpDate(exp);
        cardToken.setCardType(CARD_TYPE);
        cardToken.setCvc(cvc.getText().toString());
        cardToken.setCustomerName(customerName.getText().toString());
        cardToken.setCustomerEmail(customerEmail.getText().toString());
        BayarindFragment.getInstance().setCardToken(cardToken);
        BayarindFragment.getInstance().pay();
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
                    int rDrawer = getCardTypeIcon(CARD_TYPE);
                    cardNo.setError(null);
                    cardNo.setCompoundDrawables(null, null, null, null);
                    if(rDrawer == R.drawable.bt_ic_unknown){
                        cardNo.setError("Invalid or Unsupported Card Number");
                    }else{
                        cardNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, rDrawer, 0);
                    }
                }else{
                    cardNo.setError("Card Number is Required");
                }
            }
        };
    }

    private int getCardTypeIcon(String cardType){
        switch (cardType.toUpperCase()) {
            case "VISA":
                return R.drawable.bt_ic_visa;
            case "MASTERCARD":
                return R.drawable.bt_ic_mastercard;
            case "JCB":
                return R.drawable.bt_ic_jcb;
            case "AMEX":
                return R.drawable.bt_ic_amex;
            default:
                return R.drawable.bt_ic_unknown;
        }
    }

    private ArrayList<String> getExpYears(){
        ArrayList<String> years = new ArrayList<>();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        years.add("Year");
        years.add(Integer.toString(year));
        for (int i = 1; i <= 20; i++){
            years.add(Integer.toString(year+1));
            year++;
        }
        return years;
    }
}
