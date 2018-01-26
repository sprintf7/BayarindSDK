# BayarindSDK

Library for Bayarind Payment Service

# Intallation

Add the following dependency to your **build.gradle** (Application Level) file.

`compile 'com.dlazaro66.qrcodereaderview:qrcodereaderview:2.0.3'`

`compile 'net.sprintasia:bayarind:1.1.20180126'`

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

4.) Add bayarind payment SDK activity to your AndroidManifest.xml
```xml
<activity android:name="net.sprintasia.bayarind.activity.BayarindPayActivity" ></activity>
```

5.) A very basic activity would look like this:

```java
public class MainActivity extends AppCompatActivity{
    BayarindPayment bayarindPayment;
    Button btnPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText custId = findViewById(R.id.customerid);
        final EditText phone = findViewById(R.id.phone);
        final TextView reponse = findViewById(R.id.reponse);

        btnPay = findViewById(R.id.btn_pay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BayarindPayment bayarindPay = new BayarindPayment(MainActivity.this, custId.getText().toString(), phone.getText().toString());
                bayarindPay.setOnBayarindPayListener(new OnBayarindPay() {
                    @Override
                    public void onCancel() {
                        reponse.setText("Payment cancelled by user");
                    }

                    @Override
                    public void onPermissionDenied() {
                        reponse.setText("Camera permission is declined by user");
                    }

                    @Override
                    public void onSuccess() {
                        reponse.setText("Payment Success");
                    }

                    @Override
                    public void onFailed(String message) {
                        reponse.setText("Payment Failed, message: "+message);
                    }

                    @Override
                    public void onError(String message) {
                        reponse.setText("Payment Error, message: "+message);
                    }
                });
                bayarindPay.show();
            }
        });
    }
}
```

# Documentation

## Setup

### Main Class 
    Class BayarindPayment(context, String, String);
    interface OnBayarindPay();

### Ex

```java
String customerId = "CUST001";
String phone = "0888888888888";
BayarindPayment bayarindPay = new BayarindPayment(MainActivity.this, customerId, phone);
bayarindPay.setOnBayarindPayListener(new OnBayarindPay() {
    @Override
    public void onCancel() {
        reponse.setText("Payment cancelled by user");
    }

    @Override
    public void onPermissionDenied() {
        reponse.setText("Camera permission is declined by user");
    }

    @Override
    public void onSuccess() {
        reponse.setText("Payment Success");
    }

    @Override
    public void onFailed(String message) {
        reponse.setText("Payment Failed, message: "+message);
    }

    @Override
    public void onError(String message) {
        reponse.setText("Payment Error, message: "+message);
    }
});
bayarindPay.show();
```
### Note
    1. Customer id and phone cannot be null
    2. Customer id must be in alphanumeric and max length 24 char
    3. Phone must be in numeric and manx length 20 char

## `OnBayarindPay`

    This interface will triggered if payment has **Success, Failed, Cancel, Permission Denied, Error**

```java
@Override
public void onSuccess() {
    // triggered when payment is success
}

@Override
public void onFailed(String message) {
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
public void onError(String message) {
    // triggered when something error with SDK
}

```



