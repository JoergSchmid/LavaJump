package de.joergschmid.lavajump.GameObjects;

import android.graphics.Bitmap;

public class Coin extends GameObject{

    public Coin(Bitmap bitmap, int left, int bottom) {
        super(bitmap, left, bottom, bitmap.getWidth(), bitmap.getHeight());
    }

}
