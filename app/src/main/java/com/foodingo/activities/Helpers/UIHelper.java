package com.foodingo.activities.Helpers;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Calendar;

/**
 * Created by admin on 19-01-2016.
 */
public class UIHelper {

    public Typeface regularFont, boldFont, boldItalicFont, italicFont, ocrFont, bebasNeue, Abadi;

    public UIHelper(Context context) {
        regularFont = Typeface.createFromAsset(context.getAssets(), "fonts/SmartPesa-Regular.ttf");
        boldFont = Typeface.createFromAsset(context.getAssets(), "fonts/SmartPesa-Bold.ttf");
        boldItalicFont = Typeface.createFromAsset(context.getAssets(), "fonts/SmartPesa-BoldItalic.ttf");
        italicFont = Typeface.createFromAsset(context.getAssets(), "fonts/SmartPesa-Italic.ttf");
        ocrFont = Typeface.createFromAsset(context.getAssets(), "fonts/ocr.ttf");
        bebasNeue = Typeface.createFromAsset(context.getAssets(), "fonts/BebasNeue.otf");
        Abadi = Typeface.createFromAsset(context.getAssets(), "fonts/Abadi MT Condensed Light Regular.ttf");

    }

    public static void showMessageDialogWithCallback(Context ctx, String message, String positiveText, String negativeText, MaterialDialog.ButtonCallback callback) {
        new MaterialDialog.Builder(ctx)
                .content(message)
                .callback(callback)
                .positiveText(positiveText)
                .negativeText(negativeText)
                .show();
    }

    public static void showToast(Context ctx, String message, int length) {
        Toast.makeText(ctx, message, length).show();
    }

    public static double calculateAge(int date, int month, int year) {

        double periodMonthBorn = (year - 1) * 12 + month;

        Calendar now = Calendar.getInstance();
        double yearNow = now.get(Calendar.YEAR);
        double monthNow = now.get(Calendar.MONTH);
        double periodMonth = (yearNow - 1) * 12 + monthNow;

        double age = (periodMonth - periodMonthBorn) / 12;

        return age;

    }

    public static boolean validateHeight(Context context, int height){
        if(height > 0 && height < 251){
            return true;
        }else {
            Toast.makeText(context,"Please enter valid height. Tallest man on earth, Sultan Kosen is 251 cm.",Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public static boolean validateWeight(Context context, double weight){
        if(weight > 0 && weight < 251){
            return true;
        }else {
            Toast.makeText(context,"Please enter valid weight. Heaviest man on earth, Manuel Uribe is 561 kg.",Toast.LENGTH_LONG).show();
            return false;
        }
    }


}
