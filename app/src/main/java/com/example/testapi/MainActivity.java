package com.example.testapi;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "shared_prefs";

    public static final String EMAIL_KEY = "email_key";

    public static final String PASSWORD_KEY = "password_key";

    SharedPreferences sharedpreferences;

    String username, password;

    //widget
    EditText txtUsername;
    EditText txtPassword;
    Button btnLogin;
    ProgressBar pbLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //xml
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnPassword);
        pbLoadingBar = (ProgressBar) findViewById(R.id.pbloadingBar);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = txtUsername.getText().toString();
                password = txtPassword.getText().toString();
                pbLoadingBar.setVisibility(View.VISIBLE);
                btnLogin.setEnabled(false);

                //hit api
                AndroidNetworking.post("https://mediadwi.com/api/latihan/login")
                        .addBodyParameter("username", username)
                        .addBodyParameter("password", password)
                        .setTag("postRequest")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Handle success response
                                Log.d("sukses login", "onResponse"+response.toString());
                                try {
                                    boolean status = response.getBoolean("status");
                                    String message = response.getString("message");
                                    if (status){
                                        Toast.makeText(MainActivity.this,message, Toast.LENGTH_SHORT).show();
                                        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString(EMAIL_KEY, username.toString());
                                        editor.putString(PASSWORD_KEY, "");

                                        // to save our data with key and value.
                                        editor.apply();
                                        startActivity(new Intent(MainActivity.this, Homepage.class));
                                        finish();
                                    }else{
                                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            @Override
                            public void onError(ANError error) {
                                // Handle error response
                            }
                        });

            }
        });

    }
}