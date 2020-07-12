package com.example.caloriestracker;

import com.example.caloriestracker.Entity.FoodInternal;
import com.fatsecret.platform.model.CompactFood;


import com.fatsecret.platform.model.Food;
import com.fatsecret.platform.services.FatsecretService;
import com.fatsecret.platform.services.Response;


public class FoodFinder {
    //static FoodInternal foodInternal = null;

    public static FoodInternal foodFinder(String foodToAdd){

        FoodInternal foodInternal = new FoodInternal();

    String key = "31e611da66404c20932f92a88a8ddcaf";

    String secret = "7a12a062c72e41949100a41e0d955da3";

    FatsecretService service = new FatsecretService(key, secret);

    Response<CompactFood> response = service.searchFoods(foodToAdd);

 //   Response<CompactRecipe> response1 = service.searchRecipes(foodToAdd);

   // List<CompactRecipe> recipeList = response1.getResults();

//        for (CompactRecipe item: recipeList) {
//            Recipe responseRecipe = service.getRecipe(item.getId());
//
//            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$ name ........."+responseRecipe.getName());
//
//            List<Category> categories = responseRecipe.getCategories();
//            for (Category category: categories )
//                System.out.println("\t%%%%%%%%%%%$$$ category ........."+ category.getName());
//        }

        //System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$ category ........."+responseRecipe.getCategories().get(0).getName());
    Food food = service.getFood(response.getResults().get(0).getId());



        //foodInternal.setId(10); // get the count from food table and increment 1 for id
        foodInternal.setName(response.getResults().get(0).getName());
        foodInternal.setCategory(response.getResults().get(0).getType());
        foodInternal.setCalorieamount(food.getServings().get(0).getCalories().intValue());
        foodInternal.setServingunit(food.getServings().get(0).getMetricServingUnit());
        foodInternal.setServingamount(food.getServings().get(0).getMetricServingAmount());
        foodInternal.setFat(food.getServings().get(0).getFat().intValue());

System.out.println(" Food Object created ........................."+foodInternal.getName());
        return foodInternal;
    }


}
