package com.foodingo.activities.Application;

import android.app.Application;

import com.foodingo.activities.Model.Dish;
import com.foodingo.activities.Model.EmailInvitationCode;
import com.foodingo.activities.Model.InvitationCode;
import com.foodingo.activities.Model.MealStored;
import com.foodingo.activities.R.string;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseObject;


public class FoodApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //enable parse local storage
        Parse.enableLocalDatastore(this);

        //initialize with parse app key
        ParseObject.registerSubclass(MealStored.class);
        ParseObject.registerSubclass(Dish.class);
        ParseObject.registerSubclass(InvitationCode.class);
        ParseObject.registerSubclass(EmailInvitationCode.class);
        Parse.initialize(this, getString(string.parse_app_id), getString(string.parse_client_key));
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseFacebookUtils.initialize(getString(string.facebook_app_id));
    }
}
