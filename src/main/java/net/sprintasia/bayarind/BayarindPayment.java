package net.sprintasia.bayarind;

import android.content.Context;
import android.content.Intent;

import net.sprintasia.bayarind.activity.BayarindPayActivity;
import net.sprintasia.bayarind.instance.BayarindPayProcess;
import net.sprintasia.bayarind.listener.OnBayarindPay;

/**
 * Created by ops1 on 17/01/2018.
 */

public class BayarindPayment implements OnBayarindPay {

    private Context context;
    private OnBayarindPay listener;
    private String customerId;
    private String phone;

    public BayarindPayment(Context context){
        this.context =context;
        BayarindPayProcess.getInstance().setListener(this);
    }

    public BayarindPayment(Context context, String customerId, String phone) {
        this.context = context;
        this.customerId = customerId;
        this.phone = phone;
        BayarindPayProcess.getInstance().setListener(this);
    }

    public void setCustomerId(String customerId){
        this.customerId = customerId;
    }

    public void setCustomerPhone(String phone){
        this.phone = phone;
    }

    public void show(){
        if(check()) {
            Intent i = new Intent(context, BayarindPayActivity.class);
            i.putExtra("customer_id", customerId);
            i.putExtra("customer_phone", phone);
            context.startActivity(i);
        }
    }

    private boolean check(){
        if(customerId == null || customerId.equalsIgnoreCase("") || customerId.matches("") || phone.matches("") || phone.equalsIgnoreCase("") || phone == null){
            String m = "customer id and phone cannot be null.";
            if(listener != null)
                listener.onFailed(m);
            else
                new IllegalArgumentException(m);
            return false;
        }
        if(!customerId.matches("[A-Za-z0-9]*") || customerId.length() > 24){
            String m = "Invalid customer id (Must be alphanumeric and max length 24 char.";
            if(listener != null)
                listener.onFailed(m);
            else
                new IllegalArgumentException(m);
            return false;
        }
        if(!phone.matches("[0-9]*") || phone.length() > 20){
            String m = "Invalid phone number (Must be numeric and max length 20 char.";
            if(listener != null)
                listener.onFailed(m);
            else
                new IllegalArgumentException(m);

            return false;
        }
        return true;
    }

    public void setOnBayarindPayListener(OnBayarindPay listener) {
        this.listener =  listener;
    }

    @Override
    public void onCancel() {
        listener.onCancel();
    }

    @Override
    public void onPermissionDenied() {
        listener.onPermissionDenied();
    }

    @Override
    public void onSuccess() {
        listener.onSuccess();
    }

    @Override
    public void onFailed(String message) {
        listener.onFailed(message);
    }

    @Override
    public void onError(String message) {
        listener.onError(message);
    }

}
