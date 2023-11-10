package de.joergschmid.lavajump.GameObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public abstract class GameObject {

    protected Bitmap image;
    protected Rect rect = new Rect();
    protected int width;
    protected int height;
    protected int bottom;
    protected int left;

    public GameObject(Bitmap bitmap, int left, int bottom, int width, int height) {
        image = bitmap;
        this.bottom = bottom;
        this.left = left;
        this.width = width;
        this.height = height;
        updateRect();
    }

    public void draw(Canvas canvas, int xOffset, int yOffset, double scalingFactor) {
        canvas.drawBitmap(image, null,
                new Rect(
                        (int) (scalingFactor * (getLeft() - xOffset)),
                        (int) (scalingFactor * getTop()) + yOffset,
                        (int) (scalingFactor * (getRight() - xOffset)),
                        (int) (scalingFactor * getBottom()) + yOffset),
                null);
    }

    protected void updateRect() {
        rect.set(getLeft(), getTop(), getRight(), getBottom());
    }

    public void moveBy(int x, int y) {
        left += x;
        bottom += y;
        updateRect();
    }


    public int getLeft() {
        return left;
    }

    public int getTop() {
        return bottom - height;
    }

    public int getRight() {
        return left + width;
    }

    public int getBottom() {
        return bottom;
    }

    public Rect getRect() {
        return rect;
    }

    public int getGlobalCenterX() {
        return left + width / 2;
    }

    public int getGlobalCenterY() {
        return bottom + height / 2;
    }
}
