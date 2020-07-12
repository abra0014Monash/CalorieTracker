package com.example.caloriestracker;



import com.example.caloriestracker.Entity.ConsumptionUser;
import com.example.caloriestracker.Entity.CredentialTable;
import com.example.caloriestracker.Entity.FoodInternal;
import com.example.caloriestracker.Entity.Report;
import com.example.caloriestracker.Entity.UserTable;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RestClientInterface {

    //used in login activity to match the username and the password
    //method will return boolean value if username and password matches
    @GET("calorietracker.credentialtable/CheckUsernameAndPassword/{username}/{passwordhash}")
    Call<Object> checkCredentials(@Path("username") String username, @Path("passwordhash") String passwordhash);

    //finding user by Id
    @GET("calorietracker.usertable/findById/{userid}")
    Call<UserTable>getUser(@Path("userid") Integer id);

    //check if username exsist during register activity
    @GET("calorietracker.credentialtable/CheckUsernameExsists/{username}")
    Call<Object> CheckUsernameExsists(@Path("username") String username);

    @POST("calorietracker.usertable/")
    Call<Void> createUser(@Body UserTable user);

    @POST("calorietracker.food/")
    Call<Void> createFood(@Body FoodInternal food);

    @POST("calorietracker.credentialtable/")
    Call<Void> createCredentials(@Body CredentialTable credentialTable);

    @POST("calorietracker.report/")
    Call<Void> createReport(@Body Report report);

    @POST("calorietracker.consumptionuser/")
    Call<Void> createConsumption(@Body ConsumptionUser consumptionUser);

    @GET("calorietracker.credentialtable/count")
    Call<String> CountUsers();

    @GET("calorietracker.food/count")
    Call<String> CountFoodItems();

    @GET("calorietracker.report/getCalorieGoal/{userid}/{date}")
    Call<Report> getReportObject(@Path("userid") Integer userid,@Path("date") String date);

    @GET("calorietracker.consumptionuser/fetchConsumption/{userid}/{date}/{foodid}")
    Call<List<ConsumptionUser>> getConsumptionObject(@Path("userid") Integer userid,@Path("date") String date,@Path("foodid") Integer foodid);

    @GET("calorietracker.consumptionuser/CalculateTotalCalorieConsumed/{userid}/{date}")
    Call<Object> calculateTotalCalorieConsumed(@Path("userid") Integer userid,@Path("date") String date);

    @PUT("calorietracker.report/editGoal/{userid}/{date}")
    Call<Void> editGoal(@Path("userid") Integer id,@Path("date") String date, @Body Report report);

    @PUT("calorietracker.consumptionuser/updateConsumption/{userid}/{date}/{foodid}")
    Call<Void> updateConsumption(@Path("userid") Integer id,@Path("date") String date,@Path("foodid") String foodid,@Body ConsumptionUser consumptionuser);

    @GET("calorietracker.food/findByCategory/{category}")
    Call<List<FoodInternal>> getFoodObject(@Path("category") String category);

    @GET("calorietracker.food/findByName/{name}")
    Call<List<FoodInternal>> getFood(@Path("name") String name);

    @GET("calorietracker.usertable/CalculateCaloriesBurnedPerStep/{userid}")
    Call<Object>caloriesBurnedPerStep(@Path("userid") Integer id);
}

