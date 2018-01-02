package net.sprintasia.bayarind.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ops1 on 02/01/2018.
 */

public class CardInfo implements Parcelable {

    private String cardNo;
    private String cvc;
    private String expMonth;
    private String expYear;
    private String cardType;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public String getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(String expMonth) {
        this.expMonth = expMonth;
    }

    public String getExpYear() {
        return expYear;
    }

    public void setExpYear(String expYear) {
        this.expYear = expYear;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public static Creator<CardInfo> getCREATOR() {
        return CREATOR;
    }

    public CardInfo(){}


    protected CardInfo(Parcel in) {
    }

    public static final Creator<CardInfo> CREATOR = new Creator<CardInfo>() {
        @Override
        public CardInfo createFromParcel(Parcel in) {
            return new CardInfo(in);
        }

        @Override
        public CardInfo[] newArray(int size) {
            return new CardInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(cardNo);
        parcel.writeString(cardType);
        parcel.writeString(expMonth);
        parcel.writeString(expYear);
        parcel.writeString(cardType);
        parcel.writeString(cvc);

    }

    @Override
    public String toString() {
        return "CardInfo{" +
                "cardNo='" + cardNo + '\'' +
                ", cvc='" + cvc + '\'' +
                ", expMonth='" + expMonth + '\'' +
                ", expYear='" + expYear + '\'' +
                ", cardType='" + cardType + '\'' +
                '}';
    }
}
