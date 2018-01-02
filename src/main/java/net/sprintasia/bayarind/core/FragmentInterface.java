package net.sprintasia.bayarind.core;

import net.sprintasia.bayarind.listener.OnBayarindPaymentListener;
import net.sprintasia.bayarind.listener.OnFragmentProcessListener;
import net.sprintasia.bayarind.model.CardInfo;

/**
 * Created by ops1 on 02/01/2018.
 */

public class FragmentInterface {
    private static FragmentInterface mInstance;
    private OnFragmentProcessListener mListener;

    private FragmentInterface(){}

    public static FragmentInterface getInstance(){
        if(mInstance == null) {
            mInstance = new FragmentInterface();
        }
        return mInstance;
    }

    public void setListener(OnFragmentProcessListener listener) {
        mListener = listener;
    }

    public void doPayment(){
        mListener.doPayment();
    }

    public void doProcess(CardInfo cardInfo){
        mListener.doProcess(cardInfo);
    }
}
