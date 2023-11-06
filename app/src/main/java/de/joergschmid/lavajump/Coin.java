package de.joergschmid.lavajump;

import android.graphics.Bitmap;

public class Coin extends GameObject{

    private final String LOG_TAG = Coin.class.getSimpleName();

    public Coin(Bitmap bitmap, int Left, int Bottom) {
        super(bitmap, Left, Bottom);
    }

}
