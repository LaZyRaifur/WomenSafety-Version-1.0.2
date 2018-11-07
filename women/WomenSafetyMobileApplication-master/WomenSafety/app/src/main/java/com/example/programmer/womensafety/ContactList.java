package com.example.programmer.womensafety;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Programmer on 12/17/2017.
 */

public class ContactList  extends AppCompatActivity {

    private final int REQUEST_CODE = 99;

    private final int PICK_CONTACT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactlist);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

    public void BackButton(View v) {
        onBackPressed();
    }

    public void callContacts(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CONTACT) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                Uri contactData = data.getData();
                Cursor cursor = getContentResolver().query(contactData, null, null, null, null);

                if (cursor.moveToFirst()) {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                    //System.out.print(name);
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("call", "name");
                    Toast.makeText(this, "You've picked : " + name, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



//        @Override
//        public void onActivityResult(int reqCode, int resultCode, Intent data) {
//            super.onActivityResult(reqCode, resultCode, data);
//            switch (reqCode) {
//                case (REQUEST_CODE):
//                    if (resultCode == Activity.RESULT_OK) {
//                        Uri contactData = data.getData();
//                        Cursor c = getContentResolver().query(contactData, null, null, null, null);
//                        if (c.moveToFirst()) {
//                            String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
//                            String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
//                            String num = "";
//                            if (Integer.valueOf(hasNumber) == 1) {
//                                Cursor numbers = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.
//                                                CONTENT_URI, null,
//                                        ContactsContract.CommonDataKinds.Phone.
//                                                CONTACT_ID + " = " + contactId, null, null);
//                                while (numbers.moveToNext()) {
//                                    num = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                                    Toast.makeText(ContactList.this, "Number="+num, Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        }
//                        break;
//                    }
//            }
//        }
}




