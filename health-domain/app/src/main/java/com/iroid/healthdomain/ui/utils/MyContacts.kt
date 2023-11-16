package com.iroid.healthdomain.ui.utils

import android.annotation.SuppressLint
import android.app.Service
import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.os.IBinder
import android.provider.ContactsContract
import android.util.Log
import com.google.gson.Gson
import com.iroid.healthdomain.data.dummyModel.HashedModel
import com.iroid.healthdomain.data.dummyModel.NewContactsModel
import com.iroid.healthdomain.data.md5
import com.iroid.healthdomain.ui.preference.AppPreferences.contacts_loaded
import com.iroid.healthdomain.ui.preference.AppPreferences.user_contacts
import com.iroid.healthdomain.ui.preference.AppPreferences.user_new_contacts


class MyContacts : Service() {


    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    @SuppressLint("Range")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("TAG", "onStartCommand: ")

        var hashedPhone = arrayListOf<String>()
        var newContacts = arrayListOf<NewContactsModel>()
        val PROJECTION = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        val builder = StringBuilder()
        val resolver: ContentResolver = application.contentResolver
        val cursor: Cursor? = resolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            PROJECTION,
            null,
            null,
            null
        )
        if (cursor != null) {
            try {

                while (cursor.moveToNext()) {
                    val phoneNumValue = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    )
                    //  Log.i(TAG, "getContacts: $phoneNumValue")
                    var numWithCode = ""
                    if (phoneNumValue.length >= 10) {
                        numWithCode =
                            "91" + phoneNumValue.substring(phoneNumValue.length - 10)
                    }

                    val name = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    )

                    builder.append("Contact: ").append(name).append(", Phone Number: ")
                        .append(
                            numWithCode
                        ).append("\n\n")

                    hashedPhone.add(numWithCode.md5())

                    val dataModel = NewContactsModel(numWithCode.md5(), name)
                    newContacts.add(dataModel)

                }

            } finally {
                cursor.close()
            }
        }

        val hashedModel = HashedModel(hashedPhone)
        val gson = Gson()
        val json = gson.toJson(hashedModel)
        val json1 = gson.toJson(newContacts)

        user_new_contacts = json1
        user_contacts = json
        contacts_loaded = true

        Log.e("TAG", "onStartCommand: "+ hashedModel)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("TAG", "onDestroy: ")
    }

}