package de.joergschmid.lavajump;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

public class SelectedSkinView extends View {

    private final String LOG_TAG = SkinSelectionActivity.class.getSimpleName();

    private Bitmap skinBitmap;

    private int xPos;
    private int yPos;
    private int radius;

    public SelectedSkinView(Context context) {
        super(context);

        // Just default values
        setSkin("blue");
        setPosition(0,0);
        setVisibility(INVISIBLE);
    }

    public void setSkin(String skin) {
        skinBitmap = Utility.getBitmap(getContext(), skin);
    }

    public void setPosition(float x, float y) {
        xPos = (int) x;
        yPos = (int) y;
        radius = (int) (skinBitmap.getWidth() * 0.8);
        invalidate();
    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(skinBitmap, null, new Rect((int) xPos - radius, yPos - radius, xPos + radius, yPos + radius), null);
    }

}
