package com.project.varun.opendrive.Login;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.api.client.extensions.android.http.AndroidHttp;
//import com.google.api.client.json.gson.GsonFactory;
//import com.google.api.services.drive.Drive;
//import com.google.api.services.drive.DriveScopes;
import com.project.varun.opendrive.R;
//import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks{
    private int REQUEST_CODE_PICK_ACCOUNT=99;
    ArrayList<String> driveScopes = new ArrayList<>();
    GoogleApiClient mGoogleApiClient;
    private final int RESOLVE_CONNECTION_REQUEST_CODE = 990;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

//        driveScopes.add(DriveScopes.DRIVE);

        SignInButton signInButton = (SignInButton)findViewById(R.id.button_sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                pickUserAccount();
                mGoogleApiClient = new GoogleApiClient.Builder(LoginActivity.this)
                        .addApi(com.google.android.gms.drive.Drive.API)
                        .addScope(com.google.android.gms.drive.Drive.SCOPE_FILE)
                        .addConnectionCallbacks(LoginActivity.this)
                        .addOnConnectionFailedListener(LoginActivity.this)
                        .build();

                mGoogleApiClient.connect();
            }
        });

    }

    public void onStart(){
        super.onStart();

    }

    public void pickUserAccount() {

        if (isDeviceOnline()) {
            Log.i("Picking", "Picking User Account");
            String accountTypes[] = new String[]{"com.google"}; //com.google.android.legacyimap for all types of accounts
            Intent in = AccountPicker.newChooseAccountIntent(null, null, accountTypes, true, null, null, null, null);

            startActivityForResult(in, REQUEST_CODE_PICK_ACCOUNT);
        }
        else {
            Log.i("Network", "Not able to connect");
            Toast.makeText(getApplicationContext(), "Connect to a network to Log in", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isDeviceOnline() {
        ConnectivityManager comMng = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo netInfo = comMng.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected())
            return true;
        return false;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode == REQUEST_CODE_PICK_ACCOUNT){
//            if(resultCode == RESULT_OK){
//                String email = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
//                GoogleAccountCredential accountCredential = GoogleAccountCredential.usingOAuth2(this, driveScopes);
//                accountCredential.setSelectedAccountName(email);
//
//                    Drive service =
//                            new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), accountCredential).build();
//            }
        } else if(requestCode == RESOLVE_CONNECTION_REQUEST_CODE){

            if(resultCode == RESULT_OK){

                mGoogleApiClient.connect();
                Log.i("LoginActivity", "Google Drive Connected");
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        if(connectionResult.hasResolution()){

            try {
                connectionResult.startResolutionForResult(this, RESOLVE_CONNECTION_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
