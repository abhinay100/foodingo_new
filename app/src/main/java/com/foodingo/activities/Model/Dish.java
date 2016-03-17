package com.foodingo.activities.Model;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

/**
 * Created by admin on 27-01-2016.
 */
@ParseClassName("mealsdummyx")
public class Dish extends ParseObject {

    String restaurant;
    String dish;
    Double energykcal;
    Double fatgms;
    Double carbgms;
    Double proteingms;
    Double fibregms;
    Double price;
    Double fat_percent;
    Double carb_percent;
    Double prot_percent;
    String halal;
    String vegetarian;
    String seafood;


    public Dish(String restaurant, String dish, Double energykcal,
                Double fatgms, Double carbgms, Double proteingms, Double fibregms,
                Double price, Double fat_percent, Double carb_percent,
                Double prot_percent, String halal, String vegetarian, String seafood) {
        super();
        this.restaurant = restaurant;
        this.dish = dish;
        this.energykcal = energykcal;
        this.fatgms = fatgms;
        this.carbgms = carbgms;
        this.proteingms = proteingms;
        this.fibregms = fibregms;
        this.price = price;
        this.fat_percent = fat_percent;
        this.carb_percent = carb_percent;
        this.prot_percent = prot_percent;
        this.halal = halal;
        this.vegetarian = vegetarian;
        this.seafood = seafood;
    }

    public Dish() {
    }

    public String getDish() {
        String s = "";
        try {
            s = fetchIfNeeded().getString("dish");
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return s;
    }

    public String getKCal(){
        String s = "";
        try {
            s = fetchIfNeeded().getNumber("energyKcal").toString();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return s;
    }


    public String getImageUrl() {
        String s = "";
        try {
            s = fetchIfNeeded().getString("imgurl");
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return s;
    }



    public void setDish(String dish) {
        put("dish", dish);
    }

    public String getRestaurant() {
        if (has("restaurant") && !has("user_created_meal")) {
            return getString("restaurant");
        }
        else {
            return "";
        }
    }

	/*public GeoPoint getLocation(){
		if(has("location")){
			return getGeoPoint("location");
		}

	}*/

    public String getCal() {
        if (has("fat_percent") && has("prot_percent") && has("carb_percent")) {
            return "Fat: " + getNumber("fat_percent").toString() + "%" + "\n"
                    + "Protein: " + getNumber("prot_percent").toString() + "%"
                    + "\n" + "Carbohydrate: "
                    + getNumber("carb_percent").toString() + "%";
        } else {
            return "";
        }
    }

    public double getCalNo() {
        try {
            return Double.parseDouble(fetchIfNeeded().getNumber("energyKcal")
                    .toString());
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public String getPrice() {
        if (has("price")) {
            return "SGD " + getNumber("price").toString() + " ";
        } else {
            return "";
        }
    }

    public double getPriceNo() {
        if (has("price")) {
            return getDouble("price");
        } else {
            return 0;
        }
    }

    public boolean isHalal() {
        if (getBoolean("halal")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isSeafood() {
        if (getBoolean("seafood")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isVeg() {
        if (getBoolean("vegetarian")) {
            return true;
        } else {
            return false;
        }
    }

    public String getFoodDetails() {
        String s = getPrice();
        return s;
    }
}
