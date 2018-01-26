package net.sprintasia.bayarind.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.sprintasia.bayarind.R;
import net.sprintasia.bayarind.adapter.CardAdapter;
import net.sprintasia.bayarind.instance.BayarindFragment;
import net.sprintasia.bayarind.lib.ApiCall;
import net.sprintasia.bayarind.listener.OnApiListener;
import net.sprintasia.bayarind.model.CardToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ops1 on 18/01/2018.
 */

public class CardListFragment extends Fragment {

    private RelativeLayout header;
    private View view;
    private String customerId;
    private ProgressBar progressBar;
    private ListView cardList;
    private ArrayList<CardToken> tokenList;
    private CardAdapter adapter;
    private TextView noCardInfo;
    private SwipeRefreshLayout refreshLayout;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_list,
                container, false);
        this.view = view;
        customerId = getArguments().getString("customer_id");
        Button btnNewCard = view.findViewById(R.id.btn_new_card);
        header = view.findViewById(R.id.header);
        noCardInfo = view.findViewById(R.id.no_card_info);
        refreshLayout = view.findViewById(R.id.swiperefresh);
        noCardInfo.setVisibility(View.GONE);
        btnNewCard.setTranslationY(500);
        header.setTranslationY(-600);
        header.animate().translationY(0).start();
        btnNewCard.animate().translationY(0).start();
        progressBar = view.findViewById(R.id.progress);
        cardList = view.findViewById(R.id.list_card);
        btnNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BayarindFragment.getInstance().changePage("NEW_CARD");
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCardToken();
            }
        });

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.dismiss();
        getCardToken();
        return view;
    }

    private void getCardToken(){
        progressBar.setVisibility(View.VISIBLE);
        cardList.setVisibility(View.GONE);
        ApiCall apiToken = new ApiCall("mobile/token/"+customerId, "GET");
        apiToken.send(new OnApiListener() {
            @Override
            public void onSuccess(final Boolean status, final String message, JSONObject data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideSwipeRefresh();
                        progressBar.setVisibility(View.GONE);
                        String m = (!status && message != null) ? message : "Opps, something error when retrieving your card data.";
                        Toast.makeText(getContext(), m, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onSuccess(final Boolean status, String message, final JSONArray data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        hideSwipeRefresh();
                        if(status) {
                            if(data.length() > 0) {
                                cardList.setVisibility(View.VISIBLE);
                                tokenList = new ArrayList<>();
                                try {
                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject token = data.getJSONObject(i);
                                        CardToken tk = new CardToken();
                                        tk.setCustomerEmail(token.getString("customer_email"));
                                        tk.setCustomerName(token.getString("customer_name"));
                                        tk.setCardType(token.getString("card_type"));
                                        tk.setMaskCardNo(token.getString("masked_card_no"));
                                        tk.setExpDate(token.getString("expiry_date"));
                                        tk.setToken(token.getString("card_token"));
                                        tokenList.add(tk);
                                    }
                                    adapter = new CardAdapter(tokenList, getContext(), CardListFragment.this);
                                    cardList.setAdapter(adapter);
                                    cardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            CardToken token = tokenList.get(position);
                                            BayarindFragment.getInstance().setCardToken(token);
                                            BayarindFragment.getInstance().changePage("TOKEN_DETAIL");
                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                noCardInfo.setVisibility(View.VISIBLE);
                            }
                        }else{
                            Toast.makeText(getContext(), "Opps, something error when retrieving your card data.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            @Override
            public void onSuccess(final Boolean status, final String message, String data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideSwipeRefresh();
                        progressBar.setVisibility(View.GONE);
                        noCardInfo.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onFailed() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideSwipeRefresh();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Opps, something error when communicating to payment server.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void deleteCardToken(final String cardToken){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        builder.setTitle("Delete Saved Card")
                .setMessage("Are you sure you want to delete this card?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        doDeletecardToken(cardToken);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void doDeletecardToken(String cardToken){
        progressDialog.show();
        ApiCall apiToken = new ApiCall("mobile/token/delete/"+customerId+"/"+cardToken, "DELETE");
        apiToken.send(new OnApiListener() {
            @Override
            public void onSuccess(Boolean status, String message, JSONObject data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        getCardToken();
                    }
                });
            }

            @Override
            public void onSuccess(final Boolean status, final String message, final JSONArray data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        String m = (!status && message != null) ? message : "Opps, something error when deleting your card data.";
                        Toast.makeText(getContext(), m, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onSuccess(final Boolean status, final String message, String data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        String m = (!status && message != null) ? message : "Opps, something error when deleting your card data.";
                        Toast.makeText(getContext(), m, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailed() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Opps, something error when communicating to payment server.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void hideSwipeRefresh(){
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }
}
