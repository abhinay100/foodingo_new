package com.foodingo.activities.Model;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by admin on 27-01-2016.
 */
@ParseClassName("mealhistory")
public class MealStored extends ParseObject {

    Dish dish;
    Dish energykcal;
    Double fat_gms;
    Double carb_gms;
    Double prot_gms;
    Dish price;
    String consumption_date;


    public MealStored(Dish dish, Dish energykcal, Double fat_gms, Double carb_gms, Double prot_gms, Dish price, String consumption_date) {
        super();
        this.dish = dish;
        this.energykcal = energykcal;
        this.fat_gms = fat_gms;
        this.carb_gms = carb_gms;
        this.prot_gms = prot_gms;
        this.price = price;
        this.consumption_date = consumption_date;


    }


    public MealStored() {

    }

    public Dish getDish() {
        Dish s = null;
        try {
            s = (Dish) fetchIfNeeded().get("dish_id"); //.get("dish_id");
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return s;
    }


    public Dish getPrice() {
        Dish s = null;
        try {
            s = (Dish) fetchIfNeeded().get("price"); //.get("dish_id");
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return s;
    }


    public Dish getKCall() {
        Dish s = null;
        try {
            s = (Dish) fetchIfNeeded().get("energyKcal");
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return s;
    }


    public void setConsumptionDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cald = Calendar.getInstance();
        String date = dateFormat.format(cald.getTime()).toString();
        put("consumption_date", date);
    }


}
