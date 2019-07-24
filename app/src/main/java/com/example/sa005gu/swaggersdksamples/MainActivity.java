package com.example.sa005gu.swaggersdksamples;

import com.example.sa005gu.swaggersdksamples.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pb.identify.identifyAddress.validateMailingAddressPro.model.ValidateMailingAddressProAPIResponse;
import com.pb.identify.identifyAddress.validateMailingAddressPro.model.ValidateMailingAddressProAPIResponseList;
import com.pb.identify.interfaces.IdentifyServiceManager;
import com.pb.identify.identifyAddress.validateMailingAddressPro.model.Address;
import com.pb.identify.interfaces.RequestObserver;
import com.pb.identify.network.ErrorResponse;
import com.pb.identify.utils.Log;
import com.pb.identify.utils.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import pb.ApiClient;
import pb.Configuration;
import pb.locationintelligence.LIAPIGeoLifeServiceApi;
import pb.locationintelligence.LIAPIGeocodeServiceApi;
import pb.locationintelligence.model.Demographics;
import pb.ApiException;
import pb.locationintelligence.model.DemographicsThemes;

public class MainActivity extends AppCompatActivity {

    String address = "";
    String country = "";
    Button validateButton;
    protected ProgressDialog progressDialog;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private RequestObserver validateMailingAddressProCallBack(final List<ValidateMailingAddressProAPIResponse> responses,
                                                              final ErrorResponse[] errorResponses) {
        return new RequestObserver<ValidateMailingAddressProAPIResponseList>() {
            @Override
            public void onSucess(ValidateMailingAddressProAPIResponseList responseList) {
                Gson gson = new GsonBuilder().create();

                String response = gson.toJson(responseList);
                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);


                Log.d(response);
                ((EditText) findViewById(R.id.textAddressLine1)).setText(responseList.getResponses().get(0).getAddressLine1());
                ((EditText) findViewById(R.id.textAddressLine2)).setText(responseList.getResponses().get(0).getAddressLine2());
                ((EditText) findViewById(R.id.textCity)).setText(responseList.getResponses().get(0).getCity());
                ((EditText) findViewById(R.id.textCountry)).setText(responseList.getResponses().get(0).getCountry());
                ((EditText) findViewById(R.id.textState)).setText(responseList.getResponses().get(0).getStateProvince());
                ((EditText) findViewById(R.id.textPostalCode)).setText(responseList.getResponses().get(0).getPostalCode());
                ((EditText) findViewById(R.id.textFirmName)).setText(responseList.getResponses().get(0).getFirmname());

                String block_address = responseList.getResponses().get(0).getBlockAddress();
                address = responseList.getResponses().get(0).getAddressLine1() + " " + responseList.getResponses().get(0).getCity() + " " + responseList.getResponses().get(0).getStateProvince() + " " + responseList.getResponses().get(0).getPostalCode();
                country = responseList.getResponses().get(0).getCountry();

                intent.putExtra("Address", address);
                intent.putExtra("Block_Address", block_address);
                intent.putExtra("Country", country);
                startActivity(intent);
                hideProgressDialog();
            }

            @Override
            public void onRequestStart() {
                Log.d("Request initiated");
            }

            @Override
            public void onFailure(ErrorResponse e) {
                hideProgressDialog();
                Log.d("Error: " + e.getRootErrorMessage());
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = this;
        initProgressDialog();
        validateButton = (Button) findViewById(R.id.buttonValidate);
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String API_KEY = context.getString(R.string.API_KEY);
                final String SECRET = context.getString(R.string.SECRET);
                if (API_KEY.isEmpty() || SECRET.isEmpty()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("API_KEY and SECRET Missing");
                    alertDialog.setMessage("Enter your API_KEY and SECRET in build.gradle file to make this app running");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    IdentifyServiceManager identifyServiceManager = IdentifyServiceManager.getInstance(context, API_KEY, SECRET);

                    Address address2 = new Address();
                    if (validateFields()) {
                        showProgressDialog("Getting Demographics", false);
                        address2.setAddressLine1(((EditText) findViewById(R.id.textAddressLine1)).getText().toString());
                        address2.setAddressLine2(((EditText) findViewById(R.id.textAddressLine2)).getText().toString());
                        address2.setCity(((EditText) findViewById(R.id.textCity)).getText().toString());
                        address2.setStateProvince(((EditText) findViewById(R.id.textState)).getText().toString());
                        address2.setCountry(((EditText) findViewById(R.id.textCountry)).getText().toString());
                        address2.setFirmName(((EditText) findViewById(R.id.textFirmName)).getText().toString());

                        final ValidateMailingAddressProAPIResponseList validateMailingAddressResponse = new ValidateMailingAddressProAPIResponseList();
                        identifyServiceManager.getIdentifyAddressService().validateMailingAddressPro(context, Arrays.asList(address2), null, validateMailingAddressProCallBack(validateMailingAddressResponse.getResponses(), null));
                    }
                }
            }
        });

        Button resetButton = (Button) findViewById(R.id.buttonReset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditText) findViewById(R.id.textAddressLine1)).setText("");
                ((EditText) findViewById(R.id.textAddressLine2)).setText("");
                ((EditText) findViewById(R.id.textCity)).setText("");
                ((EditText) findViewById(R.id.textCountry)).setText("");
                ((EditText) findViewById(R.id.textState)).setText("");
                ((EditText) findViewById(R.id.textFirmName)).setText("");
                ((EditText) findViewById(R.id.textPostalCode)).setText("");

            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private boolean validateFields() {
        if (TextUtils.isEmpty(((EditText) findViewById(R.id.textCity)).getText().toString())) {
            Toast.makeText(this, "enter city", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(((EditText) findViewById(R.id.textState)).getText().toString())) {
            Toast.makeText(this, "enter state", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(((EditText) findViewById(R.id.textCountry)).getText().toString())) {
            Toast.makeText(this, "enter country", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (TextUtils.isEmpty(((EditText) findViewById(R.id.textAddressLine1)).getText().toString()))
        {
            Toast.makeText(this, "enter address line 1", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    /**
     * Progress Dialog Init
     */
    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Getting Demographics");
    }

    /**
     * Show Progress Dialog
     *
     * @param message     message to show
     * @param cancellable dialog cancellable
     */
    @SuppressWarnings("SameParameterValue")
    public void showProgressDialog(String message, boolean cancellable) {
        try {
            if (progressDialog != null) {
                if (!TextUtils.isEmpty(message)) {
                    progressDialog.setMessage(message);
                }
                progressDialog.setCancelable(cancellable);
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            }
        } catch (Throwable ignore) {
        }
    }

    //Hide progress dialog.
    public void hideProgressDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Throwable ignore) {
        }
    }

}