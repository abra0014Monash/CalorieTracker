package com.example.caloriestracker.Fragment;

import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.caloriestracker.DailySteps;
import com.example.caloriestracker.DailyStepsDatabase;
import com.example.caloriestracker.Entity.Report;
import com.example.caloriestracker.Entity.ReportPK;
import com.example.caloriestracker.Entity.UserTable;
import com.example.caloriestracker.R;
import com.example.caloriestracker.RestClient1;
import com.google.gson.JsonArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class TrackCalorieFragment extends Fragment implements View.OnClickListener {
    View view;
    TextView tv_getgoal , tv_getsteps, tv_getCalorieConsumed,tv_getCalorieBurned;
    Button b_addreport;
    DailyStepsDatabase db = null;
    Date currentTime;
    String currentDate;
    int userId = 0;
    int totalStepsTaken = 0;
    Report report;
    int CalorieGoal;
    UserTable user;
    BigDecimal totalCaloriesConsumed;
    BigDecimal totalCaloriesBurned;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set Variables and listener
        view = inflater.inflate(R.layout.fragment_track_calorie, container, false);
        db = Room.databaseBuilder(getActivity().getApplicationContext(),
                DailyStepsDatabase.class, "DailyStepsDatabase")
                .fallbackToDestructiveMigration()
                .build();

        tv_getgoal = (TextView)view.findViewById(R.id.tv_getgoal);
        tv_getsteps = (TextView)view.findViewById(R.id.tv_getsteps);
        tv_getCalorieConsumed = (TextView)view.findViewById(R.id.tv_getCalCons);
        tv_getCalorieBurned = (TextView)view.findViewById(R.id.tv_getCalBurn);
        b_addreport = (Button)view.findViewById(R.id.b_addreport);

        b_addreport.setOnClickListener(this);

        try {
            currentDate = getCurrentDate();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SharedPreferences pref_goal = getActivity().getSharedPreferences("CalorieGoal", MODE_PRIVATE);
        CalorieGoal = pref_goal.getInt("CalorieGoal", 1);

        SharedPreferences pref_userid = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        userId = pref_userid.getInt("userId", 1);


        tv_getgoal.setText("Calories Goal : "+CalorieGoal);

        FindByDateAsyncTask findByDateAsyncTask = new FindByDateAsyncTask();
        findByDateAsyncTask.execute();

        GetUserObject getUserObject = new GetUserObject();
        getUserObject.execute();

        GetCaloriesAyncTask getCaloriesAyncTask = new GetCaloriesAyncTask();
        getCaloriesAyncTask.execute();

        GetCaloriesBurnedPerStepAyncTask getCaloriesBurnedPerStepAyncTask = new GetCaloriesBurnedPerStepAyncTask();
        getCaloriesBurnedPerStepAyncTask.execute();



        return view;
    }

    public void addReport(){
        report = new Report();
        ReportPK reportPK = new ReportPK();
        reportPK.setUserid(userId);
        reportPK.setDate(currentTime);
        report.setReportPK(reportPK);
        report.setCaloriegoal(BigDecimal.valueOf(CalorieGoal).movePointLeft(2));
        report.setTotalcalorieconsumed(totalCaloriesConsumed);
        report.setTotalcalorieburned(totalCaloriesBurned);
        report.setTotalstepstaken(totalStepsTaken);
        report.setUserTable(user);



    }

    @Override
    public void onClick(View v) {
        addReport();
        PostAsyncTaskReport postAsyncTaskReport = new PostAsyncTaskReport();
        postAsyncTaskReport.execute(report);
    }


    private class FindByDateAsyncTask extends AsyncTask<Void, Void, List<DailySteps>> {
        @Override
        protected List<DailySteps> doInBackground(Void... params) {


            List<DailySteps> stepsList = db.dailyStepsDao().getAll();



            return stepsList;

        }
        @Override
        protected void onPostExecute(List<DailySteps> stepsList)
        {
           ShowListView(stepsList);
        }
    }

    public void ShowListView(List<DailySteps> stepsList){
        totalStepsTaken = 0;
        if (stepsList != null) {
            for(DailySteps item : stepsList) {

                totalStepsTaken = totalStepsTaken + item.getStepsTaken();
               tv_getsteps.setText("Total Steps taken : "+totalStepsTaken);
            }
        }

    }

    private class GetCaloriesAyncTask extends AsyncTask<Void, Void, Double> {
        @Override
        protected Double doInBackground(Void... params) {

            Call<Object> call = RestClient1.getAPI().calculateTotalCalorieConsumed(userId, currentDate);
            String response = null;
            try {
                response = call.execute().body().toString();

            } catch (IOException e) {
                e.printStackTrace();
            }

            Double totalCalorieConsumed = 0.0;
            try {
                JSONObject obj = new JSONObject(response);

                totalCalorieConsumed = obj.getDouble("totalCalorieConsumedPerDay");        //parsing json to string
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return totalCalorieConsumed;

        }

        @Override
        protected void onPostExecute(Double totalCalorieConsumed )
        {
            tv_getCalorieConsumed.setText("Total Calories Consumed : "+totalCalorieConsumed);
            totalCaloriesConsumed = (BigDecimal.valueOf(totalCalorieConsumed));
        }
    }

    private class GetCaloriesBurnedPerStepAyncTask extends AsyncTask<Void, Void, Double> {
        @Override
        protected Double doInBackground(Void... params) {

            Call<Object> call = RestClient1.getAPI().caloriesBurnedPerStep(userId);
            String response = null;
            try {
                response = call.execute().body().toString();

            } catch (IOException e) {
                e.printStackTrace();
            }

            Double calorieBurnedPerStep = 0.0;
            try {
                JSONObject obj = new JSONObject(response);

                calorieBurnedPerStep = obj.getDouble("CaloriesBurnedPerStep");        //parsing json to string
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return calorieBurnedPerStep;

        }

        @Override
        protected void onPostExecute(Double calorieBurnedPerStep )
        {
            double roundedCalories = Double.valueOf(roundDouble(calorieBurnedPerStep));
            tv_getCalorieBurned.setText("Total Calories burned : "+(roundedCalories *totalStepsTaken));
            totalCaloriesBurned = BigDecimal.valueOf(roundedCalories *totalStepsTaken);
            //addReport();
        }
    }

    public class PostAsyncTaskReport extends AsyncTask<Report, Void, String>
    {
        @Override
        protected String doInBackground(Report... params) {


            Call<Void> call = RestClient1.getAPI().createReport(params[0]);
            int response = 0;
            try {
                response = call.execute().code();
                System.out.println("$$$$$$$$$$$$$$$$$$$$$  while creating report  "+response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Data added to report";
        }
        @Override
        protected void onPostExecute(String response) {

        }
    }

    private class GetUserObject extends AsyncTask<Void, Void, UserTable> {
        @Override
        protected UserTable doInBackground(Void... params) {
            Call<UserTable> call = RestClient1.getAPI().getUser(userId);
            //UserTable user = null;
            try {
                Response<UserTable> userResponse = call.execute();

                user = userResponse.body();

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("==========user name ==========="+user.getName());
            return user;
        }
    }
    public String getCurrentDate() throws ParseException {
        currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(currentTime);
        return strDate;
    }

    public String roundDouble(Double value){
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        String roundedvalue = df.format(value);
        System.out.println("============rounded value=============="+roundedvalue);
         return roundedvalue;
    }
}
