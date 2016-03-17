package com.foodingo.activities.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.foodingo.activities.Activity.LoginActivity;
import com.foodingo.activities.Activity.MainActivity;
import com.foodingo.activities.Activity.OnboardingActivity;
import com.foodingo.activities.Model.EmailInvitationCode;
import com.foodingo.activities.Model.OnBoardUser;
import com.foodingo.activities.Model.InvitationCode;
import com.foodingo.activities.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class InvitationCodeFragment extends Fragment {

    public static OnBoardUser onBoardUser;
    private Button validate, joinTheQueue;
    private EditText invitation_Code;
    private boolean clicked = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_invitationcode, container, false);

        onBoardUser = LoginFragment.onBoardUser;
        validate = (Button) rootview.findViewById(R.id.validateBtn);
        joinTheQueue = (Button) rootview.findViewById(R.id.joinque);
        invitation_Code = (EditText) rootview.findViewById(R.id.invitation_code);

        validate.setOnClickListener(myhandler1);
        joinTheQueue.setOnClickListener(myhandler2);

        return rootview;

    }

    ParseUser user = ParseUser.getCurrentUser();
    View.OnClickListener myhandler1 = new View.OnClickListener() {

        public void onClick(View v) {
            //    Toast.makeText(getApplicationContext(), "validate button clicked", Toast.LENGTH_SHORT).show();
            final String invitaion_Code = invitation_Code.getText().toString().trim();
            if (invitaion_Code != null) {
                ParseQuery<InvitationCode> query = ParseQuery.getQuery("invitationCode");
                query.whereEqualTo("invitationCode", invitaion_Code);
                query.findInBackground(new FindCallback<InvitationCode>() {
                    public void done(List<InvitationCode> objects, ParseException e) {
                        if (e == null) {
                            if (objects.size() != 0) {
//                                Toast.makeText(getApplicationContext(), "ParseKey is present", Toast.LENGTH_SHORT).show();

                                if (user == null) {
                                    onBoardUser.setInvitationCode(objects.get(0));
                                } else {
                                    user.put("invitationCode", objects.get(0));
                                    onBoardUser.setInvitationCode(objects.get(0));
                                }

                                Intent intent = new Intent(getActivity(), OnboardingActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            } else {
                                Toast.makeText(getActivity(), "Invalid code.Please join the queue to get invitation code.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
//                            Toast.makeText(getApplicationContext(), "ParseKey is not present", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    };
    View.OnClickListener myhandler2 = new View.OnClickListener() {
        public void onClick(View v) {
            String eMailEntered = onBoardUser.getEmail();
            ParseQuery<EmailInvitationCode> query = ParseQuery.getQuery("emailForInvitationCode");
            query.whereEqualTo("email", eMailEntered);
            query.findInBackground(new FindCallback<EmailInvitationCode>() {
                public void done(List<EmailInvitationCode> objects, ParseException e) {
                    if (e == null) {
                        EmailInvitationCode emailForInvitationCode = new EmailInvitationCode();
                        if (objects.size() == 0) {
                            emailForInvitationCode.setEmail(onBoardUser.getEmail());
                            emailForInvitationCode.setsentFlag(true);
                            emailForInvitationCode.saveInBackground();
                            Toast.makeText(getActivity(), "successfully joined", Toast.LENGTH_SHORT).show();
                        } else {
                            //toast message
                            Toast.makeText(getActivity(), "already joined", Toast.LENGTH_SHORT).show();


                        }


                    }

                }


            });
        }


    };


    public void onBackPressed() {
        if (clicked) {
            getActivity().finishAffinity();
            clicked = false;

        } else {
            Toast.makeText(getActivity(), "Tap again to Exit", Toast.LENGTH_SHORT).show();
            clicked = true;
        }
    }


}
