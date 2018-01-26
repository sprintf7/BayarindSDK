package net.sprintasia.bayarind.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ops1 on 18/01/2018.
 */

public class CardToken implements Parcelable {
    private String expDate;
    private String cardNo;
    private String maskCardNo;
    private String token;
    private String customerName;
    private String customerEmail;
    private String cardType;
    private String cvc;

    public CardToken() {
    }

    public CardToken(String expDate, String cardNo, String maskCardNo, String token, String customerName, String customerEmail, String cardType) {
        this.expDate = expDate;
        this.cardNo = cardNo;
        this.maskCardNo = maskCardNo;
        this.token = token;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.cardType = cardType;
    }

    private CardToken(Parcel in) {
        expDate = in.readString();
        cardNo = in.readString();
        maskCardNo = in.readString();
        token = in.readString();
        customerName = in.readString();
        customerEmail = in.readString();
        cardType = in.readString();
        cvc = in.readString();
    }

    public static final Creator<CardToken> CREATOR = new Creator<CardToken>() {
        @Override
        public CardToken createFromParcel(Parcel in) {
            return new CardToken(in);
        }

        @Override
        public CardToken[] newArray(int size) {
            return new CardToken[size];
        }
    };

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getMaskCardNo() {
        return maskCardNo;
    }

    public void setMaskCardNo(String maskCardNo) {
        this.maskCardNo = maskCardNo;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(expDate);
        parcel.writeString(cardNo);
        parcel.writeString(maskCardNo);
        parcel.writeString(token);
        parcel.writeString(customerName);
        parcel.writeString(customerEmail);
        parcel.writeString(cardType);
        parcel.writeString(cvc);
    }
}
