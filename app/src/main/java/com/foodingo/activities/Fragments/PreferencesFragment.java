package com.foodingo.activities.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codetroopers.betterpickers.datepicker.DatePickerBuilder;
import com.codetroopers.betterpickers.datepicker.DatePickerDialogFragment;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.foodingo.activities.Activity.MainActivity;
import com.foodingo.activities.Helpers.UIHelper;
import com.foodingo.activities.R;
import com.parse.ParseUser;

/**
 * Created by yosinanggusti on 17/12/15.
 */
public class PreferencesFragment extends Fragment implements View.OnClickListener,DatePickerDialogFragment.DatePickerDialogHandler{

    Button dobBTN;
    ParseUser user;
    int finalDate,finalMonth,finalYear;
    double finalAge = 9999;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_preferences, container, false);

        dobBTN = (Button) rootView.findViewById(R.id.date_EDIT_BTN);
        dobBTN.setOnClickListener(this);
    //    setUpUserDetails();
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.date_EDIT_BTN:
                DatePickerBuilder dpb = new DatePickerBuilder()
                       .setFragmentManager(getActivity().getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment_Light);
                dpb.show();
                break;




            case R.id.save_EDIT_BTN:
                saveUserPref();
//             //   Intent intent = new Intent(getActivity(),MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);

                break;
            default:
                break;

        }
    }

    private void saveUserPref() {
        user.put("date_of_birth", dobBTN.getText().toString());
    }

    //on result from date picker
    @Override
    public void onDialogDateSet(int reference, int year, int monthOfYear, int dayOfMonth) {

        int month = monthOfYear + 1;
        finalDate = dayOfMonth;
        finalMonth = month;
        finalYear = year;

        finalAge = UIHelper.calculateAge(finalDate, finalMonth, finalYear);
        dobBTN.setText(dayOfMonth + "/" + month + "/" + year);
    }

    //set up the user details
    public void setUpUserDetails() {

        dobBTN.setText(user.getString("date_of_birth"));

    }


}
