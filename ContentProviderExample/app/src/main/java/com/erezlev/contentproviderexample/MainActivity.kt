package com.erezlev.contentproviderexample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


private const val TAG = "MainActivity"
private const val REQUEST_CODE_READ_CONTACTS = 1

class MainActivity : AppCompatActivity() {

//    private var readGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val hasReadContactPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
        Log.d(TAG, "onCreate: check self permission return $hasReadContactPermission")

//        if (hasReadContactPermission == PackageManager.PERMISSION_GRANTED) {
//            Log.d(TAG, "onCreated: permission granted")
////            readGranted = true          // TODO: don't do this
//        } else {
//            Log.d(TAG, "onCreated: requesting permission")
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE_READ_CONTACTS)
//        }

        if (hasReadContactPermission != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onCreated: requesting permission")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE_READ_CONTACTS)
        }

        fab.setOnClickListener {
            Log.d(TAG, "fab OnClick: starts")
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                fab.setOnClickListener { view ->
                    Snackbar.make(view, "Please access contact permission", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Grant Access", View.OnClickListener{
                            Log.d(TAG, "Snackbar OnClick: starts")
                            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                                Manifest.permission.READ_CONTACTS)) {
                                Log.d(TAG, "Snackbar OnClick: calling requestPermissions")
                                ActivityCompat.requestPermissions(this,
                                    arrayOf(Manifest.permission.READ_CONTACTS),
                                    REQUEST_CODE_READ_CONTACTS)
                            } else {
                                // The user permanently denied the [permission, take them directly to settings.
                                Log.d(TAG, "Snackbar Onclick: launching settings.")
                                val intent = Intent()
                                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                val uri = Uri.fromParts("package", this.packageName, null)
                                Log.d(TAG, "Snackbar OnClick: uri is $uri")
                                intent.data = uri
                                this.startActivity(intent)
                            }
                            Log.d(TAG, "Snackbar OnClick: ends")
                        }).show()
                    Log.d(TAG, "fab OnClick: ends")
                }
            } else {
                val projection = arrayOf(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)

                val cursor = contentResolver.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    projection,
                    null,
                    null,
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
                )

                val contacts = ArrayList<String>()          // Create a list to hold down contacts
                cursor?.use {                               // Loop through the cursot.
                    while (it.moveToNext()) {
                        contacts.add(it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)))
                    }
                }

                val adapter =
                    ArrayAdapter<String>(this, R.layout.contact_detail, R.id.name, contacts)
                contact_names.adapter = adapter

                Log.d(TAG, "fab onClick: ends")
            }
        }
        Log.d(TAG, "onCreate: ends")
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        Log.d(TAG, "onRequestPermissionsResult: starts")
//        when (requestCode) {
//            REQUEST_CODE_READ_CONTACTS -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // Permission was granted.
//                    // Do the contacts task we need to do.
//                    Log.d(TAG, "onRequestPermissionResult: Permission granted")
//                } else {
//                    // Permission denied.
//                    // Disable the functionality that depends on this permission.
//                    Log.d(TAG, "onRequestPermissionResult: Permission refused")
//                }
////                fab.isEnabled = readGranted
//                // Permission was drnied. We want to show some functionality to the button.
//            }
//        }
//        Log.d(TAG, "onRequestPermissionResult: ends")
//    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
