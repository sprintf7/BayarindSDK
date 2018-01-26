package net.sprintasia.bayarind.listener;

/**
 * Created by ops1 on 17/01/2018.
 */

public interface OnBayarindPay {
    void onCancel();
    void onPermissionDenied();
    void onSuccess();
    void onFailed(String message);
    void onError(String message);
}
