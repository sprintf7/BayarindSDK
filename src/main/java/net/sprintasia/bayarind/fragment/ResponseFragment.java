package net.sprintasia.bayarind.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.sprintasia.bayarind.R;
import net.sprintasia.bayarind.instance.BayarindFragment;
import net.sprintasia.bayarind.instance.BayarindPayProcess;

/**
 * Created by ops1 on 18/01/2018.
 */

public class ResponseFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.response,
                container, false);
        this.view = view;

        final String status = getArguments().getString("payment_status");
        String message = getArguments().getString("payment_message");

        if(message == null || message.equals("")){
            message = "Opps, your payment has been rejected by payment server.";
        }

        LinearLayout blockButton = view.findViewById(R.id.block_button);
        blockButton.setTranslationY(1500);
        blockButton.animate().translationY(0).start();

        final TextView responseMessage = view.findViewById(R.id.response_message);
        final ImageView responseLogo = view.findViewById(R.id.response_logo);
        final RelativeLayout relativeLayout = view.findViewById(R.id.response_bg);

        responseLogo.setTranslationX(-500);
        responseMessage.setTranslationX(500);
        responseLogo.animate().translationX(0).start();
        responseMessage.animate().translationX(0).start();

        if(status.equalsIgnoreCase("00")){
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            responseLogo.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_check_black_24dp));
            responseMessage.setText("Your Payment has been successfully paid.");
        }else if(status.equalsIgnoreCase("02")){
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.colorOrange));
            responseLogo.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_report_black_24dp));
            String m = (message != null) ? message : "Opps, your payment declined by payment server.";
            responseMessage.setText(m);
        }else{
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.colorRed));
            responseLogo.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_block_black_24dp));
            responseMessage.setText(message);
        }

        Button btnFinish = view.findViewById(R.id.btnFinish);
        final String finalMessage = message;
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status.equalsIgnoreCase("00")){
                    BayarindPayProcess.getInstance().onSuccess();
                }else if(status.equalsIgnoreCase("02")){
                    String m = (finalMessage != null) ? finalMessage : "Opps, your payment declined by payment server.";
                    BayarindPayProcess.getInstance().onFailed(m);
                }else{
                    BayarindPayProcess.getInstance().onError(finalMessage);
                }
                BayarindFragment.getInstance().paymentDone();
            }
        });

        Button btnMore = view.findViewById(R.id.btnMore);
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BayarindFragment.getInstance().changePage("QR_LANDING");
            }
        });

        return view;
    }

}
