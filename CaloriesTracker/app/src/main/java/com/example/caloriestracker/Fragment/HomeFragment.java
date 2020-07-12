package com.example.caloriestracker.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.caloriestracker.Entity.Report;
import com.example.caloriestracker.Entity.ReportPK;
import com.example.caloriestracker.Entity.UserTable;
import com.example.caloriestracker.R;
import com.example.caloriestracker.RestClient1;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment
{

    Button button_set,button_edit;
    EditText input_goal;
    TextView textview1,textview2,tv_showGoal;

    int userId;
    Report report = null;
    ReportPK reportPK = null;
    Date currentTime;
    View view;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Set Variables and listener
            view = inflater.inflate(R.layout.fragment_home, container, false);

            SharedPreferences preferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
            userId = preferences.getInt("userId", 1);

            final Report report_new = new Report();
            reportPK = new ReportPK();

            currentTime = Calendar.getInstance().getTime();
            textview2 = (TextView)view.findViewById(R.id.textview2);
            textview2.setText(currentTime.toString());

            button_set = (Button)view.findViewById(R.id.button_set);
            button_edit = (Button) view.findViewById(R.id.button_edit);
            input_goal = (EditText)view.findViewById(R.id.input_goal);
            tv_showGoal = (TextView) view.findViewById(R.id.tv_showGoal);

            input_goal.setVisibility(View.VISIBLE);
            button_set.setVisibility(View.VISIBLE);
            button_edit.setVisibility(View.GONE);

            GetUserFirstName task = new GetUserFirstName();
            task.execute(userId);

            button_set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_showGoal.setText("goal set to"+input_goal.getText().toString());
                    input_goal.setVisibility(View.GONE);
                    button_set.setVisibility(View.GONE);
                    tv_showGoal.setVisibility(View.VISIBLE);
                    button_edit.setVisibility(View.VISIBLE);

                    int CalorieGoal = Integer.valueOf(input_goal.getText().toString());
                    SharedPreferences prefs = getActivity().getSharedPreferences("CalorieGoal", Context.MODE_PRIVATE);
                    prefs.edit().putInt("CalorieGoal", CalorieGoal).commit();

                    int valueInInteger = Integer.valueOf(input_goal.getText().toString());
                    BigDecimal bigDecimalValue= BigDecimal.valueOf(valueInInteger);

                    report_new.setCaloriegoal(bigDecimalValue);

                    reportPK.setUserid(userId);
                    reportPK.setDate(currentTime);
                    report_new.setReportPK(reportPK);


                    if(report == null) {
                        report_new.setTotalstepstaken(0);
                        report_new.setTotalcalorieburned(BigDecimal.valueOf(0.0));
                        report_new.setTotalcalorieconsumed(BigDecimal.valueOf(0.0));
                        PostAsyncTaskReport postAsyncTaskReport = new PostAsyncTaskReport();
                        postAsyncTaskReport.execute(report_new);
                    }
                    else {
                        report_new.setTotalstepstaken(report.getTotalstepstaken());
                        report_new.setTotalcalorieburned(report.getTotalcalorieburned());
                        report_new.setTotalcalorieconsumed(report.getTotalcalorieconsumed());

                        EditGoalAsyncTask editGoalAsyncTask = new EditGoalAsyncTask();
                        editGoalAsyncTask.execute(report_new);
                    }
                }

            });

            button_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    input_goal.setVisibility(View.VISIBLE);
                    button_set.setVisibility(View.VISIBLE);
                    tv_showGoal.setVisibility(View.GONE);
                    button_edit.setVisibility(View.GONE);

                }

            });


        return view;
    }




    private class GetUserFirstName extends AsyncTask<Integer, Void, String> {
                @Override
                protected String doInBackground(Integer... params) {
                    System.out.println("$$$$$$$$$$$$$$ user id in get username   "+userId);
                    Call<UserTable> call = RestClient1.getAPI().getUser(userId);
                    UserTable user = null;
                    try {
                        Response<UserTable> userResponse = call.execute();

                        user = userResponse.body();
                        System.out.println("     result based on user id..........."+user);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String firstName = user.getName();
                    System.out.println("$$$$$$$$$ firstname      "+firstName);

                    return firstName;
                }
                @Override
                protected void onPostExecute(String firstName) {
                    textview1 = (TextView)view.findViewById(R.id.textview1);
                    textview1.setText(" WELCOME " + firstName);

                    //System.out.println(" date  "),
                    FetchReportAsyncTask fetchCalorieGoalAsyncTask = new FetchReportAsyncTask();
                    fetchCalorieGoalAsyncTask.execute();

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

            public class EditGoalAsyncTask extends AsyncTask<Report, Void, String>
            {
                @Override
                protected String doInBackground(Report... params) {


                    Call<Void> call = null;
                    try {
                        call = RestClient1.getAPI().editGoal(userId,getCurrentDate(),params[0]);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    int response = 0;
                    try {
                        response = call.execute().code();
                        System.out.println("$$$$$$$$$$$$$$$$$$$$$  while updating report  "+response);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return "Data updated to report was added";
                }
                @Override
                protected void onPostExecute(String response) {

                }
            }

            private class FetchReportAsyncTask extends AsyncTask<Void, Void, Report> {
                @Override
                protected Report doInBackground(Void... params) {


                    Call<Report> call = null;
                    try {
                        System.out.println("##################         date ........"+getCurrentDate());
                        call = RestClient1.getAPI().getReportObject(userId, getCurrentDate());
                        //System.out.println("$$$$$$$$$$$ date after conversion ......."+currentDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Report report = null;
                    try {
                        Response<Report> ReportResponse = call.execute();

                        report = ReportResponse.body();
                        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%    "+report);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return report;
                }
                @Override
                protected void onPostExecute(Report report) {

                    setReport(report);

                }
            }

            public void setReport(Report report){
                this.report = report;
            }

            public String getCurrentDate() throws ParseException {
                currentTime = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String strDate = dateFormat.format(currentTime);
                return strDate;
            }
}
