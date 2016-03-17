package com.foodingo.activities.Model;

import com.parse.ParseFile;


public class OnBoardUser {
    public InvitationCode invitationCode;

    public InvitationCode getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(InvitationCode invitationCode) {
        this.invitationCode = invitationCode;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public ParseFile getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(ParseFile profilePic) {
        this.profilePic = profilePic;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getWeight_unit() {
        return weight_unit;
    }

    public void setWeight_unit(int weight_unit) {
        this.weight_unit = weight_unit;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight_unit() {
        return height_unit;
    }

    public void setHeight_unit(int height_unit) {
        this.height_unit = height_unit;
    }

    public int getExcercise() {
        return excercise;
    }

    public void setExcercise(int excercise) {
        this.excercise = excercise;
    }

    public int getMeal() {
        return meal;
    }

    public void setMeal(int meal) {
        this.meal = meal;
    }

    public int getSnack() {
        return snack;
    }

    public void setSnack(int snack) {
        this.snack = snack;
    }

    public int getHalal() {
        return halal;
    }

    public void setHalal(int halal) {
        this.halal = halal;
    }

    public int getSeaFood() {
        return seaFood;
    }

    public void setSeaFood(int seaFood) {
        this.seaFood = seaFood;
    }

    public int getVeg() {
        return veg;
    }

    public void setVeg(int veg) {
        this.veg = veg;
    }

    String alias;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    String email;
    String password;
    String dob;
    String gender;
    ParseFile profilePic;
    double weight;
    int weight_unit;
    int height, height_unit;
    int excercise, meal, snack;
    int halal;

    @Override
    public String toString() {
        return "OnBoardUser{" +
                "alias='" + alias + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", dob='" + dob + '\'' +
                ", gender='" + gender + '\'' +
                ", profilePic=" + profilePic +
                ", weight=" + weight +
                ", weight_unit=" + weight_unit +
                ", height=" + height +
                ", height_unit=" + height_unit +
                ", excercise=" + excercise +
                ", meal=" + meal +
                ", snack=" + snack +
                ", halal=" + halal +
                ", seaFood=" + seaFood +
                ", veg=" + veg +
                ", calorieFactor=" + calorieFactor +
                ", age=" + age +
                '}';
    }

    int seaFood;
    int veg;

    public double getCalorieFactor() {
        return calorieFactor;
    }

    public void setCalorieFactor(double calorieFactor) {
        this.calorieFactor = calorieFactor;
    }

    double calorieFactor;

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    double age;

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    double budget;


    public int getNoOfMeal_balance() {
        return noOfMeal_balance;
    }

    public void setNoOfMeal_balance(int noOfMeal_balance) {
        this.noOfMeal_balance = noOfMeal_balance;
    }

    public double getCal_balance() {
        return cal_balance;
    }

    public void setCal_balance(double cal_balance) {
        this.cal_balance = cal_balance;
    }

    public double getSnack_cal_balance() {
        return snack_cal_balance;
    }

    public void setSnack_cal_balance(double snack_cal_balance) {
        this.snack_cal_balance = snack_cal_balance;
    }

    int noOfMeal_balance;
    double cal_balance;
    double snack_cal_balance;

}
