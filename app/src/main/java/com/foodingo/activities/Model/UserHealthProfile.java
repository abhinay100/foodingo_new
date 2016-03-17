package com.foodingo.activities.Model;

/**
 * Created by admin on 26-01-2016.
 */
public class UserHealthProfile {

    double BMR;
    double BMI;
    double rdci;//based on BMR * activity factor
    double calorieBank;//the one we are basing to get targeted BMI (used for meal recommendation)

    public UserHealthProfile(double calorieLimit, double BMR, double BMI, double dailyCalorieNeed) {
        this.calorieBank = calorieLimit;
        this.BMR = BMR;
        this.BMI = BMI;
        this.rdci = dailyCalorieNeed;
    }


    public double getCalorieBank() {
        return calorieBank;
    }

    public void setCalorieBank(double calorieBank) {
        this.calorieBank = calorieBank;
    }

    public double getBMR() {
        return BMR;
    }

    public void setBMR(double BMR) {
        this.BMR = BMR;
    }

    public double getBMI() {
        return BMI;
    }

    public void setBMI(double BMI) {
        this.BMI = BMI;
    }

    public double getRdci() {
        return rdci;
    }

    public void setRdci(double rdci) {
        this.rdci = rdci;
    }

    @Override
    public String toString() {
        return "UserHealthProfile{" + "BMR=" + BMR + ", BMI=" + BMI + ", rdci=" + rdci + ", calorieBank=" + calorieBank + '}';
    }

}
