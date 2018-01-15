package com.maciek.onebuttonweb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatabaseHandler extends AppCompatActivity implements View.OnClickListener {

    private String data;
    private ProgressBar progressBar;
    private TextView textView_record_details;
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private String qrcodeText;
    private Button button_cofirm_update;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        textView_record_details = (TextView)findViewById(R.id.tekst);
        Intent intent = getIntent();
        qrcodeText = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        button_cofirm_update = (Button)findViewById(R.id.button_confirm_update);
        new test().execute();
        button_cofirm_update.setOnClickListener(this);

        toolbar = (Toolbar)findViewById(R.id.my_toolbar_mainactivity_2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void updateDatabase(String qrcode, String location){

        String url_change_status = "http://android.x25.pl/insertdataprezent.php?qrcode=" + qrcode;
        /*
            Tak sie tego nie powinno robic, poki co tworze webview niewidoczny dla usera ktorym obsluguje url,
             byc moze pozniej wpadne na lepszy pomysl
         */
        WebView webView_status = (WebView)findViewById(R.id.webview_update_database);
        webView_status.loadUrl(url_change_status);
        
        WebView webView_location = (WebView)findViewById(R.id.webview_update_location);
        String url_change_location = "http://android.x25.pl/insertlocationprezent.php?lokalizacja=" +location +"&qrcode=" + qrcode;
        webView_location.loadUrl(url_change_location);
        
        new test.execute();

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_confirm_update){
            SharedPreferences sharedPreferences;
            sharedPreferences = getSharedPreferences(LocalizationPreference.MY_PREFERENCES, Context.MODE_PRIVATE);
            if(sharedPreferences==null){
                Toast.makeText(this, "Uzupełnij lokalizacje", Toast.LENGTH_SHORT).show();
            }else {
                String location = sharedPreferences.getString(LocalizationPreference.LOCATION_KEYS, "błąd");
                updateDatabase(qrcodeText,location);
                Toast.makeText(this, "zakutalizowano", Toast.LENGTH_SHORT).show();
            }
        }
    }


    class test extends AsyncTask<Void, Void, Void> {


        InputStream inputStream = null;
        String parsed;



        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url_fetch_qrcode = "http://android.x25.pl/fetchdataprezent.php?qrcode="+qrcode; 
                URL url = new URL(url_fetch_qrcode);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    data = data + line;
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.INVISIBLE);
            if (data != null) {

                try {
                    String crappyPrefix = "null";

                    if (data.startsWith(crappyPrefix)) {
                        data = data.substring(crappyPrefix.length(), data.length());
                    }


                    jsonObject = new JSONObject(data);
                    JSONArray weather = jsonObject.getJSONArray("dzieci");
                    
                    JSONObject w = weather.getJSONObject(0);
                        
                    parsed = w.getString("email") + "\n" +
                                            "wybrał dziecko : " + w.get("dziecko") + "\n"+
                                            "status: " + w.get("status") + "\n"+
                                            "odebrano w: " + w.get("lokalizacja");

                       

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                textView_record_details.setText("użytkownik z nikiem: " + parsed);

            }

        }

    }

}
