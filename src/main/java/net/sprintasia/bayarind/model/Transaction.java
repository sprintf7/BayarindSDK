package net.sprintasia.bayarind.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ops1 on 20/12/2017.
 */

public class Transaction implements Parcelable {
    private String transactionNo;
    private int amount;
    private String customerEmail;
    private String customerName;

    public Transaction(){}

    public Transaction(String transactionNo, int amount, String customerEmail, String customerName) {
        this.transactionNo = transactionNo;
        this.amount = amount;
        this.customerEmail = customerEmail;
        this.customerName = customerName;
    }

    public Transaction(Parcel parcel){
        this.transactionNo = parcel.readString();
        this.amount = parcel.readInt();
        this.customerEmail = parcel.readString();
        this.customerName = parcel.readString();
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(transactionNo);
        parcel.writeInt(amount);
        parcel.writeString(customerEmail);
        parcel.writeString(customerName);
    }

    public static Creator<Transaction> CREATOR = new Creator<Transaction>() {

        @Override
        public Transaction createFromParcel(Parcel source) {
            return new Transaction(source);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }

    };

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionNo='" + transactionNo + '\'' +
                ", amount=" + amount +
                ", customerEmail='" + customerEmail + '\'' +
                ", customerName='" + customerName + '\'' +
                '}';
    }
}
