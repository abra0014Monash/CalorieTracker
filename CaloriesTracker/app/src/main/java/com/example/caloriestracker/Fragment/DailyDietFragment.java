package com.example.caloriestracker.Fragment;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloriestracker.Entity.ConsumptionUser;
import com.example.caloriestracker.Entity.ConsumptionUserPK;
import com.example.caloriestracker.Entity.FoodInternal;
import com.example.caloriestracker.Entity.UserTable;
import com.example.caloriestracker.FoodFinder;
import com.example.caloriestracker.GoogleImageAPI;
import com.example.caloriestracker.R;
import com.example.caloriestracker.RestClient1;
import com.example.caloriestracker.SearchGoogleAPI;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class DailyDietFragment extends Fragment implements View.OnClickListener {
    private View view;
    private EditText ed_newfood;
    private Button addButton;
    private TextView tv_dispayfood;
    private ImageView iv_dispalyfood;
    Spinner categorySelector,foodSelector;
    String categorySelected;
    FoodInternal food;
    List<String> foodList;
    int userId;
    ConsumptionUser consumptionuser;
    Date currentTime = Calendar.getInstance().getTime();

    ConsumptionUser newConsumption = new ConsumptionUser();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set Variables and listener
        view = inflater.inflate(R.layout.fragment_daily_diet, container, false);

        SharedPreferences preferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        userId = preferences.getInt("userId", 1);

        categorySelector = (Spinner)view.findViewById(R.id.spinner_food_category);
        foodSelector = (Spinner)view.findViewById(R.id.spinner_food);
        ed_newfood = (EditText)view.findViewById(R.id.input_newfood);
        addButton = (Button)view.findViewById(R.id.b_add);
        tv_dispayfood = (TextView)view.findViewById(R.id.tv_displayFood);

        addButton.setOnClickListener(this);

        categorySelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                categorySelected = categorySelector.getItemAtPosition(i).toString();

                FetchFoodAsyncTask fetchReportAsyncTask = new FetchFoodAsyncTask();
                fetchReportAsyncTask.execute();
            }

            public void onNothingSelected(
                    AdapterView<?> adapterView) {

            }
        });



        return view;

    }

    @Override
    public void onClick(View v) {

        food = FoodFinder.foodFinder(ed_newfood.getText().toString());
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%     "+food.getName());

        SearchAsyncTask searchAsyncTask=new SearchAsyncTask();
        searchAsyncTask.execute(ed_newfood.getText().toString());

        ImageAsyncTask imageAsyncTask = new ImageAsyncTask();
        imageAsyncTask.execute(ed_newfood.getText().toString());

        CountFoodAsyncTAsk countFoodAsyncTAsk = new CountFoodAsyncTAsk();
        countFoodAsyncTAsk.execute();

    }

    private class FetchFoodAsyncTask extends AsyncTask<Void, Void, List<FoodInternal>> {
        @Override
        protected List<FoodInternal> doInBackground(Void... params) {

            Call<List<FoodInternal>> call = RestClient1.getAPI().getFoodObject(categorySelected);

            List<FoodInternal> food = null;
            try {

                food = call.execute().body();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return food;
        }
        @Override
        protected void onPostExecute(List<FoodInternal> food) {
            List<String> foodList = new ArrayList<String>();

            for (FoodInternal row : food) {

                foodList.add(row.getName());
            }
            setfoodArray(foodList);
        }
    }
    public void setfoodArray(List<String> foodList){
        this.foodList = foodList;
        gettingFoodSpinnerData();


    }

    public void setFood(FoodInternal Food){
        this.food =  food;
    }

    public void gettingFoodSpinnerData(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String> (getActivity(),android.R.layout.simple_spinner_dropdown_item,foodList);

        foodSelector.setAdapter(dataAdapter);
        foodSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            int counter = 0;
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3)
            {
                String food = " Selected " + parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), food, Toast.LENGTH_LONG).show();

                // adding to the consupmtion table after checking if record exsists

                System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$dssdsds        "+parent.getItemAtPosition(position).toString());


                FindByNameAsyncTask findByNameAsyncTask = new FindByNameAsyncTask();
                findByNameAsyncTask.execute(parent.getItemAtPosition(position).toString());


            }

            public void onNothingSelected(AdapterView<?> arg0)
            {

            }
        });
    }

    private class CountFoodAsyncTAsk extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            Call<String> call = RestClient1.getAPI().CountFoodItems();
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
            setNewFoodID(newValue);

            AddFoodAsyncTask addFoodAsyncTask = new AddFoodAsyncTask();
            addFoodAsyncTask.execute(food);

        }

    }

    public class AddFoodAsyncTask extends AsyncTask<FoodInternal, Void, String>
    {
        @Override
        protected String doInBackground(FoodInternal... params) {


            Call<Void> call = RestClient1.getAPI().createFood(params[0]);
            int response = 0;
            try {
                response = call.execute().code();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Food was added";
        }

    }

    private class SearchAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return SearchGoogleAPI.search(params[0], new String[]{"num"}, new
                    String[]{"1"});
        }
        @Override
        protected void onPostExecute(String result) {

            tv_dispayfood.setText(SearchGoogleAPI.getSnippet(result));
        }
    }

    private class ImageAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return GoogleImageAPI.search(params[0], new String[]{"num"}, new
                    String[]{"1"});
        }
        @Override
        protected void onPostExecute(String result) {

            iv_dispalyfood = (ImageView)view.findViewById(R.id.iv_displayfood);
            URL url = null;
            try {
                url = new URL(GoogleImageAPI.getImage(result));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Bitmap obj = null;
            try {
                obj = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            iv_dispalyfood.setImageBitmap(obj);

        }
    }

    public class AddConsumptionAsyncTask extends AsyncTask<ConsumptionUser, Void, String>
    {
        @Override
        protected String doInBackground(ConsumptionUser... params) {


            Call<Void> call = RestClient1.getAPI().createConsumption(params[0]);
            int response = 0;
            try {
                response = call.execute().code();
                System.out.println("$$$$$$$$$$$$$$$$$$$$$  while creating consumption  "+response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Data added to Consumption User";
        }
        @Override
        protected void onPostExecute(String response) {

        }
    }

    private class FetchConsumptionAsyncTask extends AsyncTask<Integer,Void,List<ConsumptionUser>> {
        @Override
        protected List<ConsumptionUser> doInBackground(Integer... params) {


            Call<List<ConsumptionUser>> call = null;
            try {
                System.out.println("=========value of food id while fettching list========"+params[0]);
                call = RestClient1.getAPI().getConsumptionObject(userId, getCurrentDate(),params[0]);

            } catch (ParseException e) {
                e.printStackTrace();
            }
           List<ConsumptionUser> consumptionuser = null;
            try {
                Response<List<ConsumptionUser>> ConsumptionResponse = call.execute();

                consumptionuser = ConsumptionResponse.body();
                System.out.println("=======================here    ========================="+consumptionuser);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return consumptionuser;
        }
        @Override
        protected void onPostExecute(List<ConsumptionUser> consumptionuser) {

            if(!consumptionuser.isEmpty()) {
                setConsumption(consumptionuser.get(0));
                UpdateConsumption();
            }
            else
              AddConsumption();
        }
    }

    public void setConsumption(ConsumptionUser consumptionuser){
        this.consumptionuser = consumptionuser;

    }

    private class FindByNameAsyncTask extends AsyncTask<String, Void, FoodInternal> {
        @Override
        protected FoodInternal doInBackground(String... params) {

            Call<List<FoodInternal>> call = RestClient1.getAPI().getFood(params[0]);

            List<FoodInternal> food = null;
            try {

                food = call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return food.get(0);
        }
        @Override
        protected void onPostExecute(FoodInternal foodToAdd) {

            System.out.println("%%%%%%%%%%%%%   in post execute %%%%%%%%%%%%%%%"+foodToAdd.getName());

            newConsumption.setFood(foodToAdd);

            GetUserObjectAsyncTask getUserObjectAsyncTask = new GetUserObjectAsyncTask();
            getUserObjectAsyncTask.execute(userId);

        }
    }

        public String getCurrentDate() throws ParseException {

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = dateFormat.format(currentTime);
            return strDate;
        }

    private class GetUserObjectAsyncTask extends AsyncTask<Integer, Void, UserTable> {
        @Override
        protected UserTable doInBackground(Integer... params) {

            Call<UserTable> call = RestClient1.getAPI().getUser(userId);
            UserTable user = null;
            try {
                Response<UserTable> userResponse = call.execute();

                user = userResponse.body();

            } catch (IOException e) {
                e.printStackTrace();
            }

           return user;
        }
        @Override
        protected void onPostExecute(UserTable user) {

            newConsumption.setUserTable(user);
            FetchConsumptionAsyncTask fetchConsumptionAsyncTask = new FetchConsumptionAsyncTask();
            fetchConsumptionAsyncTask.execute(newConsumption.getFood().getId());

        }
    }

    private  void setNewFoodID(int id) {
            this.food.setId(id);
    }

    public void UpdateConsumption(){

        getNewConsumption();

        BigDecimal quantity = BigDecimal.valueOf(1.0);

            newConsumption.setQuantity(consumptionuser.getQuantity().add(quantity));
            System.out.println("===========foood ==============="+newConsumption.getFood().getName());
            UpdateConsumptionAsyncTask updateConsumptionAsyncTask = new UpdateConsumptionAsyncTask();
            updateConsumptionAsyncTask.execute(newConsumption);

        System.out.println("DATA added in the consumption table");


    }

    public void AddConsumption(){

        getNewConsumption();
        BigDecimal quantity = BigDecimal.valueOf(1.0);
        newConsumption.setQuantity(quantity);

            AddConsumptionAsyncTask addConsumptionAsyncTask = new AddConsumptionAsyncTask();
            addConsumptionAsyncTask.execute(newConsumption);

            System.out.println("DATA added in the consumption table");
    }

    public void getNewConsumption(){
        ConsumptionUserPK consumptionuserPK = new ConsumptionUserPK();

        consumptionuserPK.setDate(currentTime);
        consumptionuserPK.setUserid(userId);
        consumptionuserPK.setFoodid(newConsumption.getFood().getId());
        newConsumption.setConsumptionUserPK(consumptionuserPK);
    }
    public class UpdateConsumptionAsyncTask extends AsyncTask<ConsumptionUser, Void, String> {
        @Override
        protected String doInBackground(ConsumptionUser... params) {

            ConsumptionUser consumptionUser = params[0];
            Call<Void> call = null;

            call = RestClient1.getAPI().updateConsumption(userId, consumptionUser.getConsumptionUserPK().getDate().toString(),
                        consumptionUser.getFood().getId().toString(), consumptionUser);

            int response = 0;
            try {
                response = call.execute().code();
                System.out.println("$$$$$$$$$$$$$$$$$$$$$  while updating consumption  " + response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Data updated to report was added";
        }
    }




}
