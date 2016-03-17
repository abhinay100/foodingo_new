package com.foodingo.activities.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.foodingo.activities.Fragments.OnboardingPersonalDetailsFragment;

import com.foodingo.activities.R;


/**
 * //TODO handle fragments for onborading processes i.e. personal details height and weight, habits and preferences input.
 */
public class OnboardingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        Fragment fragment = null;
        //based on selected item in navigation drawer, show the correct fragment using fragment transaction to replace and update the title in action bar.
        if (fragment == null) {

            fragment = new OnboardingPersonalDetailsFragment();

            //create fragment manager which allows you to interact with different fragments inside the activity
            FragmentManager fragmentManager = getSupportFragmentManager();

            //this transaction will allow us to add,attach,dettach,remove,replace,animate transition etc
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.fragment_content, fragment).commit();

        }
    }
    //retrieving OnActivityResult from fragments
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }


}