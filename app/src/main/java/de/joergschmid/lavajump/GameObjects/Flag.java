package de.joergschmid.lavajump.GameObjects;

import android.graphics.Bitmap;

import de.joergschmid.lavajump.GameSurface;

public class Flag extends GameObject {
    public Flag(Bitmap bitmap, int x) {
        super(bitmap, x, GameSurface.getGround(), bitmap.getWidth(), bitmap.getHeight());
    }
}
