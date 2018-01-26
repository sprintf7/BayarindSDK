package net.sprintasia.bayarind.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.sprintasia.bayarind.Constant;
import net.sprintasia.bayarind.R;
import net.sprintasia.bayarind.instance.BayarindFragment;

/**
 * Created by ops1 on 18/01/2018.
 */

public class WebViewFragment extends Fragment {

    private WebView webView;
    private View view;

    private String paymentCode;
    private String redirectURL;
    private String redirectData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.webview,
                container, false);
        this.view = view;

        paymentCode = getArguments().getString("payment_code");
        redirectURL = getArguments().getString("redirect_url");
        redirectData = getArguments().getString("redirect_data");

        initWebView();
        return view;
    }

    private void initWebView(){
        webView = view.findViewById(R.id.bayarin_webview);
        webView.addJavascriptInterface(new BayarindClickJavascriptInterface(getActivity().getBaseContext()), "BAYARIND");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl( Constant.BACKEND_URL + "landing/" + paymentCode );
        webView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url){
                if(url.contains(Constant.BACKEND_URL + "landing/")){
                    webView.loadUrl("javascript:handler('" + redirectURL + "', '" + redirectData + "')");
                }
            }
        });
    }

    public class BayarindClickJavascriptInterface {
        Context mContext;
        BayarindClickJavascriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void onPaymentResponse(String status, String message) {
            BayarindFragment.getInstance().setPaymentResponse(status, message);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(webView != null){
            webView.destroy();
        }
    }
}
