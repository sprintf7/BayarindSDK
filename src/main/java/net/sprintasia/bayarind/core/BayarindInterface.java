package net.sprintasia.bayarind.core;

import net.sprintasia.bayarind.listener.OnBayarindPaymentListener;

/**
 * Created by ops1 on 02/01/2018.
 */

public class BayarindInterface {
    private static BayarindInterface mInstance;
    private OnBayarindPaymentListener mListener;

    private BayarindInterface(){}

    public static BayarindInterface getInstance(){
        if(mInstance == null) {
            mInstance = new BayarindInterface();
        }
        return mInstance;
    }

    public void setListener(OnBayarindPaymentListener listener) {
        mListener = listener;
    }

    public void setSuccess(){
        mListener.onSuccess();
    }

    public void setCancel(){
        mListener.onCancel();
    }

    public void setPermissionDenied(){
        mListener.onPermissionDenied();
    }

    public void setFailed(){
        mListener.onFailed();
    }

    public void setError(){
        mListener.onError();
    }
}
