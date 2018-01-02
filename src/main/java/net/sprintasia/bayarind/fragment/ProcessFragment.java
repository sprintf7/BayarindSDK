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

import net.sprintasia.bayarind.R;
import net.sprintasia.bayarind.core.BayarindInterface;

/**
 * Created by ops1 on 02/01/2018.
 */

public class ProcessFragment extends Fragment {
    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.process, container,false);

        initWebView(rootView);

        return rootView;
    }

    private void initWebView(View v){
        webView = (WebView) v.findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.addJavascriptInterface(new BayarindClickJavascriptInterface(getActivity().getBaseContext()), "BAYARIND");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("https://simpg.sprintasia.net/myCard/index.html");
    }

    public class BayarindClickJavascriptInterface {
        Context mContext;
        BayarindClickJavascriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void onPaymentResponse(String status) {
            if(status.equalsIgnoreCase("00")){
                BayarindInterface.getInstance().setSuccess();
            }else if(status.equalsIgnoreCase("01")){
                BayarindInterface.getInstance().setFailed();
            }else{
                BayarindInterface.getInstance().setError();
            }
        }
    }
}
