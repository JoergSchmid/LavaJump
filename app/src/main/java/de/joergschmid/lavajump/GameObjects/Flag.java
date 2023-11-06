package de.joergschmid.lavajump.GameObjects;

import android.graphics.Bitmap;

import de.joergschmid.lavajump.GameSurface;

public class Flag extends GameObject {

    private final String LOG_TAG = Flag.class.getSimpleName();

    public Flag(Bitmap bitmap, int Left) {
        super(bitmap, Left, GameSurface.getGround());
    }

    public void setPosition(int posX, int posY) { // left and bottom
        move(posX - left, posY - bottom);
    }
}
