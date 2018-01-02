package net.sprintasia.bayarind.listener;

import net.sprintasia.bayarind.model.CardInfo;

/**
 * Created by ops1 on 02/01/2018.
 */

public interface OnFragmentProcessListener {
    void doPayment();
    void doProcess(CardInfo cardInfo);
}
