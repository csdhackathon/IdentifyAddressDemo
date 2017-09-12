package com.example.sa005gu.swaggersdksamples;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pb.identify.utils.Log;

import pb.ApiClient;
import pb.ApiException;
import pb.Configuration;
import pb.locationintelligence.LIAPIGeoLifeServiceApi;
import pb.locationintelligence.model.Demographics;
import pb.locationintelligence.model.DemographicsThemes;


public class ResultActivity extends AppCompatActivity {

    String Addressvalidated;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Addressvalidated = getIntent().getStringExtra("Address");
        String block_add = getIntent().getStringExtra("Block_Address");

        TextView textView = (TextView) findViewById(R.id.Validated_Address_Value);
        textView.setText(block_add);

        Log.d(Addressvalidated);

        new ResultActivity.MyAsyncTask().execute();

    }

    private class MyAsyncTask extends AsyncTask {
        @Override
        protected void onPostExecute(Object dem) {
            //super.onPostExecute(resp);
            Demographics resp = (Demographics) dem;
            Gson gson = new GsonBuilder().create();
            String response1 = gson.toJson(resp);
            DemographicsThemes themes = resp.getThemes();
            String Population_adult_count = themes.getAgeTheme().getIndividualValueVariable().get(1).getName();
            String Population_adult_count_value = themes.getAgeTheme().getIndividualValueVariable().get(1).getValue();
            String Population_range = themes.getAgeTheme().getRangeVariable().get(0).getField().get(5).getDescription();
            String Population_range_value = themes.getAgeTheme().getRangeVariable().get(0).getField().get(5).getValue();
            String Female_population_count = themes.getGenderTheme().getIndividualValueVariable().get(1).getName();
            String Female_population_count_value = themes.getGenderTheme().getIndividualValueVariable().get(1).getValue();
            String Male_population_count = themes.getGenderTheme().getIndividualValueVariable().get(0).getName();
            String Male_population_count_value = themes.getGenderTheme().getIndividualValueVariable().get(0).getValue();
            String Household_Average_Income = themes.getIncomeTheme().getIndividualValueVariable().get(1).getValue();

            //TextView textView = (TextView) findViewById(R.id.Validated_Address_Value);
            ((TextView) findViewById(R.id.textFemale_population_count)).setText(Female_population_count_value);
            ((TextView) findViewById(R.id.textMale_population_count)).setText(Male_population_count_value);
            ((TextView) findViewById(R.id.textPopulation_adult_count)).setText(Population_adult_count_value);
            ((TextView) findViewById(R.id.textPopulation_range)).setText(Population_range_value);
            ((TextView) findViewById(R.id.textHousehold_Average_Income)).setText(Household_Average_Income);

            Log.d(Population_adult_count);
        }

        @Override
        protected Demographics doInBackground(Object[] params) {
            ApiClient defaultClient = Configuration.getDefaultApiClient();
            defaultClient.setoAuthApiKey("<API_KEY>");
            defaultClient.setoAuthSecret("<SECRET>");

            final LIAPIGeoLifeServiceApi api = new LIAPIGeoLifeServiceApi();


            String profile = null;
            String filter = null;


            Demographics resp = null;

            try {
                Log.d("GeoLife" + "getDemographicsByAddress" + Addressvalidated);
                resp = api.getDemographicsByAddress(Addressvalidated, profile, filter, "USA");

                return resp;
            } catch (ApiException e) {
                e.printStackTrace();
                Log.d(e.getResponseBody());
                return null;
            }

        }




        @Override
        protected void onPreExecute() {}



    }

}

