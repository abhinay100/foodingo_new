package com.foodingo.activities.Activity;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.foodingo.activities.Fragments.BlogFragment;
import com.foodingo.activities.Fragments.FeedbackFragment;
import com.foodingo.activities.Fragments.CalstoryFragment;
import com.foodingo.activities.Fragments.ExploreMealsFragment;
import com.foodingo.activities.Fragments.FoodCartFragment;
import com.foodingo.activities.Fragments.FoodPicksFragment;
import com.foodingo.activities.Fragments.LoginFragment;
import com.foodingo.activities.Fragments.PreferencesFragment;
import com.foodingo.activities.Helpers.UIHelper;
import com.foodingo.activities.R;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParsePush;
import com.parse.ParseUser;


/**
 * MainActivity takes care of fragment handling for food picks, food cart, explore meals, calstory, feedback, blog and preferences.
 */
public class MainActivity extends AppCompatActivity {

    UIHelper font;
    Context mContext;
    ParseUser user;
    Boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        font = new UIHelper(mContext);
        user = ParseUser.getCurrentUser();

        //setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        // display the first navigation drawer view on app launch
        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(1);
        }
        //setup drawer
        setupDrawer();

    }


    private void setupDrawer() {

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName("Rehan Arif").withEmail("rehan@foodingo.com.sg").withIcon(getResources().getDrawable(R.drawable.boyprofile))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();


        //create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(this)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_foodpicks).withIcon(R.drawable.ic_food_picks).withTypeface(font.boldFont),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_exploremeals).withIcon(R.drawable.ic_food_explore).withTypeface(font.boldFont),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_foodcart).withIcon(R.drawable.ic_food_cart).withTypeface(font.boldFont),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_preferences).withIcon(R.drawable.ic_profile_male).withTypeface(font.boldFont),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_calstory).withIcon(R.drawable.ic_calstory).withTypeface(font.boldFont),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_blog).withIcon(R.drawable.ic_blog).withTypeface(font.boldFont),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_feedback).withIcon(R.drawable.ic_feedback).withTypeface(font.boldFont),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_logout).withIcon(R.drawable.ic_logout).withTypeface(font.boldFont)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //return fragment based on selected position
                        displayView(position);
                        return false;
                    }
                })
                .build();

        //to keep user updated on latest meal and cal balance
        SecondaryDrawerItem mealcredititem = new SecondaryDrawerItem().withName(R.string.drawer_item_mealcredit).withBadge("1210 kcal").withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.colorPrimary));
        SecondaryDrawerItem snackcredititem = new SecondaryDrawerItem().withName(R.string.drawer_item_snackcredit).withBadge("120 kcal").withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.colorPrimary));

        result.addStickyFooterItem(mealcredititem.withSelectable(false));
        result.addStickyFooterItem(snackcredititem.withSelectable(false));

    }


    //to display the fragments and change title of actionbar
    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 1:
                //when i create this and switch between framgents, do i need to clean this up fragment up?
                fragment = new FoodPicksFragment();
                title = getString(R.string.drawer_item_foodpicks);
                break;
            case 2:
                fragment = new ExploreMealsFragment();
                title = getString(R.string.drawer_item_exploremeals);
                break;
            case 3:
                fragment = new FoodCartFragment();
                title = getString(R.string.drawer_item_foodcart);
                break;
            case 4:
                fragment = new PreferencesFragment();
                title = getString(R.string.drawer_item_preferences);
                break;
            case 5:
                fragment = new CalstoryFragment();
                title = getString(R.string.drawer_item_calstory);
                break;
            case 6:
                fragment = new BlogFragment();
                title = getString(R.string.drawer_item_blog);
                break;
            case 8:
                fragment = new FeedbackFragment();
                title = getString(R.string.drawer_item_feedback);
                break;
            case 9:
                showLogoutDialog(mContext);
                Toast.makeText(this, "log out", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        //based on selected item in navigation drawer, show the correct fragment using fragment transaction to replace and update the title in action bar.
        if (fragment != null) {

            //create fragment manager which allows you to interact with different fragments inside the activity
            FragmentManager fragmentManager = getSupportFragmentManager();

            //this transaction will allow us to add,attach,dettach,remove,replace,animate transition etc
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.fragment_content, fragment).commit();
            //fragmentManager.beginTransaction().replace(R.id.container_body, fragment).addToBackStack(null).commit();

            // Update the action bar title
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(title);

        }
    }

    private void showLogoutDialog(Context context){
        UIHelper.showMessageDialogWithCallback(context, "Do you want to logout?", "YES", "NO", new MaterialDialog.ButtonCallback() {

            @Override
            public void onPositive(MaterialDialog dialog) {
                super.onPositive(dialog);
                ParseUser user = ParseUser.getCurrentUser();
//

                if(ParseFacebookUtils.getSession()!=null)
                    ParseFacebookUtils.getSession().closeAndClearTokenInformation();
                ParseUser.logOut();

                startActivity(new Intent(mContext,LoginActivity.class));
                finish();
            }

            @Override
            public void onNegative(MaterialDialog dialog) {
                super.onNegative(dialog);
                //changeSelectedDrawer();
            }
        });
    }

    //double press back to exit
    @Override
    public void onBackPressed()
    {
        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;

        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


}
