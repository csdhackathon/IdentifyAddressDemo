package com.example.sa005gu.swaggersdksamples;

import com.example.sa005gu.swaggersdksamples.R;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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

import pb.ApiClient;
import pb.Configuration;
import pb.locationintelligence.LIAPIGeoLifeServiceApi;
import pb.locationintelligence.LIAPIGeocodeServiceApi;
import pb.locationintelligence.model.Demographics;
import pb.ApiException;
import pb.locationintelligence.model.DemographicsThemes;

public class MainActivity extends AppCompatActivity {

    String address;
    String country;
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
                Intent intent=  new Intent(getApplicationContext(), ResultActivity.class);


                Log.d(response);
                ((EditText) findViewById(R.id.textAddressLine1)).setText(responseList.getResponses().get(0).getAddressLine1());
                ((EditText) findViewById(R.id.textAddressLine2)).setText(responseList.getResponses().get(0).getAddressLine2());
                ((EditText) findViewById(R.id.textCity)).setText(responseList.getResponses().get(0).getCity());
                ((EditText) findViewById(R.id.textCountry)).setText(responseList.getResponses().get(0).getCountry());
                ((EditText) findViewById(R.id.textState)).setText(responseList.getResponses().get(0).getStateProvince());
                ((EditText) findViewById(R.id.textPostalCode)).setText(responseList.getResponses().get(0).getPostalCode());
                ((EditText) findViewById(R.id.textFirmName)).setText(responseList.getResponses().get(0).getFirmname());

                String block_address = responseList.getResponses().get(0).getBlockAddress();
                address = responseList.getResponses().get(0).getAddressLine1() + " " + responseList.getResponses().get(0).getCity() + " " + responseList.getResponses().get(0).getStateProvince() + " " + responseList.getResponses().get(0).getPostalCode() ;
                country = responseList.getResponses().get(0).getCountry();

                intent.putExtra("Address",address);
                intent.putExtra("Block_Address",block_address);

                startActivity(intent);
            }

            @Override
            public void onRequestStart() {
                Log.d("Request initiated");
            }

            @Override
            public void onFailure(ErrorResponse e) {
                Log.d("Error: " + e.getRootErrorMessage());
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context = this;
        Button validateButton = (Button) findViewById(R.id.buttonValidate);
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final String API_KEY = "<API_KEY>";
                final String SECRET = "<SECRET>";
                IdentifyServiceManager identifyServiceManager = IdentifyServiceManager.getInstance(context, API_KEY, SECRET);

                Address address2 = new Address();
                address2.setAddressLine1(((EditText) findViewById(R.id.textAddressLine1)).getText().toString());
                address2.setAddressLine2(((EditText) findViewById(R.id.textAddressLine2)).getText().toString());
                address2.setCity(((EditText) findViewById(R.id.textCity)).getText().toString());
                address2.setStateProvince(((EditText) findViewById(R.id.textState)).getText().toString());
                address2.setCountry(((EditText) findViewById(R.id.textCountry)).getText().toString());
                address2.setFirmName(((EditText) findViewById(R.id.textFirmName)).getText().toString());


                final ValidateMailingAddressProAPIResponseList validateMailingAddressResponse = new ValidateMailingAddressProAPIResponseList();
                identifyServiceManager.getIdentifyAddressService().validateMailingAddressPro(context, Arrays.asList(address2), null, validateMailingAddressProCallBack(validateMailingAddressResponse.getResponses(), null));
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
                ((TextView) findViewById(R.id.textOutput)).setText("Validate Address Response");
            }
        });

        /*Button demographicButton = (Button) findViewById(R.id.buttonDemographic);
        demographicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  new MyAsyncTask().execute();
               *//* final LIAPIGeoLifeServiceApi api = new LIAPIGeoLifeServiceApi();
                Demographics resp = null;

                try {
                    Log.d("GeoLife " + "getDemographicsByAddress");
                    Log.d("GeoLife " + address);
                    resp = api.getDemographicsByAddress(address, null, null, country);
                    Gson gson = new GsonBuilder().create();
                    String response1 = gson.toJson(resp);
                    Log.d(response1);
                } catch (ApiException e) {
                    e.printStackTrace();
                }*//*


                TextView view =  (TextView) findViewById(R.id.textOutput);
                view.setVisibility(View.INVISIBLE);
                ((TextView) findViewById(R.id.textOutput1)).setText("Demographic details -");
                TextView view1 =  (TextView) findViewById(R.id.textOutput1);
                view1.setVisibility(View.VISIBLE);

            }
        });*/
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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

}