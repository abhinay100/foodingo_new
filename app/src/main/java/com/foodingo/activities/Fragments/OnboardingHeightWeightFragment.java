package com.foodingo.activities.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.foodingo.activities.Helpers.UIHelper;
import com.foodingo.activities.Model.OnBoardUser;
import com.foodingo.activities.R;


public class OnboardingHeightWeightFragment extends Fragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private SeekBar weightseek, heightseek;
    private EditText editweight, editheight;
    Button nextBTN, skipBtnOne;
    Context mContext;
    int finalHeight = 9999;
    int finalWeight = 9999;
    public static OnBoardUser onBoardUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_onboardingheightandweight, container, false);


        mContext = getActivity().getApplication();
        onBoardUser = OnboardingPersonalDetailsFragment.onBoardUser;

        nextBTN = (Button) rootView.findViewById(R.id.nextbutton);
        nextBTN.setOnClickListener(this);

        skipBtnOne = (Button) rootView.findViewById(R.id.skipbutton);
        skipBtnOne.setOnClickListener(this);

        editweight = (EditText) rootView.findViewById(R.id.weight_ET);
        editheight = (EditText) rootView.findViewById(R.id.height_ET);
        //takes to the habit fragment with saving height ande weight
        nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    saveUserDetails();
                    Fragment fragment = new OnboardingHabitFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_content, fragment).commit();
                }
            }
        });

        //skips to habit fragment when saving default height and weight
        skipBtnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBoardUser.setHeight(0);
                onBoardUser.setWeight(0);
                onBoardUser.setWeight_unit(0);
                onBoardUser.setHeight_unit(0);
                Fragment fragment = new OnboardingHabitFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_content, fragment).commit();
            }
        });

        //fetches the data from edit text and saves as weight
        editweight.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (UIHelper.validateWeight(mContext, Integer.valueOf(s.toString()))) {
                        finalWeight = Integer.valueOf(s.toString());
                        Log.v("final weight=", String.valueOf(finalWeight));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        //fetches data from edit text and saves as height
        editheight.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (UIHelper.validateHeight(mContext, Integer.valueOf(s.toString()))) {
                        finalHeight = Integer.valueOf(s.toString());
                        Log.v("final height=", String.valueOf(finalHeight));

                    }
                } catch (NumberFormatException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //seekbar for weight initialization
        weightseek = (SeekBar) rootView.findViewById(R.id.WeightseekBarID);
        weightseek.setOnSeekBarChangeListener(this);
        weightseek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                editweight.setText("" + progress);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //seekbar for height initialization
        heightseek = (SeekBar) rootView.findViewById(R.id.heightseekBarID);
        heightseek.setOnSeekBarChangeListener(this);
        heightseek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                editheight.setText("" + progress);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        return rootView;
    }


    //check if user filled details
    private boolean validateFields() {

        if (finalHeight == 9999) {
            UIHelper.showToast(mContext, "Please enter your height", 3500);
            return false;
        } else if (finalWeight == 9999) {
            UIHelper.showToast(mContext, "Please enter your weight", 3500);
            return false;
        } else {
            return true;
        }
    }


    //saving the user details based on the final values stored
    public void saveUserDetails() {

        onBoardUser.setHeight(finalHeight);
        onBoardUser.setWeight(finalWeight);
        onBoardUser.setWeight_unit(0);
        onBoardUser.setHeight_unit(0);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        editweight.setText("" + progress);
        editheight.setText("" + progress);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {

    }
}
