
package com.example.myspecialstalker;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String NOT_READY_MSG = "To send you're message you must fill all fields";
    public static final String READY_MSG = "Ready to Send SMS message";
    public static final String PRE_DEFINED_TEXT = "I'm going to call this number: ";
    public static final String PHONE_NUM_SP = "phoneNum";
    public static final String MESSAGE_SP = "msg";
    public static final String PER_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String PER_CALLS = Manifest.permission.PROCESS_OUTGOING_CALLS;
    public static final String SEND_SMS = Manifest.permission.SEND_SMS;
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;
    protected EditText phoneNum;
    protected EditText msg;
    protected TextView title;
    protected TextView numMissingData;
    protected TextView msgMissingData;
    public static String getPhoneNum;
    public static String getMsg;
    public static boolean emptyPhone = false;
    public static boolean emptyMsg = true;
    public final String[] PERMISSION_REQUEST = {PER_PHONE_STATE, PER_CALLS,
            SEND_SMS};
    public static final int REQUEST_NUM = 1;
    public static final int PackManGranted = PackageManager.PERMISSION_GRANTED;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (
                ContextCompat.checkSelfPermission(MainActivity.this, SEND_SMS) != PackManGranted  ||
                        ContextCompat.checkSelfPermission(MainActivity.this, PER_CALLS) != PackManGranted  ||
                        ContextCompat.checkSelfPermission(MainActivity.this, PER_PHONE_STATE) != PackManGranted)
            ActivityCompat.requestPermissions(this, PERMISSION_REQUEST, REQUEST_NUM);

        else {
            setContentView(R.layout.activity_main);
            launch();
        }
    }
    public boolean checkSmsNum(String sms_num) {

        if (sms_num.length() == 9) {
            return android.text.TextUtils.isDigitsOnly(sms_num);
        } else if (sms_num.length() == 12) {
            if (sms_num.charAt(0) != '+') {
                return false;
            } else return android.text.TextUtils.isDigitsOnly(sms_num.substring(1));
        }
        return false;
    }

    public void launch() {


        phoneNum = (EditText) findViewById(R.id.phone_number_edit_txt);
        msg = (EditText) findViewById(R.id.message_edit_txt);
        title = (TextView) findViewById(R.id.txt_view);
        numMissingData = (TextView) findViewById(R.id.phone_number_txt_view);
        msgMissingData = (TextView) findViewById(R.id.msg_txt_view);
        msgMissingData.setVisibility(View.INVISIBLE);
        numMissingData.setVisibility(View.INVISIBLE);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        getPhoneNum = sharedPreferences.getString(PHONE_NUM_SP, "");
        getMsg = sharedPreferences.getString(MESSAGE_SP, "");


        if (getPhoneNum.length() == 0) {
            emptyPhone = false;
        } else {
            emptyPhone = true;
        }
        if (getMsg.length() == 0) {
            emptyMsg = false;
        } else {
            emptyMsg = true;
        }
        if (!emptyMsg) {
            msg.setText(PRE_DEFINED_TEXT);
            getMsg = PRE_DEFINED_TEXT;
            emptyMsg = true;
        } else {
            msg.setText(getMsg);
        }
        phoneNum.setText(getPhoneNum);


        if (emptyPhone && emptyMsg && checkSmsNum(getPhoneNum)) {
            title.setText(READY_MSG);
        } else {
            title.setText(NOT_READY_MSG);
        }
        phoneNum.addTextChangedListener(phoneFieldWatcher());

        msg.addTextChangedListener(messageFieldWatcher());
    }

    public TextWatcher phoneFieldWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                return;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {
                    emptyPhone = true;
                    numMissingData.setVisibility(View.INVISIBLE);
                    if (emptyMsg && emptyPhone && checkSmsNum(getPhoneNum)) {
                        title.setText(READY_MSG);
                    } else {
                        title.setText(NOT_READY_MSG);
                    }
                } else {
                    title.setText(NOT_READY_MSG);
                    emptyPhone = false;
                    numMissingData.setVisibility(View.VISIBLE);
                }
                getPhoneNum = s.toString();
                editor.putString(PHONE_NUM_SP, s.toString());
                editor.apply();
            }

            @Override
            public void afterTextChanged(Editable s) {
                return;
            }
        };
    }

    public TextWatcher messageFieldWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                return;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String message = msg.getText().toString();
                if (message.equals("")) {
                    emptyMsg = false;
                    title.setText(NOT_READY_MSG);
                    msgMissingData.setVisibility(View.VISIBLE);
                } else {
                    emptyMsg = true;
                    msgMissingData.setVisibility(View.INVISIBLE);
                    if (emptyMsg && emptyPhone && checkSmsNum(getPhoneNum)) {
                        title.setText(READY_MSG);
                    } else {
                        title.setText(NOT_READY_MSG);
                    }
                }
                getMsg = s.toString();
                editor.putString(MESSAGE_SP, s.toString());
                editor.apply();
            }
            @Override
            public void afterTextChanged(Editable s) {
                return;
            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_NUM) {
            if ( grantResults.length == 3 && grantResults[0] == PackManGranted && grantResults[1] == PackManGranted
                    && grantResults[2] == PackManGranted) {
                setContentView(R.layout.activity_main);
                launch();
            } else
                ActivityCompat.requestPermissions(this, PERMISSION_REQUEST, REQUEST_NUM);
        }
    }

}