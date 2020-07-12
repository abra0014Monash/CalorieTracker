package com.example.caloriestracker;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.caloriestracker.Entity.CredentialTable;
import com.example.caloriestracker.Entity.UserTable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
CalendarView cv;
TextView tv_display;
EditText firstName,lastName,email,dateOfBirth,height,weight,address,postCode,stepsPerMile,username,password;
Button registerButton;
RadioGroup radioGender;
RadioButton gender;
Spinner activitySelector;
String activity;
int month,day,year;
int id;
CredentialTable credentialTable = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstName = (EditText) findViewById(R.id.input_fname);
        lastName = (EditText) findViewById(R.id.input_lname);
        email = (EditText)findViewById(R.id.input_email);
        cv = (CalendarView) findViewById(R.id.calendar);
        dateOfBirth = (EditText) findViewById(R.id.input_dob);
        height = (EditText) findViewById(R.id.input_height);
        weight = (EditText) findViewById(R.id.input_weight);
        radioGender = (RadioGroup) findViewById(R.id.radioGender);
        activitySelector = (Spinner)findViewById(R.id.spinner_activity);
        address = (EditText) findViewById(R.id.input_address);
        postCode = (EditText) findViewById(R.id.input_postCode);
        stepsPerMile = (EditText) findViewById(R.id.input_stepsPerMile);
        username = (EditText) findViewById(R.id.input_username);
        password = (EditText) findViewById(R.id.input_password);
        registerButton = (Button) findViewById(R.id.RegisterButton);
        registerButton.setOnClickListener(this);

        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView cv, int i, int i1, int i2) {
                String Date = (i1 + 1) + "/" + i2 + "/" + i;
                month = (i1 + 1);
                day = i2;
                year = i;
                dateOfBirth.setText(Date);
            }
        });

        // radio button handling
        int selectedId = radioGender.getCheckedRadioButtonId();
        gender = findViewById(selectedId);


        activitySelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    activity = activitySelector.getItemAtPosition(i).toString();
                    }

            public void onNothingSelected(
                    AdapterView<?> adapterView) {

            }
        });


    }



        @Override
        public void onClick(View v) {


         UserTable user=new UserTable();

            user.setName(firstName.getText().toString());
            user.setSurname(lastName.getText().toString());
            user.setEmail(email.getText().toString());
            Date DateOfBirth = DateHandling(year,month,day);
            user.setDob(DateOfBirth);
            BigDecimal bigDecimalHeight= handlingBigDecimal(height.getText().toString());
            user.setHeight(bigDecimalHeight);
            BigDecimal bigDecimalWeight= handlingBigDecimal(weight.getText().toString());
            user.setWeight(bigDecimalWeight);
            user.setGender(gender.getText().toString());
            user.setAddress(address.getText().toString());
            user.setPostcode(postCode.getText().toString());
            Integer intActivity = handlingInterger(activity);
            user.setLevelofactivity(intActivity);
            Integer intStepsPerMile = handlingInterger(stepsPerMile.getText().toString());
            user.setStepspermile(intStepsPerMile);

         credentialTable = new CredentialTable();

         String passwordHash = PasswordHandling(password.getText().toString());

            credentialTable.setUsername(username.getText().toString());
            credentialTable.setPasswordhash(passwordHash);
            credentialTable.setSignupdate(DateOfBirth);
            credentialTable.setUserid(user);

            this.credentialTable = credentialTable;

            checkUsernameAsyncTask checkUser = new checkUsernameAsyncTask();
            checkUser.execute();


        }

    private  void setNewUserID(int id) {
        this.credentialTable.getUserid().setUserid(id);

        PostAsyncTaskUser postAsyncTaskUser = new PostAsyncTaskUser();
        postAsyncTaskUser.execute(this.credentialTable.getUserid());

    }

    public String PasswordHandling(String password)
    {
        String hashedPassword = PasswordHashing.getHashedPaswword(password);
        return hashedPassword;
    }

    public Date DateHandling(int year,int month,int day){
        Calendar dob = Calendar.getInstance();
        dob.set(year,month,day);
        return dob.getTime();
    }

    public BigDecimal handlingBigDecimal(String valueInString){
        int valueInInteger = Integer.valueOf(valueInString);
        BigDecimal bigDecimalValue= BigDecimal.valueOf(valueInInteger);
        return bigDecimalValue;
    }

    public Integer handlingInterger(String valueToBeConverted){
        Integer intValue = Integer.valueOf(valueToBeConverted);
        return intValue;
    }


    public class PostAsyncTaskCredentials extends AsyncTask<CredentialTable, Void, String>
    {
        @Override
        protected String doInBackground(CredentialTable... params) {


            Call<Void> call = RestClient1.getAPI().createCredentials(params[0]);
            int response = 0;
            try {
                response = call.execute().code();
                //System.out.println("$$$$$$$$$$$$$$$$$$$$$    "+response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "User was added";
        }
        @Override
        protected void onPostExecute(String response) {
            // resultTextView.setText(response);
        }
    }
    private class PostAsyncTaskCount extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            Call<String> call = RestClient1.getAPI().CountUsers();
            String response = "";
            try {
                response = call.execute().body().toString();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
        @Override
        protected void onPostExecute(String response) {
            int newValue = Integer.valueOf(response) + 1;
            setNewUserID(newValue);

        }
    }

        public class PostAsyncTaskUser extends AsyncTask<UserTable, Void, String>
    {
        @Override
        protected String doInBackground(UserTable... params) {


            Call<Void> call = RestClient1.getAPI().createUser(params[0]);
            int response = 0;
            try {
                response = call.execute().code();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "User was added";
        }
        @Override
        protected void onPostExecute(String response) {
            PostAsyncTaskCredentials postAsyncTaskCredentials = new PostAsyncTaskCredentials();
            postAsyncTaskCredentials.execute(credentialTable);
        }
    }

    private class checkUsernameAsyncTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {

            Call<Object> call = RestClient1.getAPI().CheckUsernameExsists(username.getText().toString());
            String response = null;
            try {
                response = call.execute().body().toString();

            } catch (IOException e) {
                e.printStackTrace();
            }

            Boolean exsists = false;
            try {
                JSONObject obj = new JSONObject(response);
                exsists = obj.getBoolean("exsists");        //parsing json to string
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return exsists;
        }

        @Override
        protected void onPostExecute(Boolean exsists) {

            if (exsists == false) {
                // get count for crential table and set userid
                // add credential table to backend
                getCountFromCredential();
            }
        }
    }

    public void getCountFromCredential(){
        PostAsyncTaskCount counter = new PostAsyncTaskCount();
        counter.execute();
    }


}




