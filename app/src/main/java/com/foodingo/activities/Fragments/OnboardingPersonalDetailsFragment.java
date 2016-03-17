package com.foodingo.activities.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;
import com.foodingo.activities.Helpers.UIHelper;
import com.foodingo.activities.Model.OnBoardUser;
import com.foodingo.activities.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.codetroopers.betterpickers.datepicker.DatePickerBuilder;
import com.codetroopers.betterpickers.datepicker.DatePickerDialogFragment;
import com.soundcloud.android.crop.Crop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;


public class OnboardingPersonalDetailsFragment extends Fragment implements View.OnClickListener, DatePickerDialogFragment.DatePickerDialogHandler, View.OnTouchListener, SpringListener {

    Button nextBTN, dobBTN;
    EditText userNameET;
    RoundedImageView profilePicIV;
    Context mContext;
    int cropCount = 0;
    Bitmap bm = null;
    private static double TENSION = 800;
    private static double DAMPER = 20;
    private SpringSystem mSpringSystem;
    private Spring mSpring;
    ParseFile userProfilePicFile = null;
    int TOUCHFLAG;
    ImageView boyIV, girlIV;
    public static OnBoardUser onBoardUser;
    int finalDate, finalMonth, finalYear;
    ProgressDialog progressDialog;
    boolean boy;
    ParseUser user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_onboardingpersonaldetails, container, false);

        profilePicIV = (RoundedImageView) rootView.findViewById(R.id.profilePic_IV);
        profilePicIV.setOnClickListener(this);

        boyIV = (ImageView) rootView.findViewById(R.id.boyIV);
        girlIV = (ImageView) rootView.findViewById(R.id.girlIV);
        boyIV.setOnTouchListener(this);
        girlIV.setOnTouchListener(this);

        nextBTN = (Button) rootView.findViewById(R.id.nextBTN);
        nextBTN.setOnClickListener(this);

        dobBTN = (Button) rootView.findViewById(R.id.dobBTN);
        dobBTN.setOnClickListener(this);

        userNameET = (EditText) rootView.findViewById(R.id.username_ET);
        user = ParseUser.getCurrentUser();

        //initialize the spring effect
        mSpringSystem = SpringSystem.create();
        mSpring = mSpringSystem.createSpring();
        mSpring.addListener(this);
        SpringConfig config = new SpringConfig(TENSION, DAMPER);
        mSpring.setSpringConfig(config);

        onBoardUser = InvitationCodeFragment.onBoardUser;
        mContext = getActivity().getApplication();

        progressDialog = new ProgressDialog(getActivity().getApplicationContext());
        // Fetch Facebook user info if the session is active
        Session session = ParseFacebookUtils.getSession();
        if (session != null && session.isOpened() || user != null) {
            progressDialog = ProgressDialog.show(getActivity(), "",
                    "Retrieving your profile", true);
            //   session.openForRead(new Session.OpenRequest(this));//.setCallback(statusCallback));
            user = ParseUser.getCurrentUser();
            makeMeRequest(user);
            progressDialog.dismiss();
        }

        return rootView;
    }

    private void makeMeRequest(final ParseUser currentUser) {
        Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if (user != null) {
                            // Create a JSON object to hold the profile info
                            JSONObject userProfile = new JSONObject();
                            try {

                                // Populate the JSON object and
                                // name&email&gender textview
                                // also update foouser object
                                String fid = user.getId();

                                userProfile.put("facebookId", user.getId());
                                if (user.getName() != null) {
                                    userProfile.put("name", user.getName());
                                    //   userProfile.put("birthday",user.getBirthday());
                                }
                                if (user.getProperty("email") != null) {
                                    userProfile.put("email", user.getProperty("email"));
                                }
                                // Save the user profile info in a user property
                                currentUser.put("profile", userProfile);

                                // Save the user profile to parseUser
                                // columns
                                if (!currentUser.has("alias")) {
                                    currentUser.put("alias", user.getName());
                                }
                                try {
                                    if (user.getProperty("email") != null) {
                                        currentUser.put("username", user.getProperty("email").toString());
                                        currentUser.put("date_of_birth", user.getBirthday());
                                        currentUser.put("email", user.getProperty("email"));
                                        currentUser.put("gender", user.getProperty("gender"));
                                    }
                                } catch (IllegalArgumentException e1) {
                                    e1.printStackTrace();
                                }
                                userNameET.setText(currentUser.getString("alias"));

                                if (currentUser.get("gender").equals("male")) {
                                    boyIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_boy_selected));
                                } else {
                                    girlIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_girl_selected));
                                }
                                String dob = currentUser.getString("date_of_birth");
                                dobBTN.setText(dob.substring(3, 6) + dob.substring(0, 2) + dob.substring(5));


                                if (currentUser.get("gender").equals("male")) {
                                    onBoardUser.setGender("male");
                                } else {
                                    onBoardUser.setGender("female");
                                }

                                onBoardUser.setDob(dobBTN.getText().toString());
                                onBoardUser.setAlias(userNameET.getText().toString());

                                currentUser.saveInBackground();
                                // Show the user info
                            } catch (JSONException e) {

                            }
                        } else if (response.getError() != null) {
                            if ((response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_RETRY)
                                    || (response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_REOPEN_SESSION)) {
//                                Log.d(getResources().getString R.string.app_name),"The facebook session was invalidated."+ response.getError());
                                //  logout();
                            }
                        }
                    }
                });
        request.executeAsync();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.nextBTN:
                if (validateFields()) {
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            saveUserDetails();
                        }
                    };
                    thread.start();
                    //   startActivity(new Intent(getActivity(), OnboardingHeightWeightFragment.class));
                    Fragment fragment = new OnboardingHeightWeightFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_content, fragment).commit();
                }
                break;
            case R.id.profilePic_IV:
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
                break;
            case R.id.dobBTN:
                DatePickerBuilder dpb = new DatePickerBuilder()
                        .setFragmentManager(getFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment_Light);
                dpb.show();
                break;
            default:
                break;
        }

    }

    //check if all fields are filled
    public boolean validateFields() {
        if (userNameET.getText().toString().equals("")) {
            UIHelper.showToast(mContext, "Please enter username", 3500);
            return false;
//        } else if (dobBTN.getText().toString().equals("DD/MM/YYYY")) {
//            UIHelper.showToast(mContext, "Please enter birthday", 3500);
//            return false;
        } else if (boyIV.getDrawable() == getResources().getDrawable(R.drawable.ic_boy) && girlIV.getDrawable() == getResources().getDrawable(R.drawable.ic_girl)) {
            UIHelper.showToast(mContext, "Please select gender", 3500);
            return false;
        } else {
            return true;
        }
    }

    //save user details to local instance of onBoard
    private void saveUserDetails() {
        if (bm != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] image = stream.toByteArray();
            userProfilePicFile = new ParseFile("profile_pic.png", image);
            if (userProfilePicFile != null) {
                onBoardUser.setProfilePic(userProfilePicFile);
            }
        }
        if (boy) {
            onBoardUser.setGender("male");
        } else {
            onBoardUser.setGender("female");
        }
        onBoardUser.setDob(dobBTN.getText().toString());
        onBoardUser.setAlias(userNameET.getText().toString());

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (v.getId() == R.id.boyIV) {
            TOUCHFLAG = 0;
        } else if (v.getId() == R.id.girlIV) {
            TOUCHFLAG = 1;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mSpring.setEndValue(1f);
                return true;
            case MotionEvent.ACTION_UP:
                mSpring.setEndValue(0f);
                if (v.getId() == R.id.boyIV) {
                    boyIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_boy_selected));
                    girlIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_girl));
                    boy = true;
                } else if (v.getId() == R.id.girlIV) {
                    girlIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_girl_selected));
                    boyIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_boy));
                    boy = false;
                }
                return true;
        }
        return false;
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        float value = (float) spring.getCurrentValue();
        float scale = 1f - (value * 0.5f);

        if (TOUCHFLAG == 0) {
            boyIV.setScaleX(scale);
            boyIV.setScaleY(scale);
        } else if (TOUCHFLAG == 1) {
            girlIV.setScaleX(scale);
            girlIV.setScaleY(scale);
        }

    }

    @Override
    public void onSpringAtRest(Spring spring) {

    }

    @Override
    public void onSpringActivate(Spring spring) {

    }

    @Override
    public void onSpringEndStateChange(Spring spring) {

    }

    //start the image crop
    private void beginCrop(Uri source) {
        cropCount++;
        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped" + cropCount));
        Crop.of(source, destination).asSquare().start(getActivity());
    }

    //handle the cropped image
    public void handleCrop(int resultCode, Intent result) {
        if (resultCode == getActivity().RESULT_OK) {
            Uri imageUri = Crop.getOutput(result);
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                profilePicIV.setImageBitmap(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(getActivity(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //handle the picked image
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 0:
                if (resultCode == getActivity().RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    beginCrop(selectedImage);
                }
                break;
            case 1:
                if (resultCode == getActivity().RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    beginCrop(selectedImage);
                }
                break;
            case Crop.REQUEST_CROP:
                handleCrop(resultCode, imageReturnedIntent);
                break;
        }
    }

    @Override
    public void onDialogDateSet(int reference, int year, int monthOfYear, int dayOfMonth) {

        int month = monthOfYear + 1;
        finalDate = dayOfMonth;
        finalMonth = month;
        finalYear = year;

        onBoardUser.setAge(UIHelper.calculateAge(finalDate, finalMonth, finalYear));
        dobBTN.setText(dayOfMonth + "/" + month + "/" + year);

    }


    //on result from date picker
//    @Override
//    public void onDialogDateSet(int reference, int year, int monthOfYear, int dayOfMonth) {
//
//        int month = monthOfYear + 1;
//        finalDate = dayOfMonth;
//        finalMonth = month;
//        finalYear = year;
//
//        onBoardUser.setAge(UIHelper.calculateAge(finalDate, finalMonth, finalYear));
//        dobBTN.setText(dayOfMonth + "/" + month + "/" + year);
//
//    }
}
