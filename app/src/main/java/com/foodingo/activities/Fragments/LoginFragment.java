package com.foodingo.activities.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.foodingo.activities.Activity.MainActivity;
import com.foodingo.activities.Helpers.UIHelper;
import com.foodingo.activities.Model.OnBoardUser;
import com.foodingo.activities.R;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;


public class LoginFragment extends Fragment implements View.OnClickListener {

    private Button loginBTN, signUpBTN, aboutUsBTN, fb_Login;
    ProgressBar loginProgress;
    String finalEmail, finalPassword;
    EditText emailET, passwordET;
    public static String email;
    String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    Context mContext;
    UIHelper font;
    TextView forgotpassword;
    public static OnBoardUser onBoardUser;
  //  final Context context = getActivity().getApplication();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        mContext = getActivity().getApplication();
        fb_Login = (Button) rootView.findViewById(R.id.fb_Login);
        loginProgress = (ProgressBar) rootView.findViewById(R.id.loginProgress);
        loginBTN = (Button) rootView.findViewById(R.id.login_LOGIN_BTN);
        emailET = (EditText) rootView.findViewById(R.id.username_ET);
        passwordET = (EditText) rootView.findViewById(R.id.password_ET);
        signUpBTN = (Button) rootView.findViewById(R.id.signUpBTN);
        forgotpassword = (TextView) rootView.findViewById(R.id.forgotpassword);

        fb_Login.setOnClickListener(this);
        loginBTN.setOnClickListener(this);
        signUpBTN.setOnClickListener(this);
        forgotpassword.setOnClickListener(this);

        font = new UIHelper(mContext);
        loginBTN.setTypeface(font.boldFont);
        passwordET.setTypeface(font.regularFont);
        emailET.setTypeface(font.regularFont);
        signUpBTN.setTypeface(font.boldFont);

        onBoardUser = new OnBoardUser();

        ParseUser currentUser = ParseUser.getCurrentUser();
        if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
            if (currentUser.has("weight") && currentUser.has("height")
                    && currentUser.has("date_of_birth")) {
                //user is logged into main Activity
                showRecommendationActivity();
            } else {
                showInvitationActivity();
            }
        }
        return rootView;
    }


    private void showRecommendationActivity() {
        //user is logged into main Activity
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        loginProgress.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fb_Login:
                //case facebook login
                loginProgress.setVisibility(View.VISIBLE);
                //facebook login based on permissions
                List<String> permissions = Arrays.asList("user_birthday", "email", "public_profile");
                ParseFacebookUtils.logIn(permissions, getActivity(), new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, com.parse.ParseException e) {
                        if (parseUser == null) {
                            Log.d(getResources().getString(R.string.app_name),
                                    "Uh oh. The user cancelled the Facebook login.");
                            loginProgress.setVisibility(View.INVISIBLE);
                        } else if (parseUser.isNew()) {
                            Log.d(getResources().getString(R.string.app_name),
                                    "User signed up and logged in through Facebook!");
                            fetching_fb_details(parseUser);
                        } else {
                            Log.d(getResources().getString(R.string.app_name), "User logged in through Facebook!");
                            showRecommendationActivity();
                        }
                    }

                });
                break;
            case R.id.login_LOGIN_BTN:
                //case email login
                if (validateUser()) {
                    loginProgress.setVisibility(View.VISIBLE);
                    loginUser();
                }
                break;
            case R.id.signUpBTN:
                // case email signup
                if (validateUser()) {
                    loginProgress.setVisibility(View.VISIBLE);
                    signUpUser();
                }
                break;
            case R.id.forgotpassword:
                //forgotpassword.setText("hi");
                // startActivity(new Intent(Login.this,Pop.class));
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(getActivity());
                View promptsView = li.inflate(R.layout.activity_alert, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Reset",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        ParseUser.requestPasswordResetInBackground(userInput.getText().toString(), new RequestPasswordResetCallback() {
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    // An email was successfully sent with reset instructions.
                                                } else {
                                                    Context context = getActivity().getApplicationContext();
                                                    CharSequence text = "Please enter valid email";
                                                    int duration = Toast.LENGTH_SHORT;

                                                    Toast toast = Toast.makeText(context, text, duration);
                                                    toast.show();

                                                }
                                            }
                                        });

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
                break;
            default:
                break;

        }
    }

    private void signUpUser() {

        if (finalEmail.contains("@")) {
            // if it does, what the user typed in the Username EditText should be an email address
            // set up query looking for any user with that email address
            ParseQuery<ParseUser> emailLoginQuery = ParseUser.getQuery();
            emailLoginQuery.whereEqualTo("email", finalEmail);
            emailLoginQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> list, ParseException e) {
                    if (list.size() == 0) {
                        // if the result list size is 0, no user with entered email exists.
                        // Set up an alert dialog or something
                        onBoardUser.setEmail(finalEmail);
                        onBoardUser.setPassword(finalPassword);
                        showInvitationActivity();

                    } else {
                        ParseUser usr = list.get(0);
                        if (usr.containsKey("profile")) {
                            Toast.makeText(getActivity(), "  facebook user", Toast.LENGTH_LONG).show();
                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setTitle("Facebook Warning..! ");
                            alert.setMessage("already user through Facebook please login from another Facebook account");
                            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    loginProgress.setVisibility(View.INVISIBLE);
                                    dialog.dismiss();
                                }
                            });
                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    loginProgress.setVisibility(View.INVISIBLE);
                                    dialog.dismiss();
                                }
                            });
                            alert.setCancelable(true);
                            alert.show();

                        } else {
                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setTitle("Email Warning..! ");
                            alert.setMessage("Email already exists. Please choose a different email");
                            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    loginProgress.setVisibility(View.INVISIBLE);
                                    dialog.dismiss();
                                }
                            });
                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                    loginProgress.setVisibility(View.INVISIBLE);
                                }
                            });
                            alert.setCancelable(true);
                            alert.show();
                        }
                    }
                }
            });
        }
    }


    private void loginUser() {
        //login the user based on the availability of email
        ParseUser.logInInBackground(finalEmail, finalPassword, new LogInCallback() {
            @Override
            public void done(ParseUser user, com.parse.ParseException e) {
                if (e == null) {

                    //GlobalVar.email = finalEmail;
                    email = finalEmail;

                    if (user.has("gender")) {
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    } else {
                        startActivity(new Intent(getActivity(), OnboardingPersonalDetailsFragment.class));
                        getActivity().finish();
                    }
                } else {
                    Toast.makeText(getActivity(), "Oops, please check your email and password", Toast.LENGTH_LONG).show();
                    loginProgress.setVisibility(View.GONE);
                }
            }
        });
    }

    private void showInvitationActivity() {
        Fragment fragment = new InvitationCodeFragment();
        //create fragment manager which allows you to interact with different fragments inside the activity
        FragmentManager fragmentManager = getFragmentManager();
        //this transaction will allow us to add,attach,dettach,remove,replace,animate transition etc
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_content, fragment).commit();
        loginProgress.setVisibility(View.INVISIBLE);
    }

    public boolean validateUser() {
        //Validating user based on patternd email and password
        String email = emailET.getText().toString().trim();
        email = email.toLowerCase();
        String pwd = passwordET.getText().toString().trim();
        if (!email.isEmpty() && !pwd.isEmpty() && email.matches(EMAIL_PATTERN)) {
            this.finalEmail = email;
            this.finalPassword = pwd;
            return true;
        } else {
            Toast.makeText(getActivity(), "Please enter valid email", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void fetching_fb_details(final ParseUser currentUser) {
        //facebook profile details
        Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if (user != null) {
                            try {
                                // Create a JSON object to hold the profile info
                                JSONObject userProfile = new JSONObject();
                                finalEmail = (String) user.getProperty("email");
                                if (finalEmail.contains("@")) {
                                    // if it does, what the user typed in the Username EditText should be an email address
                                    // set up query looking for any user with that email address
                                    ParseQuery<ParseUser> emailLoginQuery = ParseUser.getQuery();
                                    emailLoginQuery.whereEqualTo("email", finalEmail);
                                    emailLoginQuery.findInBackground(new FindCallback<ParseUser>() {
                                        @Override
                                        public void done(List<ParseUser> list, com.parse.ParseException e) {
                                            if (list.size() == 0) {
                                                loginProgress.setVisibility(View.INVISIBLE);
                                                showInvitationActivity();
                                                Toast.makeText(getActivity(), " user not exist in parse", Toast.LENGTH_LONG).show();
                                            } else {
                                                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                                alert.setTitle("Email Warning..! ");
                                                alert.setMessage("Email already exists. Please choose a different email");
                                                alert.setNegativeButton("  OK  ", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                        currentUser.deleteInBackground();
                                                        loginProgress.setVisibility(View.INVISIBLE);
                                                        dialog.dismiss();
                                                    }
                                                });
                                                alert.show();
                                            }
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        request.executeAsync();
    }
}
