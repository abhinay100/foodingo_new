package com.foodingo.activities.Fragments;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.foodingo.activities.Adapters.DishAdapter;
import com.foodingo.activities.Model.Dish;
import com.foodingo.activities.Model.MealStored;
import com.foodingo.activities.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.melnykov.fab.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.doorbell.android.DoorbellApi;
import io.doorbell.android.manavo.rest.RestCallback;

/**
 * Created by yosinanggusti on 17/12/15.
 */

/**
 * A placeholder fragment containing a simple view.
 */
public class FoodPicksFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    protected GoogleApiClient mGoogleApiClient;
    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;
    protected LocationRequest mLocationRequest;
    protected final String TAG = "Google-location-extract";

    boolean mRequestingLocationUpdates;
    protected String mLastUpdateTime;

    // keys
    protected final String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";
    protected final Double LOCATION_RADIUS = 1.0;

    SwipeRefreshLayout mSwipeRefreshLayout;

    // LatLong
//    public String mLatitude;
//    public String mLongitude;
    // views
    private DishAdapter mealAdapter;
    private ListView listView;
    private Dialog progressDialog;


    // variables
    private ParseUser user;
    private Double cal;
    private int noOfMeal;
    private Double snack_cal_balance;
    private int noOfSnack;
    private double budget;
    ImageView gpsenable;
    ImageView noresult;
    Button bannerbutton;
    Button mealbannerbutton;
    public static String mLatitude;
    public static String mLongitude;


    // feedback to send email on outdated meals
    DoorbellApi doorbellAPI;


    public interface Listener {
        public void exploreMeals(int i);
    }

    public Listener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_explore_meals, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
//            case R.id.explore_meals:
//                mListener.exploreMeals(1);
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_foodpicks,
                container, false);
        setHasOptionsMenu(true);

        gpsenable = (ImageView) rootView.findViewById(R.id.gpsenable);
        noresult = (ImageView) rootView.findViewById(R.id.noresult);
        bannerbutton = (Button) rootView.findViewById(R.id.buttonClick);
        mealbannerbutton = (Button) rootView.findViewById(R.id.buttonClickmeal);


        // for location, setting up flags and bundle for saved state as well as
        // the Api client for connection
        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";
        buildGoogleApiClient();
        updateValuesFromBundle(savedInstanceState);
        // savedInstanceStateLocal = savedInstanceState;
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }

        //floating cart
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.foodCart_BTN);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //         mListener.exploreMeals(2);
            }
        });

        user = ParseUser.getCurrentUser();
//        // Fetch Facebook user info if the session is active
//        //Session session = ParseFacebookUtils.getSession();
        if (user != null) {

            int height = (int) user.getNumber("height");
            Double weight = user.getDouble("weight");

            if (height == 0 || weight == 0) {
                bannerbutton.setVisibility(View.VISIBLE);
                mealbannerbutton.setVisibility(View.GONE);
            } else {

                Double Meall = Double.parseDouble(user.getNumber("mealNo_balance").toString());
                Double call = Double.parseDouble(user.getNumber("meal_cal_balance").toString());
                Double mealcal = (call / Meall);
                DecimalFormat form = new DecimalFormat("0.00");
                mealbannerbutton.setText("Meal Target: " + form.format(mealcal) + "kcal");
                mealbannerbutton.setVisibility(View.VISIBLE);
                bannerbutton.setVisibility(View.GONE);
                if (mealcal <= 0) {
                    mealbannerbutton.setVisibility(View.GONE);
                } else if (Double.isInfinite(mealcal)) {
                    mealbannerbutton.setVisibility(View.GONE);
                }
            }


            try {
                mSwipeRefreshLayout = (SwipeRefreshLayout) rootView
                        .findViewById(R.id.activity_main_swipe_refresh_layout);
                mSwipeRefreshLayout.setOnRefreshListener(this);
                listView = (ListView) rootView.findViewById(R.id.list);

                user = ParseUser.getCurrentUser();

                if (user != null) {

                    refreshNewDayCaloriesData();

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        public void onItemClick(AdapterView<?> parent,
                                                View view, int position, long id) {

                            // When clicked, save the dish item to the DB
                            final Dish d = mealAdapter.getItem(position);
                            AlertDialog.Builder alert = new AlertDialog.Builder(
                                    getActivity());

                            alert.setTitle("You are about to consume:");
                            alert.setMessage(d.getDish());
                            alert.setCancelable(true);
                            alert.setNeutralButton("Having it",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int whichButton) {
                                            cal = cal - d.getCalNo();
                                            noOfMeal = noOfMeal - 1;
                                            Toast.makeText(
                                                    getActivity(),
                                                    "Enjoy your " + d.getDish()
                                                            + " :)",
                                                    Toast.LENGTH_SHORT).show();

                                            //1) save consumption date, type,dish_id, price
                                            // ParseObject meallog = new ParseObject("mealhistory");
                                            ParseObject meallog = new MealStored();
                                            meallog.put("price", d.getNumber("price"));
                                            meallog.put("type", "meal");
                                            meallog.put("dish_id", d);
                                            meallog.put("user_id", user);
                                            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                            Calendar cald = Calendar.getInstance();
                                            String date = dateFormat.format(cald.getTime()).toString();
                                            meallog.put("consumption_date", date);


                                            user.put("mealNo_balance", noOfMeal);
                                            user.put("meal_cal_balance", cal);
                                            // saving all objects all at once using saveallinbackground.
                                            List<ParseObject> parseobjlist = new ArrayList<ParseObject>();
                                            parseobjlist.addAll(Arrays.asList(meallog, user));
                                            ParseObject.saveAllInBackground(parseobjlist,
                                                    new SaveCallback() {
                                                        @SuppressLint("LongLogTag")
                                                        @Override
                                                        public void done(ParseException e) {
                                                            if (e == null) {

                                                                // get newest updated
                                                                // data...
                                                                Log.d("Left with ",
                                                                        noOfMeal
                                                                                + " no of meal. Calories balance: "
                                                                                + cal);
                                                                Toast.makeText(getActivity(),
                                                                        "Food logged in Calstory!",
                                                                        Toast.LENGTH_SHORT).show();

                                                            } else {
                                                                Log.d("Error in saving meal&cal balances", e.getMessage());
                                                            }
                                                        }
                                                    });


                                            noOfMeal = Integer
                                                    .parseInt(user.getNumber(
                                                            "mealNo_balance")
                                                            .toString());
                                            cal = Double.parseDouble(user
                                                    .getNumber(
                                                            "meal_cal_balance")
                                                    .toString());

                                            dialog.dismiss();

                                        }
                                    });

                            alert.setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int whichButton) {
                                            dialog.dismiss();

                                        }
                                    });

                            alert.setPositiveButton("Outdated info",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int whichButton) {
                                            // outdated meal = send email to
                                            // foodingo
                                            // with user info and update
                                            // database dish counter
                                            // email
                                            doorbellAPI = new DoorbellApi(
                                                    getActivity());
                                            doorbellAPI.setAppId(1681);
                                            doorbellAPI
                                                    .setApiKey("JI9oZF0g2hRN8Z9wyqI3spUX8UlqadstBD7YJtWhYlOr4KbRSBxIJqqVJFgLVxwH");
                                            JSONObject mProperties = new JSONObject();
                                            try {
                                                mProperties.put("User",
                                                        user.getUsername());
                                            } catch (JSONException ex) {
                                                Log.d("JSON Error",
                                                        ex.getMessage());
                                            }
                                            doorbellAPI
                                                    .setCallback(new RestCallback() {
                                                        @Override
                                                        public void success(
                                                                Object obj) {
                                                            Toast.makeText(
                                                                    getActivity(),
                                                                    obj.toString(),
                                                                    Toast.LENGTH_LONG)
                                                                    .show();
                                                        }
                                                    });
                                            try {
                                                String info = "Reported outdated dish by :"
                                                        + user.getUsername();
                                                info += "\nUser ID: "
                                                        + user.getObjectId();
                                                info += "\nDish " + d.getDish();
                                                info += "\nDish ID: "
                                                        + d.getObjectId();
                                                info += "\nRestaurant: "
                                                        + d.getRestaurant();
                                                doorbellAPI.sendFeedback(info,
                                                        user.getEmail(),
                                                        mProperties,
                                                        user.toString());
                                            } catch (Exception exc) {
                                                Log.d("Feedback Doorbell Error",
                                                        exc.getMessage());

                                            }
                                            // update Parse
                                            // Increment the current value of
                                            // the quantity key by 1
                                            d.increment("report_counter");

                                            // Save
                                            d.saveInBackground(new SaveCallback() {
                                                @SuppressLint("LongLogTag")
                                                public void done(
                                                        ParseException e) {
                                                    if (e == null) {
                                                        Toast.makeText(getActivity(), "Thank you for the feedback", Toast.LENGTH_SHORT).show();
                                                        // Saved successfully.
                                                        refreshNewDayCaloriesData();
                                                        Log.d("Increment field report_counter success", "SUCCESS");
                                                    } else {
                                                        // The save failed.
                                                        Log.d("Increment field report_counter error", e.getMessage());
                                                    }
                                                }
                                            });
                                            dialog.dismiss();

                                        }
                                    });
                            alert.show();

                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "user is " + user,
                            Toast.LENGTH_LONG);
                }

            } catch (Exception e) {
                Toast.makeText(getActivity(), "Error: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(),
                    "Error: Please restart Foodingo App!", Toast.LENGTH_SHORT)
                    .show();
        }

        fab.attachToListView(listView);


        return rootView;
    }

    //refreshing the food picks
    public void refreshNewDayCaloriesData() {
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("mealhistory");

            query.whereEqualTo("user_id", user);
            // get today's date DateFormat
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calender = Calendar.getInstance();
            String todaysdate = dateFormat.format(calender.getTime())
                    .toString();
            query.whereEqualTo("consumption_date", todaysdate);
            Log.e("Is this a new day?",
                    "Querying db to check if any mealhistory record for "
                            + todaysdate);
            query.findInBackground(new FindCallback<ParseObject>() {

                // to check user's calorie data if its a new day, refresh
                // calorie count fresh from cal bank
                // else take the latest data left for the day
                @SuppressLint("LongLogTag")
                @Override
                public void done(List<ParseObject> dishes, ParseException error) {
                    try {
                        if ((dishes != null && dishes.size() == 0)
                                || dishes == null) {
                            Log.d("User hasn't eaten anything today", "Cal balance and meal left is updated to ensure that this is a new day!");

                            ParseQuery<ParseObject> queryUSHP = ParseQuery.getQuery("UserHealthProfile");
                            queryUSHP.whereEqualTo("userId", user);
                            queryUSHP.orderByDescending("createdAt");
                            queryUSHP.getFirst();
                            queryUSHP.findInBackground(new FindCallback<ParseObject>() {
                                // to check user's calorie data if its a new day, refresh
                                // calorie count fresh from cal bank
                                // else take the latest data left for the day
                                @Override
                                public void done(List<ParseObject> ushp, ParseException error) {
                                    try {
                                        user.put("calBank", ushp.get(0).get("calorieBank"));
                                        if (user.getNumber("mealNo_balance") == user
                                                .getNumber("No_of_meals")) {
                                            // if user has chosen 2 or more no of snacks, we
                                            // limit their intake
                                            // of snack to 25% of calorie bank to snack
                                            // else, for 1 snack we
                                            // limit to 12.5%
                                            if (Integer.parseInt(user.getNumber(
                                                    "snackNo_balance").toString()) > 1) {
                                                user.put("meal_cal_balance",
                                                        (Integer) ushp.get(0).get("calorieBank") * 0.75);
                                                user.put("snack_cal_balance",
                                                        ((Integer) ushp.get(0).get("calorieBank") * 0.25));
                                            } else if (Integer.parseInt(user.getNumber(
                                                    "snackNo_balance").toString()) == 1) {
                                                user.put("meal_cal_balance",
                                                        (Integer) ushp.get(0).get("calorieBank") * 0.875);
                                                user.put("snack_cal_balance",
                                                        (Integer) ushp.get(0).get("calorieBank") * 0.125);
                                            } else {
                                                // no snack chosen == 0
                                                user.put("meal_cal_balance",
                                                        (Integer) ushp.get(0).get("calorieBank"));
                                                user.put("snack_cal_balance", 0);
                                            }
                                        }
                                        user.put("mealNo_balance",
                                                user.getNumber("No_of_meals"));
                                        user.put("snackNo_balance",
                                                user.getNumber("No_of_snack"));
                                        user.saveInBackground(new SaveCallback() {
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    cal = Double.parseDouble(user
                                                            .getNumber("meal_cal_balance")
                                                            .toString());
                                                    noOfMeal = Integer.parseInt(user
                                                            .getNumber("mealNo_balance")
                                                            .toString());

                                                } else {
                                                    Log.d("Error in refreshNewDayCaloriesData", e.getLocalizedMessage());
                                                }
                                            }
                                        });
                                    } catch (Exception e) {
                                        Toast.makeText(
                                                getActivity(),
                                                "Failed here :" + e.getMessage() + "; "
                                                        + e.getMessage(), Toast.LENGTH_LONG)
                                                .show();
                                    }

                                    // recommend food once the checking of calorie number is
                                    // done
                                    try {
                                        recommend();
                                    } catch (ParseException e1) {
                                        // TODO Auto-generated catch block
                                        Log.d("Failed at recommend() here", e1.getMessage());
                                    }

                                }
                            });


                        } else {
                            Log.i("User ate something today",
                                    "#items in meal history data: "
                                            + dishes.size());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // recommend food once the checking of calorie number is
                    // done
                    try {
                        recommend();
                    } catch (ParseException e1) {
                        // TODO Auto-generated catch block
                        Log.d("Failed at recommend() here", e1.getMessage());
                    }

                }
            });

        } catch (Exception e) {
            Log.d("updateData", e.getMessage());
        }
    }

    // recommend meals based on restrictions and whether there should still be a
    // meal recommendaed i.e. has meal balance
    @SuppressLint("LongLogTag")
    public void recommend() throws ParseException {
        progressDialog = ProgressDialog.show(getActivity(), "",
                "Building Recommendation...", true);
        cal = Double.parseDouble(user.getNumber("meal_cal_balance").toString());
        Log.i("meal cal balance in recmmendationpage:", cal + "");
        noOfMeal = Integer
                .parseInt(user.getNumber("mealNo_balance").toString());
        snack_cal_balance = Double.parseDouble(user.getNumber(
                "snack_cal_balance").toString());
        noOfSnack = Integer.parseInt(user.getNumber("snackNo_balance")
                .toString());

        user = ParseUser.getCurrentUser();
//        // Fetch Facebook user info if the session is active
//        //Session session = ParseFacebookUtils.getSession();
        if (user != null) {
//
            int height = (int) user.getNumber("height");
            Double weight = user.getDouble("weight");

            if (height == 0 || weight == 0) {
                bannerbutton.setVisibility(View.VISIBLE);
                mealbannerbutton.setVisibility(View.GONE);
            } else {
                mealbannerbutton.setVisibility(View.VISIBLE);
                bannerbutton.setVisibility(View.GONE);

                Double Meall = Double.parseDouble(user.getNumber("mealNo_balance").toString());
                Double call = Double.parseDouble(user.getNumber("meal_cal_balance").toString());
                Double mealcal = (call / Meall);
                DecimalFormat form = new DecimalFormat("0.00");
                mealbannerbutton.setText("Meal Target: " + form.format(mealcal) + "kcal");

                if (mealcal <= 0) {
                    mealbannerbutton.setVisibility(View.GONE);
                } else if (Double.isInfinite(mealcal)) {
                    mealbannerbutton.setVisibility(View.GONE);
                }
            }
        }
        if (!user.has("halal_state")) {
            user.put("halal_state", 0);
            user.put("veg_state", 0);
            user.put("seafood_state", 0);
            user.put("Budget", 10);
            user.put("CheckBox_search_pref", false);
            user.saveInBackground();
        }
        budget = Double.parseDouble(user.getNumber("Budget").toString());
        Log.i("System trying to query recommendation based on user preferences",
                "Loading...");
        ParseObject.registerSubclass(Dish.class);
        initialiseAdapter();

        updateRecommendation();
        progressDialog.dismiss();
    }


    //updation of foodpicks
    private void updateRecommendation() {
        // ***check snack cal balance...
        // scenario : when snack balance is negative, we transfer the excess
        // to cal balance to limit the cal balance intake to avoid
        // overeating
        if (snack_cal_balance < 0) {
            cal = (cal + snack_cal_balance);
            transferSnackCalToMealCal(snack_cal_balance);
        }
        // update display on the headings before the list view to let user know
        // their balance of their calorie bank
        if (noOfMeal > 0 && cal >= 0) {

            updateData();

        } else if (noOfMeal > 0) {
            String s = "Exceeded meal calories intake for the day by " + cal
                    * -1 + " kcal";
            Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
            completedMeal();
        } else {
            mealAdapter.clear();
            completedMeal();
        }

    }


    public void initialiseAdapter() {
        // Add your initialization code here

        mealAdapter = new DishAdapter(getActivity(), new ArrayList<Dish>());
        if (mLatitude != null && mLongitude != null) {
            mealAdapter.setLocation(Double.parseDouble(mLatitude),
                    Double.parseDouble(mLongitude));
        }
        listView.setAdapter(mealAdapter);

        if (mLatitude == null && mLongitude == null) {
            gpsenable.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
            noresult.setVisibility(View.INVISIBLE);
        }


    }


    private void completedMeal() {
        String cm = getString(R.string.congrats);
        Toast.makeText(getActivity(), cm, Toast.LENGTH_LONG).show();
        listView.setBackgroundDrawable(getResources().getDrawable(R.drawable.finishedmeal));
    }

    // parsequery to ind recommended meals accoring to cal budget and filters on
    // preferences + location
    public void updateData() {
        try {
            ParseGeoPoint point = null;
            ParseQuery<Dish> query = ParseQuery.getQuery(Dish.class);
            Log.d("mLongitude,mLatitude", mLongitude + "," + mLatitude);
            if (mLongitude != null && mLatitude != null) {
                point = new ParseGeoPoint(Double.parseDouble(mLatitude),
                        Double.parseDouble(mLongitude));
            }
            if (user.getInt("halal_state") == 1) {
                query.whereEqualTo("halal", true);
            } else if (user.getInt("halal_state") == 2) {
                query.whereEqualTo("halal", false);
            }// else user is indifferent on whether it is halal or not so halal
            // field is not a query constraint

            if (user.getInt("veg_state") == 1) {
                query.whereEqualTo("vegetarian", true);
            } else if (user.getInt("veg_state") == 2) {
                query.whereEqualTo("vegetarian", false);
            }// else user is indifferent on whether it is veg or not so
            // vegetarian field is not a query constraint

            if (user.getInt("seafood_state") == 1) {
                query.whereEqualTo("seafood", true);
            } else if (user.getInt("seafood_state") == 2) {
                query.whereEqualTo("seafood", false);
            }// else user is indifferent on whether it is seafood or not so
            // seafood field is not a query constraint
            //query.whereLessThanOrEqualTo("price", 10.0);
            query.whereLessThanOrEqualTo("price", budget);
            // exclude recommendation which has been reported as outdated. only
            // include those with (undefined) field in counter
            query.whereDoesNotExist("report_counter");
            //exclude user created meal
            query.whereDoesNotExist("user_created_meal");
            // location based recommendation
            if (point != null) {
                Log.d("point", point + "");
                query.whereWithinKilometers("location", point, LOCATION_RADIUS);
            }
            // calorie based recommendation

            query.whereLessThanOrEqualTo("energyKcal", cal / noOfMeal);

            query.setLimit(10);
            query.findInBackground(new FindCallback<Dish>() {

                @Override
                public void done(List<Dish> dishes, ParseException error) {

                    if (dishes != null) {
                        //Randomizing the dishes in the food picks
                        Collections.shuffle(dishes);
                        if (dishes.size() > 0) {
                            // recStatusView.setText("");
                            mealAdapter.clear();
                            // ***showing only 10 recommendations each time
                            for (int i = 0; i < dishes.size(); i++) {

                                mealAdapter.add(dishes.get(i));
                            }
                            // mealAdapter.setDishesList(dishes1);
                            Log.d("final list", dishes.size() + "");
                        } else {

                            noresult.setVisibility(View.VISIBLE);
                            gpsenable.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), "No Results Found Around This Area...",
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }
            });
        } catch (Exception e) {
            Log.d("updateData", e.getMessage() + "cal/meal :" + cal + "/"
                    + noOfMeal);
        }

    }

    private void transferSnackCalToMealCal(Double snack_cal_balance2) {
        // this method transfer negative snack cal bal to meal cal
        // bal and update the snack cal balance to 0
        Log.d("snack cal bal <0", snack_cal_balance2 + "");
        user.increment("meal_cal_balance", snack_cal_balance2);
        Log.d("meal_cal_balance", user.get("meal_cal_balance") + "");
        user.put("snack_cal_balance", 0.0);
        Log.d("snack_cal_balance", user.get("snack_cal_balance") + "");
        snack_cal_balance = 0.0;
        try {
            user.save();
        } catch (ParseException e) {
            Log.d("Error: ", e.getMessage() + "");
        }
    }


    protected void updateValuesFromBundle(Bundle savedInstanceState) {
        Log.i(TAG, "Updating values from bundle");
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle,
            // and
            // make sure that the Start Updates and Stop Updates buttons are
            // correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(
                    REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState
                        .getBoolean(REQUESTING_LOCATION_UPDATES_KEY);
            }

            // Update the value of mCurrentLocation from the Bundle and update
            // the
            // UI to show the correct latitude and longitude.
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                // Since LOCATION_KEY was found in the Bundle, we can be sure
                // that
                // mCurrentLocationis not null.
                mCurrentLocation = savedInstanceState
                        .getParcelable(LOCATION_KEY);
            }

            // Update the value of mLastUpdateTime from the Bundle and update
            // the UI.
            if (savedInstanceState.keySet().contains(
                    LAST_UPDATED_TIME_STRING_KEY)) {
                mLastUpdateTime = savedInstanceState
                        .getString(LAST_UPDATED_TIME_STRING_KEY);
            }
            // updateUI();
        }

    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(
                        (GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(LocationServices.API).build();

        createLocationRequest();
        mGoogleApiClient.connect();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);

            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            // updateUI();
            if (mCurrentLocation != null) {
                mLatitude = (String.valueOf(mCurrentLocation
                        .getLatitude()));
                mLongitude = (String.valueOf(mCurrentLocation
                        .getLongitude()));

                Log.i("userlocation onConnected", "longitude: "
                        + mLongitude + "latitude: "
                        + mLatitude);

            } else {
                Log.d(TAG, "Fail to get the location!");


                AlertDialog.Builder alert = new AlertDialog.Builder(
                        getActivity());
                alert.setTitle("Turn ON Location Services");
                alert.setMessage("Please turn on Google location services for Better Recommendation");
                alert.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                // to open settings and let user turn on the
                                // google loc svs
                                // Provider not enabled, prompt user to enable
                                // it
                                Toast.makeText(
                                        getActivity(),
                                        "Please turn on GPS and Refresh the Food Picks Page",
                                        Toast.LENGTH_LONG).show();
                                Intent myIntent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                getActivity().startActivity(myIntent);
                                dialog.dismiss();

                            }
                        });
                alert.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.dismiss();
                            }
                        });
                alert.setCancelable(true);
                alert.show();

            }
        }
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @SuppressLint("LongLogTag")
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        // updateUI();
        if (mCurrentLocation != null) {
            mLatitude = (String.valueOf(mCurrentLocation
                    .getLatitude()));
            mLongitude = (String.valueOf(mCurrentLocation
                    .getLongitude()));
            initialiseAdapter();
            Log.d("initialiseAdapter onLocationChanged", mLatitude
                    + "," + mLongitude);

        }
        Log.d("LOCATION: ", "onLocationChanged: " + mLatitude + ";"
                + mLongitude);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    public void onDisconnected() {
        Log.i(TAG, "Disconnected");
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY,
                mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY,
                mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

    }


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                Intent intent = getActivity().getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                getActivity().overridePendingTransition(0, 0);
                getActivity().finish();

                getActivity().overridePendingTransition(0, 0);
                startActivity(intent);
            }
        }, 2000);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();


    }

}