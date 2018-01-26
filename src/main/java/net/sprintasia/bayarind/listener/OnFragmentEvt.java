package net.sprintasia.bayarind.listener;

import net.sprintasia.bayarind.model.CardToken;

/**
 * Created by ops1 on 18/01/2018.
 */

public interface OnFragmentEvt {
    void changePage(String pageName);
    void setCashierCode(String cashierCode);
    void setPaymentAmount(int paymentAmount);
    void setToken(CardToken token);
    void pay();
    void onPaymentResponse(String status, String message);
    void paymentDone();
}
