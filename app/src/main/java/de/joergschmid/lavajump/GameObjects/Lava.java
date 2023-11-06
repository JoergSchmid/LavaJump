package de.joergschmid.lavajump.GameObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import de.joergschmid.lavajump.GameSurface;

public class Lava extends GameObject {

    private final String LOG_TAG = Lava.class.getSimpleName();

    public Lava(Bitmap bitmap, int Left, int size) {
        super(bitmap, Left, GameSurface.getScreenHeight());
        width = size;
        right = left + size;
        top = GameSurface.getGround();
        calculateRect();
    }

    @Override
    public void draw(Canvas canvas, int xOffset, int yOffset, double scalingFactor) {
        canvas.drawBitmap(image, null,
                new Rect((int) (scalingFactor * (left - xOffset)), (int) (scalingFactor * top) + yOffset, (int) (scalingFactor * (right - xOffset)), bottom),
                null);
    }
}
