# BayarindSDK

Library for Bayarind Payment Service

# Intallation

Add the following dependency to your **build.gradle** (Application Level) file.

`implementation 'com.android.support:appcompat-v7:26.1.0'`
`implementation 'com.android.support:design:26.1.0'`
`implementation 'com.android.support:cardview-v7:26.1.+'`
`implementation 'me.dm7.barcodescanner:zxing:1.9.8'`
`implementation 'net.sprintasia:bayarind:1.0.0'`

# Simple Usage

1.) Add Library Repository

On your root **build.gradle** 
```javascript
maven {
    url "https://dl.bintray.com/sprintf7/Bayarind-Payment"
}
```

So your build.gradle looks like
```javascript
allprojects {
    repositories {
        google()
        jcenter()
        maven {
            url "https://dl.bintray.com/sprintf7/Bayarind-Payment"
        }
    }
}
```

2.) Add internet permission to your AndroidManifest.xml file:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

3.) Add camera permission to your AndroidManifest.xml file:

```xml
<uses-permission android:name="android.permission.CAMERA" />
```

4.) A very basic activity would look like this:

```java
public class BayarindActivity extends AppCompatActivity implements OnBayarindPaymentListener {
    
    BayarindPayment bayarindPayment;
    Button btnPay;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);
        btnPay = findViewById(R.id.btnPay);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payWithBayarind();
            }
        });
    }
    
    private void payWithBayarind(){
        Transaction transaction = new Transaction();
        transaction.setTransactionNo(getRandomChar(20));
        transaction.setAmount(getRandomNumber(1000000));
        transaction.setCustomerEmail("customer.email@email.com");
        transaction.setCustomerName("Customer Name");

        bayarindPayment = new BayarindPayment(this, transaction);
        bayarindPayment.setOnBayarindPaymentListener(this);
    }
  
    private void sdkCallback(String msg){

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("SDK Callback")
        .setMessage(msg)
        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
        .setIcon(android.R.drawable.ic_dialog_alert)
        .show();
    }

    private int getRandomNumber(int max){
        Random rand = new Random();
        int  n = rand.nextInt(max) + 1000;
        return n;
    }

    private String getRandomChar(int length){
        Random r = new Random();
        String res = "";
        String alphabet = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < length; i++) {
            res += alphabet.charAt(r.nextInt(alphabet.length()));
        }
        return res;
    }

    @Override
    public void onSuccess() {
        sdkCallback("Payment Success");
    }

    @Override
    public void onFailed() {
        sdkCallback("Payment Failed");
    }

    @Override
    public void onCancel() {
        sdkCallback("Payment Cancelled");
    }

    @Override
    public void onPermissionDenied() {
        sdkCallback("Permission denied");
    }

    @Override
    public void onError() {
        sdkCallback("Opps, something error");
    }
    
}
```

# Documentation

## Setup

```java
Transaction transaction = new Transaction();
transaction.setTransactionNo("TRX1234");
transaction.setAmount(1000000);
transaction.setCustomerEmail("customer@email.com");
transaction.setCustomerName("Customer Name");

BayarindPayment bayarindPayment = new BayarindPayment(YOUR_ACTIVITY.this, transaction);
bayarindPayment.setOnBayarindPaymentListener(new OnBayarindPaymentListener() {
    @Override
    public void onSuccess() {}

    @Override
    public void onFailed() {}

    @Override
    public void onError() {}

    @Override
    public void onCancel() {}

    @Override
    public void onPermissionDenied() {}
});
```

## `OnBayarindPaymentListener`

This interface will triggered if payment has **Success, Failed, Cancel, Permission Denied, Error**

```java
@Override
public void onSuccess() {
    // triggered when payment is success
}

@Override
public void onFailed() {
    // triggered when payment is failed
}

@Override
public void onCancel() {
    // triggered when payment is cancelled
}

@Override
public void onPermissionDenied() {
    // triggered when camera permission is not granted by user
}

@Override
public void onError() {
    // triggered when something error with SDK
}

```


## Transaction Model

Transaction model is located in package **net.sprintasia.bayarind.model**

### Attribute
```java
String transactionNo;
int amount;
String customerEmail;
String customerName;
```

All attribute must be filled

