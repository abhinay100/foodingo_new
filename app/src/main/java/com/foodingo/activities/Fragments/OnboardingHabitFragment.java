package com.foodingo.activities.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;


import com.foodingo.activities.Helpers.UIHelper;
import com.foodingo.activities.Model.OnBoardUser;
import com.foodingo.activities.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;



public class OnboardingHabitFragment extends Fragment {

    Context mContext;
    public static OnBoardUser onBoardUser;
    MaterialBetterSpinner exSpinner, mealSpinner, snackSpinner;
    Button nextBTN, skipBtnTwo;
    int finalExercise, finalMeal, finalSnack;
    double finalCalFactor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_onboardinghabit, container, false);

        onBoardUser = OnboardingHeightWeightFragment.onBoardUser;
        mContext = getActivity().getApplication();

        exSpinner = (MaterialBetterSpinner) rootView.findViewById(R.id.exSpinner);
        mealSpinner = (MaterialBetterSpinner) rootView.findViewById(R.id.mealSpinner);
        snackSpinner = (MaterialBetterSpinner) rootView.findViewById(R.id.snackSpinner);
        nextBTN = (Button) rootView.findViewById(R.id.nextBTN3);
        skipBtnTwo = (Button) rootView.findViewById(R.id.skipBTN2);

        exSpinner.setHintTextColor(getResources().getColor(R.color.colorPrimary));
        mealSpinner.setHintTextColor(getResources().getColor(R.color.colorPrimary));
        snackSpinner.setHintTextColor(getResources().getColor(R.color.colorPrimary));

        String[] list = getResources().getStringArray(R.array.exArray);
        String[] list2 = getResources().getStringArray(R.array.mealArray);
        String[] list3 = getResources().getStringArray(R.array.snackArray);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, list);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, list2);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, list3);

        exSpinner.setAdapter(adapter);
        mealSpinner.setAdapter(adapter2);
        snackSpinner.setAdapter(adapter3);

        nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    saveUserDetails();
                    Fragment fragment = new OnboardingPreferencesFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_content, fragment).commit();
                }
            }
        });

        skipBtnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBoardUser.setSnack(2);
                onBoardUser.setMeal(2);
                onBoardUser.setCalorieFactor(1.2);
                onBoardUser.setExcercise(0);
                Fragment fragment = new OnboardingPreferencesFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_content, fragment).commit();

            }
        });

        return rootView;
    }

    //check if user has entered all the fields
    private boolean validateFields() {

        if (exSpinner.getText().toString().equals("")) {
            UIHelper.showToast(mContext, "Select exercise frequency", 3500);
            return false;
        } else if (mealSpinner.getText().toString().equals("")) {
            UIHelper.showToast(mContext, "Select no. of meals", 3500);
            return false;
        } else if (snackSpinner.getText().toString().equals("")) {
            UIHelper.showToast(mContext, "Select no. of snacks", 3500);
            return false;
        } else {
            return true;
        }
    }

    private void saveUserDetails() {

        String exerciseRange = exSpinner.getText().toString();
        if (exerciseRange.startsWith("L")) {
            finalExercise = 0;
            finalCalFactor = 1.2;
        } else if (exerciseRange.startsWith("1")) {
            finalExercise = 1;
            finalCalFactor = 1.375;
        } else if (exerciseRange.startsWith("3")) {
            finalExercise = 2;
            finalCalFactor = 1.55;
        } else if (exerciseRange.startsWith("M")) {
            finalExercise = 3;
            finalCalFactor = 1.725;
        } else if (exerciseRange.startsWith("I")) {
            finalExercise = 4;
            finalCalFactor = 1.9;
        }

        String meal = mealSpinner.getText().toString();
        if (meal.equals("-")) {
            finalMeal = 1;
        } else {
            finalMeal = Integer.parseInt(meal);
        }

        String snack = snackSpinner.getText().toString();
        if (snack.equals("-")) {
            finalSnack = 0;
        } else {
            finalSnack = Integer.parseInt(snack);
        }

        onBoardUser.setSnack(finalSnack);
        onBoardUser.setMeal(finalMeal);
        onBoardUser.setCalorieFactor(finalCalFactor);
        onBoardUser.setExcercise(finalExercise);

    }


}
