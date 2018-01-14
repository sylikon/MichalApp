package com.maciek.onebuttonweb;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LocalizationPreference extends AppCompatActivity implements View.OnClickListener {

    private TextView textView_pref_location;
    private EditText editText_pref_location;
    private Button button_pref_location;
    public SharedPreferences sharedPreferences;
    public static final String MY_PREFERENCES = "MyPrefs";
    public static final String LOCATION_KEYS = "locationKeys";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localization_preference);

        textView_pref_location = (TextView)findViewById(R.id.textView_pref_location);
        editText_pref_location = (EditText)findViewById(R.id.editText_pref_location);
        button_pref_location = (Button)findViewById(R.id.button_pref_location);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        if(sharedPreferences!=null){
            textView_pref_location.setText(sharedPreferences.getString(LOCATION_KEYS, "bip bop"));
        }
        button_pref_location.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_pref_location){
            String tempLoc = editText_pref_location.getText().toString().trim();
            SharedPreferences.Editor editor = sharedPreferences.edit();

            if(tempLoc!=null){
                if(!tempLoc.equals("")){
                    editor.putString(LOCATION_KEYS, tempLoc);
                    editor.commit();
                    textView_pref_location.setText(tempLoc);
                    editText_pref_location.getText().clear();
                }
            }else{
                Toast.makeText(this, "uzupełnij pole", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
