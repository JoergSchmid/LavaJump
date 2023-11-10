package de.joergschmid.lavajump;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

public class SelectedSkinView extends View {

    private Bitmap skinBitmap;
    private Rect rect;

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
        int radius = (int) (skinBitmap.getWidth() * 0.8);
        rect = new Rect((int) x - radius, (int) y - radius, (int) x + radius, (int) y + radius);
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(skinBitmap, null, rect, null);
    }

}
