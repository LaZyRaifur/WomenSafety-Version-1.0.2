package com.example.programmer.womensafety;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by Programmer on 12/18/2017.
 */

public class Hospital extends AppCompatActivity {

    private final int PICK_CONTACT = 1;
    ImageButton btnSubmit;
    EditText varMsg,varPhoneNo;
    private static final int MY_PERMISSION_REWUEST_SEND_SMS = 0;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospital);

        btnSubmit = (ImageButton)findViewById(R.id.idBtnStart);
        varMsg = (EditText)findViewById(R.id.EtSms);
        varPhoneNo = (EditText)findViewById(R.id.EtPhone);

        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int i) {
                switch (i){
                    case R.id.Red:
                    getWindow().getDecorView().setBackgroundColor(Color.parseColor("#FF0000"));
                break;
                    case R.id.Yellow:
                        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#FFFF00"));
                        break;
                        case R.id.Megenta:
                        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#FF00FF"));
                        break;
                        case R.id.Cyan:
                        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#00FFFF"));
                        break;
                }
            }
        });
    }
    //BackButton
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

    public void BackButton(View v)
    {
        onBackPressed();
    }
    public void callContacts(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }


    //Method to start the service

    public void SendSms(View view)
    {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED){
            //Name of full method of calling message
            MyMessage();

        }else {
            //TO DO
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},MY_PERMISSION_REWUEST_SEND_SMS);
        }
    }

    private void MyMessage() {
        String MyNumber = varPhoneNo.getText().toString().trim();
        String MyMsg = varMsg.getText().toString().trim();

        //Begin check for phonenumber


        if (MyNumber == null || MyNumber.equals("") ||MyMsg == null || MyMsg.equals("")){
            Toast.makeText(this,"Field Can't be Empty",Toast.LENGTH_SHORT).show();
        }else {
            if (TextUtils.isDigitsOnly(MyNumber)){
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(MyNumber,null,MyMsg,null,null);
                Toast.makeText(this,"Message Sent",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"Please Enter the Integer Only",Toast.LENGTH_SHORT).show();
            }
        }
        //End the phone number

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSION_REWUEST_SEND_SMS:
            {
                if (grantResults.length>=0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //Name of method for calling Message
                    MyMessage();
                }else {
                    Toast.makeText(this,"You don't have required permission",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
