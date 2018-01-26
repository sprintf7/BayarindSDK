package net.sprintasia.bayarind.instance;

import net.sprintasia.bayarind.listener.OnBayarindPay;

/**
 * Created by ops1 on 17/01/2018.
 */

public class BayarindPayProcess {

    private static BayarindPayProcess instance;
    private static OnBayarindPay listener;

    public static BayarindPayProcess getInstance(){
        if(instance == null){
            instance = new BayarindPayProcess();
        }
        return instance;
    }

    public void setListener(OnBayarindPay listener){
        this.listener = listener;
    }

    public void onSuccess(){
        listener.onSuccess();
    }

    public void onFailed(String message){
        listener.onFailed(message);
    }

    public void onPermissionDenied(){
        listener.onPermissionDenied();
    }

    public void onCancel(){
        listener.onCancel();
    }

    public void onError(String message){
        listener.onError(message);
    }

}
