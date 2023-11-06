package de.joergschmid.lavajump;

import android.graphics.Bitmap;

public class Player extends GameObject {

    private final String LOG_TAG = Player.class.getSimpleName();

    private GameSurface gameSurface;
    private Timer timer;

    private double speedX = 0.0f;
    public static final int MAX_HEIGHT = 500;
    private double dTime;
    private final double GRAVITY = 0.0035f;
    private final double MAX_SPEED = 0.5f;
    private boolean aboveGround;
    private int score = 0;
    private int distanceToGround; // Like bottom, but can be negative

    private int radius;
    private int sideCheck; // Distance from bottom center of the ball to left and right, where lava is checked



    public Player(GameSurface surface, Bitmap bitmap) {
        super(bitmap, 0, GameSurface.getGround() - MAX_HEIGHT);

        gameSurface = surface;

        radius = image.getWidth() / 2;
        sideCheck = radius / 5;
        timer = new Timer();
    }


    private boolean isInLava() {
        if ((distanceToGround < 0 && aboveGround) || (distanceToGround > 0 && !aboveGround)) {
            for(Lava lava : gameSurface.lava) {
                if((lava.getLeft() <= center.x - sideCheck && lava.getRight() >= center.x - sideCheck) &&
                        (lava.getLeft() <= center.x + sideCheck && lava.getRight() >= center.x + sideCheck)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean update()  {

        dTime = (double) timer.getMarkerTimeElapsed();
        timer.setMarker();

        // For the lava you need to check if there is a change from above to below ground or vice versa, because distanceToGround is a sin wave
        aboveGround = distanceToGround > 0;

        setLeft((int) (left + speedX * dTime));
        distanceToGround = (int) (Math.sin(GRAVITY * timer.getPlayTime()) * MAX_HEIGHT);

        if(isInLava()) {
            setBottom(GameSurface.getGround() + Math.abs(distanceToGround));
            return false;
        }

        // Recalculate the y-position on screen
        setBottom(GameSurface.getGround() - Math.abs(distanceToGround));

        // Recalculate the screenOffset
        gameSurface.setScreenOffset(right);

        // Reflect from left side of the screen
        if(left <= gameSurface.getScreenOffset()) {
            setLeft(2*gameSurface.getScreenOffset() - left);
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

    public double getMaxSpeedX() {
        return MAX_SPEED;
    }

    public int getScore() {
        return score;
    }

    public int getRadius() {
        return radius;
    }
}
