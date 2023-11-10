package de.joergschmid.lavajump.GameObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import de.joergschmid.lavajump.GameSurface;

public class Lava extends GameObject {

    public Lava(Bitmap bitmap, int x, int width) {
        super(bitmap, x, GameSurface.getScreenHeight(), width, GameSurface.getScreenHeight() - GameSurface.getGround());
    }

    @Override
    public void draw(Canvas canvas, int xOffset, int yOffset, double scalingFactor) {
        canvas.drawBitmap(image, null,
                new Rect(
                        (int) (scalingFactor * (getLeft() - xOffset)),
                        (int) (scalingFactor * getTop()) + yOffset,
                        (int) (scalingFactor * (getRight() - xOffset)),
                        getBottom()),
                null);
    }
}
