package net.sprintasia.bayarind.instance;

import net.sprintasia.bayarind.listener.OnFragmentEvt;
import net.sprintasia.bayarind.model.CardToken;

/**
 * Created by ops1 on 18/01/2018.
 */

public class BayarindFragment {

    private static BayarindFragment bayarindFragment;
    private OnFragmentEvt listener;

    public static BayarindFragment getInstance(){
        if(bayarindFragment == null){
            bayarindFragment = new BayarindFragment();
        }
        return bayarindFragment;
    }

    public void setListener(OnFragmentEvt listener){
        this.listener = listener;
    }

    public void changePage(String pageName){
        listener.changePage(pageName);
    }

    public void setCashierCode(String cashierCode){
        listener.setCashierCode(cashierCode);
    }

    public void setPaymentAmount(int paymentAmount){
        listener.setPaymentAmount(paymentAmount);
    }

    public void setCardToken(CardToken token){
        listener.setToken(token);
    }

    public void pay(){
        listener.pay();
    }

    public void setPaymentResponse(String status, String message){
        listener.onPaymentResponse(status, message);
    }

    public void paymentDone(){
        listener.paymentDone();
    }

}
