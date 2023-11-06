package de.joergschmid.lavajump;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

public abstract class GameObject {

    private final String LOG_TAG = GameObject.class.getSimpleName();

    protected Bitmap image;
    protected Point center = new Point();
    protected Rect rect = new Rect();
    protected  int width;
    protected int height;
    protected int left;
    protected int top;
    protected int right;
    protected int bottom;

    public GameObject(Bitmap bitmap, int Left, int Bottom) {
        image = bitmap;
        width = image.getWidth();
        height = image.getHeight();
        setBottomLeft(Left, Bottom);
    }

    public void draw(Canvas canvas, int xOffset, int yOffset, double scalingFactor) {
        canvas.drawBitmap(image, null,
                new Rect((int) (scalingFactor * (left - xOffset)), (int) (scalingFactor * top) + yOffset, (int) (scalingFactor * (right - xOffset)), (int) (scalingFactor * bottom) + yOffset),
                null);
    }

    protected void calculateRect() {
        rect.set(left, top, right, bottom);
        center.set((left + right) / 2, (top + bottom) / 2);
    }

    public void move(int x, int y) {
        left += x;
        right += x;
        top += y;
        bottom += y;
        calculateRect();
    }

    public void setBottomLeft(int Left, int Bottom) {
        setBottom(Bottom);
        setLeft(Left);
    }

    public void setBottom(int Bottom) {
        bottom = Bottom;
        top = bottom - height;
        calculateRect();
    }

    public void setLeft(int Left) {
        left = Left;
        right = left + width;
        calculateRect();
    }


    public int getLeft() {
        return left;
    }

    public int getTop() {
        return top;
    }

    public int getRight() {
        return right;
    }

    public int getBottom() {
        return bottom;
    }

    public Point getCenter() {
        return center;
    }

    public Rect getRect() {
        return rect;
    }
}
