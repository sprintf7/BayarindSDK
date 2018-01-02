package net.sprintasia.bayarind.listener;

/**
 * Created by ops1 on 02/01/2018.
 */

public interface OnBayarindPaymentListener {
    void onSuccess();
    void onFailed();
    void onError();
    void onCancel();
    void onPermissionDenied();
}
