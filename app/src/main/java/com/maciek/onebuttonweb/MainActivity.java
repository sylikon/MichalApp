package com.maciek.onebuttonweb;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class MainActivity extends AppCompatActivity{

    private Button button;
    public static final String EXTRA_MESSAGE = "com.maciek.onebuttonweb";

    private Toolbar toolbar;

    String scannedData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = (WebView)findViewById(R.id.webview);
        String url = "http://www.wirtualnachoinka.pl";
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(url);

        /*

            Dodajemy actionBar (ten szajs na ktorym bedzie lokalizacja i cykanie fotek)
                    znajdujemy odwolanie do elementu w leyoucie, musimy dodawac do kazdego
                    osobno jesli uzywamy tej metody.

         */

        toolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    /*

    Zarządzenie kliknięciami w elementy menu(action Bar)
        mamy dwa elementy, ustawienia lokalizacji i qr scanner

     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_settings:
                startActivity(new Intent(this, LocalizationPreference.class));
                return true;
            case R.id.action_qrscanner:
                IntentIntegrator integrator = new IntentIntegrator(this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setBeepEnabled(false);
                integrator.setCameraId(0);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null) {
            scannedData = result.getContents();
            if(scannedData!=null){
                Intent intent = new Intent(this, DatabaseHandler.class);
                intent.putExtra(EXTRA_MESSAGE, scannedData);
                startActivity(intent);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }




}




