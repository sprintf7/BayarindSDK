package net.sprintasia.bayarind.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import net.sprintasia.bayarind.R;
import net.sprintasia.bayarind.fragment.CardListFragment;
import net.sprintasia.bayarind.model.CardToken;

import java.util.ArrayList;

/**
 * Created by ops1 on 18/01/2018.
 */

public class CardAdapter extends ArrayAdapter<CardToken> {

    private ArrayList<CardToken> dataSet;
    private Context mContext;
    private CardListFragment fragment;

    // View lookup cache
    private static class ViewHolder {
        ImageView logo;
        TextView expDate;
        TextView cardNo;
        TextView customerName;
        TextView customerEmail;
        ImageView btnDelete;
    }

    public CardAdapter(ArrayList<CardToken> data, Context context, CardListFragment fragment) {
        super(context, R.layout.card_row, data);
        this.dataSet = data;
        this.mContext = context;
        this.fragment = fragment;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CardToken dataModel = getItem(position);
        final ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.card_row, parent, false);

            viewHolder.expDate = (TextView) convertView.findViewById(R.id.exp_date_view);
            viewHolder.cardNo = (TextView) convertView.findViewById(R.id.cc_no_view);
            viewHolder.customerName = (TextView) convertView.findViewById(R.id.cc_name_view);
            viewHolder.customerEmail = (TextView) convertView.findViewById(R.id.cc_email_view);
            viewHolder.logo = (ImageView) convertView.findViewById(R.id.cc_logo_view);
            viewHolder.btnDelete = (ImageView) convertView.findViewById(R.id.btn_delete);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        if(position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.up_from_bottom);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            result.startAnimation(animation);
            lastPosition = position;
        }
        String[] exp = dataModel.getExpDate().split("\\|");
        String expText = exp[1] + " / " + exp[0];
        viewHolder.expDate.setText(expText);
        viewHolder.cardNo.setText(dataModel.getMaskCardNo());
        viewHolder.customerName.setText(dataModel.getCustomerName());
        viewHolder.customerEmail.setText(dataModel.getCustomerEmail());

        int cBG = R.drawable.ic_credit_card_black_24dp;
        switch (dataModel.getCardType().toUpperCase()){
            case "VISA":
                cBG = R.drawable.bt_ic_visa;
                break;
            case "JCB":
                cBG = R.drawable.bt_ic_jcb;
                break;
            case "MASTERCARD":
                cBG = R.drawable.bt_ic_mastercard;
                break;
            case "AMEX":
                cBG = R.drawable.bt_ic_amex;
                break;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewHolder.logo.setImageDrawable(getContext().getResources().getDrawable(cBG, getContext().getTheme()));
        } else {
            viewHolder.logo.setImageDrawable(getContext().getResources().getDrawable(cBG));
        }

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu cardMenu = new PopupMenu(getContext(), viewHolder.btnDelete);
                cardMenu.getMenuInflater().inflate(R.menu.card_menu, cardMenu.getMenu());
                cardMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int mId = item.getItemId();
                        if (mId == R.id.menu_delete_token) {
                            fragment.deleteCardToken(dataModel.getToken());
                            return true;
                        }
                        return false;
                    }
                });
                cardMenu.show();
            }
        });

        return convertView;
    }

}
