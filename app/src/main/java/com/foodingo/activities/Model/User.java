package com.foodingo.activities.Model;

/**
 * Created by admin on 26-01-2016.
 */
public class User {

    String userID;
    String pwd;
    double weight;// kg
    double height;// cm
    double age;
    String gender;
    double calorieFactor;
    int noOfMeals;
    int noOfSnacks;
    boolean halal;
    boolean vegetarian;
    boolean seafood_allergic;
    double budget;
    int noOfMeal_balance;
    double cal_balance;
    double snack_cal_balance;
    InvitationCode invitationCode;

    public User() {
    }

//	public User(String userID) {
//		this.userID = userID;
//	}

    public InvitationCode getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(InvitationCode invitationCode) {
        this.invitationCode = invitationCode;
    }

    public User(String userID, String pwd, double weight, double height,
                double age, String gender, int noOfMeals, int noOfSnacks,InvitationCode invitationCode) {
        this.userID = userID;
        this.pwd = pwd;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.gender = gender;
        this.noOfMeals = noOfMeals;
        this.noOfSnacks=noOfSnacks;
        this.invitationCode=invitationCode;
    }

    public int getNoOfMeal_balance() {
        return noOfMeal_balance;
    }

    public void setNoOfMeal_balance(int noOfMeal_balance) {
        this.noOfMeal_balance = noOfMeal_balance;
    }

    public double getCal_balance() {
        return cal_balance;
    }

    public void setCal_balance(double noOfCal_balance) {
        this.cal_balance = noOfCal_balance;
    }

    public int getNoOfMeals() {
        return noOfMeals;
    }

    public void setNoOfMeals(int noOfMeals) {
        this.noOfMeals = noOfMeals;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getCalorieFactor() {
        return calorieFactor;
    }

    public void setCalorieFactor(double calorieFactor) {
        this.calorieFactor = calorieFactor;
    }

    @Override
    public String toString() {
        return userID + "-" + pwd + "-" + weight + "kg -" + height + "cm -"
                + age + "years old -" + gender + "-" + calorieFactor + "kcal. -" + noOfMeals
                + "meal(s)-Halal?" + halal + "-Veg?" + vegetarian + "-Seafood?" + seafood_allergic + "- SGD"
                + budget;

    }

    public boolean isHalal() {
        return halal;
    }

    public void setHalal(boolean halal) {
        this.halal = halal;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public boolean isSeafood_allergic() {
        return seafood_allergic;
    }

    public void setSeafood_allergic(boolean seafood_allergic) {
        this.seafood_allergic = seafood_allergic;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public void setName(String userID) {
        // TODO Auto-generated method stub
        this.userID = userID;
    }

    public void setNoOfSnacks(int snackno) {
        // TODO Auto-generated method stub
        this.noOfSnacks = snackno;
    }

    public double getSnack_cal_balance() {
        return snack_cal_balance;
    }

    public void setSnack_cal_balance(double snack_cal_balance) {
        this.snack_cal_balance = snack_cal_balance;
    }

    public int getNoOfSnacks() {
        return noOfSnacks;
    }



}

