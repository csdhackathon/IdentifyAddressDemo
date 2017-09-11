package com.example.sa005gu.swaggersdksamples;

import com.example.sa005gu.swaggersdksamples.R;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity {


    private RequestObserver validateMailingAddressProCallBack(final List<ValidateMailingAddressProAPIResponse> responses,
                                                              final ErrorResponse[] errorResponses) {
        return new RequestObserver<ValidateMailingAddressProAPIResponseList>() {
            @Override
            public void onSucess(ValidateMailingAddressProAPIResponseList responseList){
                TextView textView = (TextView) findViewById(R.id.textOutput);
                Gson gson = new GsonBuilder().create();

                String response = gson.toJson(responseList);

                ((EditText) findViewById(R.id.textAddressLine1)).setText(responseList.getResponses().get(0).getAddressLine1());
                ((EditText) findViewById(R.id.textAddressLine2)).setText(responseList.getResponses().get(0).getAddressLine2());
                ((EditText) findViewById(R.id.textCity)).setText(responseList.getResponses().get(0).getCity());
                ((EditText) findViewById(R.id.textCountry)).setText(responseList.getResponses().get(0).getCountry());
                ((EditText) findViewById(R.id.textState)).setText(responseList.getResponses().get(0).getStateProvince());
                ((EditText) findViewById(R.id.textPostalCode)).setText(responseList.getResponses().get(0).getPostalCode());
                ((EditText) findViewById(R.id.textFirmName)).setText(responseList.getResponses().get(0).getFirmname());
                textView.setText(responseList.getResponses().get(0).getBlockAddress());
            }

            @Override
            public void onRequestStart(){
                Log.d("Request initiated");
            }

            @Override
            public void onFailure(ErrorResponse e) {
                Log.d("Error: "+e.getRootErrorMessage());
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
                final String API_KEY = "API_KEY";
                final String SECRET = "SECRET";
                IdentifyServiceManager identifyServiceManager = IdentifyServiceManager.getInstance(context,API_KEY,SECRET);

                Address address2 = new Address();
                address2.setAddressLine1(((EditText) findViewById(R.id.textAddressLine1)).getText().toString());
                address2.setAddressLine2(((EditText) findViewById(R.id.textAddressLine2)).getText().toString());
                address2.setCity(((EditText) findViewById(R.id.textCity)).getText().toString());
                address2.setStateProvince(((EditText) findViewById(R.id.textState)).getText().toString());
                address2.setCountry(((EditText) findViewById(R.id.textCountry)).getText().toString());
                address2.setFirmName(((EditText) findViewById(R.id.textFirmName)).getText().toString());

                final ValidateMailingAddressProAPIResponseList validateMailingAddressResponse = new ValidateMailingAddressProAPIResponseList();
                identifyServiceManager.getIdentifyAddressService().validateMailingAddressPro(context, Arrays.asList(address2),null,validateMailingAddressProCallBack(validateMailingAddressResponse.getResponses(), null));
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
    }
}
