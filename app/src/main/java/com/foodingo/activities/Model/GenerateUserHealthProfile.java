package com.foodingo.activities.Model;

import java.text.DecimalFormat;

/**
 * Created by admin on 26-01-2016.
 */
public class GenerateUserHealthProfile {

    public UserHealthProfile calcUserHealthProfile(double dailyCalNeed, double calorieToChange, int bmiState, double myBMR, double myBMI, int input, OnBoardUser u) {
        double targetCalPerDay = 0;
        if (bmiState == 0) {
            //user within healthy range, act according to response by user
            if (input == 1) {
                if ((dailyCalNeed - calorieToChange) < myBMR) {
                    calorieToChange = dailyCalNeed - myBMR;
                }
                targetCalPerDay = dailyCalNeed - calorieToChange;
            } else if (input == 2) {
                targetCalPerDay = dailyCalNeed + calorieToChange;
            } else if (input == 3) {
                targetCalPerDay = dailyCalNeed;
            }
        } else if (bmiState < 0) {
            //Case 1:need to lose weight
            //Case 1a: if dailyCalNeed - AssumeCaltoChangePerDay < myBMR i.e. you cannot eat below ur BMR, else cut based on AssumeCaltoChangePerDay
            if ((dailyCalNeed - calorieToChange) < myBMR) {
                calorieToChange = dailyCalNeed - myBMR;
            }
            targetCalPerDay = dailyCalNeed - calorieToChange;

        } else if (bmiState == 2) {
            String gender = u.getGender();
            if (gender.equals("male")) {
                targetCalPerDay = 2200;
            } else if (gender.equals("female")) {
                targetCalPerDay = 1800;
            }
        } else {
            //Case 2: need to gain weight
            targetCalPerDay = dailyCalNeed + calorieToChange;
        }
        targetCalPerDay = Math.round(targetCalPerDay);
        UserHealthProfile ushp = new UserHealthProfile(targetCalPerDay, myBMR, myBMI, dailyCalNeed);
        return ushp;
    }

    //to replace parameters with User object and use get method to get variables
    public double calcBMR(OnBoardUser u) {
        double bmr;
        if (u.getWeight() == 0 && u.getHeight() == 0) {
            bmr = 0;
        } else {
            String gender = u.getGender();
            if (gender.equals("male")) {
                bmr = 10 * u.getWeight() + 6.25 * u.getHeight() - 5 * u.getAge() + 5;
            } else if (gender.equals("female")) {
                bmr = 10 * u.getWeight() + 6.25 * u.getHeight() - 5 * u.getAge() - 161;
            } else {
                bmr = 0;
            }
        }
        return bmr;
    }

    public void setfactorBasedOnPhysicalAct(User user1) {
        double factor;

        int choice = 2;
        switch (choice) {
            case 1:
                factor = 1.2;
                break;
            case 2:
                factor = 1.375;
                break;
            case 3:
                factor = 1.55;
                break;
            case 4:
                factor = 1.725;
                break;
            case 5:
                factor = 1.9;
                break;
            default:
                factor = 0;
                break;
        }
        user1.setCalorieFactor(factor);
    }

    public String getBMI(double w, double h) {
        if (w == 0 && h == 0) {
            DecimalFormat df = new DecimalFormat("#.##");
            return df.format(0);
        } else {
            DecimalFormat df = new DecimalFormat("#.##");
            double hInMetre = h / 100;
            return df.format(w / (hInMetre * hInMetre));
        }
    }

    public double getBodyFatPercentage(double BMI, double age, String gender) {
        double g = 0;
        if (gender.equals("m")) {
            g = 1;
        }
        return ((1.2 * BMI) + (0.23 * age) - (10.8 * g)) / 100;
    }

    //*assuming height not changed/ fix
    public int getBMIState(double bmirange1, double bmirange2, double bmi) {
        if (bmi == 0) {
            int bmiState = 2;
            return bmiState;
        } else {
            int bmiState = 0;
            if (bmi < bmirange1 || bmi > bmirange2) {
                if (bmi < bmirange1) {
                    //need to gain weight
                    bmiState = 1;
                } else {
                    //need to reduce weight
                    //delta is NEGATIVE
                    bmiState = -1;
                }
            }
            return bmiState;
        }
    }

    public double getCalorieToChange(double bmirange1, double bmirange2, double bmi) {
        double calToChange = 0;
        if (bmi == 0) {
            return calToChange;
        } else {
            if (bmi < bmirange1) {
                calToChange = -57.14 * bmi + 1357.1;
            } else if (bmi < bmirange2) {
                calToChange = 41.32 * bmi * bmi - 1710.74 * bmi + 17807.12;
            } else {
                calToChange = 21.98 * bmi - 203.36;
            }
        }
        return calToChange;
    }

}
