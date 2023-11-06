package de.joergschmid.lavajump;

import android.content.Context;
import android.graphics.Bitmap;

public class Skin {

    private final String LOG_TAG = Skin.class.getSimpleName();

    private String name;
    private Bitmap image;

    public Skin(String Name, Bitmap bitmap) {
        name = Name;
        image = bitmap;
    }

    // ToDo: Localised names
    public String getName() {
        return name;
    }

    public String getLocalisedName(Context context) {
        switch(name) {
            case "red":
                return context.getString(R.string.red);
            case "yellow":
                return context.getString(R.string.yellow);
            case "green":
                return context.getString(R.string.green);
            case "blue":
                return context.getString(R.string.blue);
            case "pink":
                return context.getString(R.string.pink);
            case "orange":
                return context.getString(R.string.orange);
            case "turquoise":
                return context.getString(R.string.turquoise);
            case "purple":
                return context.getString(R.string.purple);
            default:
                return name;
        }
    }

    public static String getLocalisedName(Context context, String Name) {
        switch(Name) {
            case "red":
                return context.getString(R.string.red);
            case "yellow":
                return context.getString(R.string.yellow);
            case "green":
                return context.getString(R.string.green);
            case "blue":
                return context.getString(R.string.blue);
            case "pink":
                return context.getString(R.string.pink);
            case "orange":
                return context.getString(R.string.orange);
            case "turquoise":
                return context.getString(R.string.turquoise);
            case "purple":
                return context.getString(R.string.purple);
            default:
                return Name;
        }
    }

    public Bitmap getBitmap() {
        return image;
    }
}
