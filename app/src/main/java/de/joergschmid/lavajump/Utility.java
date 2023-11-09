package de.joergschmid.lavajump;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class Utility {

    private static final String LOG_TAG = Utility.class.getSimpleName();


    @SuppressLint("ApplySharedPref")
    public static void saveScore(Context context, int ranking, int score, String skin) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getString(R.string.preferences_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        for(int i = 4; i >= ranking; i--) {
            editor.putInt(context.getString(R.string.hs_score_key) + (i+1), sharedPrefs.getInt(context.getString(R.string.hs_score_key) + i, 0));
            editor.putString(context.getString(R.string.hs_skin_key) + (i+1), sharedPrefs.getString(context.getString(R.string.hs_skin_key) + i, "blue"));
        }
        editor.putInt(context.getString(R.string.hs_score_key) + ranking, score);
        editor.putString(context.getString(R.string.hs_skin_key) + ranking, skin);
        editor.commit();
    }


    public static void saveSelectedSkins(Context context, String[] skins) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getString(R.string.preferences_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        for(int i = 0; i < skins.length; i++) {
            editor.putString(context.getString(R.string.skin_selection_menu_key) + i, skins[i]);
        }
        editor.commit();
    }

    public static void saveSelectedSkin(Context context, String skin, int pos) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getString(R.string.preferences_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(context.getString(R.string.skin_selection_menu_key) + pos, skin);
        editor.commit();
    }


    public static void saveSkinPurchase(Context context, String skin) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getString(R.string.preferences_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(context.getString(R.string.skin_bought_key) + skin, true);
        editor.commit();
    }


    public static void saveCoins(Context context, int coins) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getString(R.string.preferences_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(context.getString(R.string.coins_key), coins);
        editor.commit();
    }


    public static void addCoins(Context context, int coins) {
        saveCoins(context, loadCoins(context) + coins);
    }

    public static void removeCoins(Context context, int coins) {
        saveCoins(context, loadCoins(context) - coins);
    }


    public static int[] loadScore(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getString(R.string.preferences_key), Context.MODE_PRIVATE);
        int[] score = new int[5];
        for(int i = 0; i < 5; i++) {
            Log.v(LOG_TAG, "sharedPrefs=" + sharedPrefs.getAll());
            score[i] = sharedPrefs.getInt(context.getString(R.string.hs_score_key) + i, 0);
        }
        return score;
    }

    public static String[] loadScoreSkins(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getString(R.string.preferences_key), Context.MODE_PRIVATE);
        String[] score = new String[5];
        for(int i = 0; i < 5; i++) {
            score[i] = sharedPrefs.getString(context.getString(R.string.hs_skin_key) + i, "blue");
        }
        return score;
    }

    public static String loadSelectedSkin(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getString(R.string.preferences_key), Context.MODE_PRIVATE);
        return sharedPrefs.getString(context.getString(R.string.skin_key), "blue");
    }

    public static String[] loadSelectedSkins(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getString(R.string.preferences_key), Context.MODE_PRIVATE);
        String[] skins = new String[4];
        for(int i = 0; i < 4; i++) {
            skins[i] = sharedPrefs.getString(context.getString(R.string.skin_selection_menu_key) + i, "blue");
        }
        // Check for too much blue
        for(int i = 0, blue = 0; i < 4; i++) {
            if(skins[i].equals("blue"))
                blue++;
            if(blue == 2) {
                skins[0] = "red";
                skins[1] = "yellow";
                skins[2] = "green";
                skins[3] = "blue";
                saveSelectedSkins(context, skins);
                break;
            }
        }
        return skins;
    }


    public static int loadCoins(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getString(R.string.preferences_key), Context.MODE_PRIVATE);
        return sharedPrefs.getInt(context.getString(R.string.coins_key), 0);
    }


    public static boolean isSkinBought(Context context, String skin) {
        if(skin.equals("red") || skin.equals("yellow") || skin.equals("green") || skin.equals("blue"))
            return true;
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getString(R.string.preferences_key), Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean(context.getString(R.string.skin_bought_key) + skin, false);
    }


    public static Bitmap getBitmap(Context context, String skin) {
        switch (skin) {
            case "red":
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.player_red);
            case "yellow":
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.player_yellow);
            case "green":
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.player_green);
            case "pink":
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.player_pink);
            case "orange":
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.player_orange);
            case "turquoise":
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.player_turquoise);
            case "purple":
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.player_purple);
            default:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.player_blue);
        }
    }


    public static Point getScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        return size;
    }

    @SuppressLint("ClickableViewAccessibility")
    public static void setBackButtonEventListener(AppCompatActivity activity, int buttonViewId) {
        final ImageButton backButton = activity.findViewById(buttonViewId);
        backButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                backButton.setImageDrawable(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.back_btn, null));
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
            } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                backButton.setImageDrawable(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.back_btn_pressed, null));
            }
            return true;
        });
    }
}
