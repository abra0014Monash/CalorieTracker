package com.example.caloriestracker;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;

import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private View vDisplayUnit;
    private Button bcheck, bregister;
    private EditText username, password;
    private TextView tv_display;
    int userId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bcheck = (Button) findViewById(R.id.bcheck);
        bregister = (Button) findViewById(R.id.bRegister);
        username = (EditText) findViewById(R.id.editText_username);
        password = (EditText) findViewById(R.id.editText_password);
        tv_display = (TextView) findViewById(R.id.tv_display);
        bcheck.setOnClickListener(this);
        bregister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bcheck:
                if (Validation.ValidateUsername(username.getText().toString()) == false) {
                    username.setError("username not valid");
                    return;
                }

                if (Validation.ValidatePassword(password.getText().toString()) == false) {
                    password.setError("password not valid");
                    return;
                }
                LoginActivity.CheckCredentialsAsyncTask checkUser = new LoginActivity.CheckCredentialsAsyncTask();
                checkUser.execute();
                break;


            case R.id.bRegister:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;


        }
    }


    // checking if the username and password matches in the database
    private class CheckCredentialsAsyncTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            String hashedPassword = PasswordHandling(password.getText().toString());

            Call<Object> call = RestClient1.getAPI().checkCredentials(username.getText().toString(), hashedPassword);

            String responseString = null;
            try {

                Object responseObject = call.execute().body();

                if (responseObject != null) {
                    responseString = responseObject.toString();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            Boolean status = false;
            if(responseString != null) {

                try {
                    JSONObject obj = new JSONObject(responseString);
                    status = obj.getBoolean("loginStatus");        //parsing json to string
                    userId = obj.getInt("userId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return status;
        }


        @Override
        protected void onPostExecute(Boolean isValid) {
            TextView resultTextView = (TextView) tv_display.findViewById(R.id.tv_display);
            resultTextView.setText(isValid ? "Login successful" : "Username or Passwords dont match !!! Try Again");
            if (isValid) {
                loginSuccess();

                    SharedPreferences prefs = getSharedPreferences("login", Context.MODE_PRIVATE);
                    prefs.edit().putInt("userId", userId).commit();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        }



    public void loginSuccess() {
        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        sp.edit().putBoolean("logged", true).apply();
    }

    public String PasswordHandling(String password)
    {
        String hashedPassword = PasswordHashing.getHashedPaswword(password);
        return hashedPassword;
    }
}

