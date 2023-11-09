package de.joergschmid.lavajump.GameObjects;

import android.graphics.Bitmap;
import de.joergschmid.lavajump.GameSurface;
import de.joergschmid.lavajump.Timer;

public class Player extends GameObject {
    public static final double GRAVITY = 0.0035f;
    public static final int MAX_HEIGHT = 500;
    public static final double MAX_SPEED = 0.5f;
    private final GameSurface gameSurface;
    private final Timer timer;
    private double speedX = 0.0f;
    private boolean aboveGround;
    private int score = 0;
    private int distanceToGround;
    private final int collisionMargin;


    public Player(GameSurface surface, Bitmap bitmap, int left, int bottom) {
        super(bitmap, left, bottom, bitmap.getWidth(), bitmap.getHeight());

        gameSurface = surface;

        collisionMargin = image.getWidth() / 4;
        timer = new Timer();
    }


    private boolean isInLava() {
        if ((distanceToGround < 0 && aboveGround) || (distanceToGround > 0 && !aboveGround)) {
            for(Lava lava : gameSurface.lava) {
                if (lava.getRect().left < this.getLeft() + collisionMargin
                        && lava.getRect().right > this.getRight() - collisionMargin)
                    return true;
            }
        }
        return false;
    }

    public boolean update()  {

        double dTime = (double) timer.getMarkerTimeElapsed();
        timer.setMarker();

        // For the lava you need to check if there is a change from above to below ground or vice versa, because distanceToGround is a sin wave
        aboveGround = distanceToGround > 0;

        left = (int) (left + speedX * dTime);
        distanceToGround = (int) (Math.sin(GRAVITY * timer.getPlayTime()) * MAX_HEIGHT);

        if(isInLava()) {
            bottom = GameSurface.getGround() + Math.abs(distanceToGround);
            return false;
        }

        // Recalculate the y-position on screen
        bottom = GameSurface.getGround() - Math.abs(distanceToGround);

        // Recalculate the screenOffset
        gameSurface.setScreenOffset(getRight());

        // Reflect from left side of the screen
        if(left <= gameSurface.getScreenOffset()) {
            left = 2*gameSurface.getScreenOffset() - left;
            speedX = - speedX;
        }

        // Recalculate the score
        score = Math.max(score, left);

        return true;
    }



    public void pausePlayTime() {
        timer.pausePlayTime();
    }

    public void resumePlayTime() {
        timer.resumePlayTime();
    }

    public void changeDir(double speed) {
        speedX = speed;
    }

    public int getScore() {
        return score;
    }
}
