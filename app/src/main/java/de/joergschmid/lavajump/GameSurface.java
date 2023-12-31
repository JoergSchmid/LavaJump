package de.joergschmid.lavajump;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.joergschmid.lavajump.GameObjects.Coin;
import de.joergschmid.lavajump.GameObjects.Flag;
import de.joergschmid.lavajump.GameObjects.Lava;
import de.joergschmid.lavajump.GameObjects.Player;


public class GameSurface extends View {
    private enum GameStatus {
        RUNNING,
        PAUSED,
        FINISHED,
        DESTROYED
    }
    private GameStatus status = GameStatus.DESTROYED;

    private Player player;
    public List<Lava> lava = new ArrayList<>();
    private final List<Coin> coins = new ArrayList<>();
    private final List<Flag> flags = new ArrayList<>();
    private int finalScore;
    private int[] highScores;
    private int ranking;
    private int coinsCollected;
    private String skin;

    private final Bitmap backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background_game);
    private final Bitmap groundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
    private final Bitmap lavaBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lava);
    private final Bitmap pauseBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pause);
    private final Bitmap pausePressedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pause_pressed);
    private final Bitmap unpauseBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.unpause);
    private final Bitmap unpausePressedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.unpause_pressed);
    private final Bitmap restartBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.restart);
    private final Bitmap restartPressedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.restart_pressed);
    private final Bitmap backBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.back);
    private final Bitmap backPressedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.back_pressed);
    private final Bitmap flagBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flag);
    private final Bitmap flagFirstBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flag_first);
    private final Bitmap flagSecondBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flag_second);
    private final Bitmap flagThirdBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flag_third);
    private final Bitmap coinOneBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.coin_one);

    private boolean pausePressed = false;
    private boolean restartPressed = false;
    private boolean backPressed = false;


    private Rect topLeft = new Rect();
    private Rect topRight = new Rect();

    private final Paint scorePaint = new Paint();
    private final Paint go_scorePaint = new Paint();

    private static int screenOffset; // Stores how much the screen has to be adjusted so the Player doesn´t go offScreen
    private static int ground;
    private static final int GROUND_HEIGHT = 120;
    private static int screenWidth;
    private static int screenHeight;

    private final double scalingFactor; // Scaling value for the display, used for different display sizes and different player jump heights
    private static int yOffset; // Stores how much objects need to be shifted down due to the scaling of the screen


    public GameSurface(Context context) {
        super(context);

        Point size = Utility.getScreenSize(context);
        screenWidth = size.x;
        screenHeight = size.y;

        ground = context.getResources().getDisplayMetrics().heightPixels - GROUND_HEIGHT;

        scalingFactor = screenHeight / (GROUND_HEIGHT + 1.6 * Player.MAX_HEIGHT);
        yOffset = (int) (ground - scalingFactor * ground);

        definePaints();
        defineRects();

        init();
    }


    private void backToMainActivity() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }


    private void definePaints() {
        scorePaint.setColor(Color.GRAY);
        scorePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        scorePaint.setShadowLayer(10, 0, 0, Color.BLACK);
        scorePaint.setTextSize(42f);

        go_scorePaint.setColor(Color.GRAY);
        go_scorePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        go_scorePaint.setShadowLayer(15, 0, 0, Color.BLACK);
        go_scorePaint.setTextSize(52f);
    }


    private void defineRects() {
        int borderBound = screenHeight / 20;
        int sideLength = screenHeight / 3;

        topLeft = new Rect(borderBound, borderBound, borderBound + sideLength, borderBound + sideLength);
        topRight = new Rect(screenWidth - borderBound - sideLength, borderBound, screenWidth - borderBound, borderBound + sideLength);
    }


    private void init() {
        finalScore = 0;
        screenOffset = -100;
        highScores = Utility.loadScore(getContext());
        ranking = highScores.length;
        flags.clear();
        for(int i = 0; i < highScores.length; i++) {
            if(highScores[i] <= 0)
                break;
            if(i == 0)
                flags.add(new Flag(flagFirstBitmap, highScores[i]));
            else if(i == 1)
                flags.add(new Flag(flagSecondBitmap, highScores[i]));
            else if(i == 2)
                flags.add(new Flag(flagThirdBitmap, highScores[i]));
            else
                flags.add(new Flag(flagBitmap, highScores[i]));
        }

        coinsCollected = 0;
        coins.clear();
        generateCoins();

        skin = Utility.loadSelectedSkin(getContext());
        player = new Player(this, Utility.getBitmap(getContext(), skin), 0, GameSurface.getGround());

        lava.clear();
        generateLava();

        status = GameStatus.RUNNING;
    }


    public void restart() {
        init();
        invalidate();
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        switch (status) {
            case RUNNING:
                drawGameStarted(canvas);
                break;
            case PAUSED:
                drawGamePaused(canvas);
                break;
            case FINISHED:
                drawGameOver(canvas);
                break;
            case DESTROYED:
            default:
                backToMainActivity();
        }
    }


    private void drawGameStarted(Canvas canvas) {
        checkRanking();
        drawBackground(canvas);
        if(pausePressed)
            canvas.drawBitmap(pausePressedBitmap, null, topRight, null);
        else
            canvas.drawBitmap(pauseBitmap, null, topRight, null);
        canvas.drawText(getContext().getString(R.string.score) + player.getScore(), 100, 80, scorePaint);
        canvas.drawText(getContext().getString(R.string.coins) + coinsCollected, 800, 80, scorePaint);
        player.draw(canvas, screenOffset, yOffset, scalingFactor);

        if(!player.update()) {
            status = GameStatus.FINISHED;
            finalScore = player.getScore();

            checkHighscore();
            Utility.addCoins(getContext(), coinsCollected);
        }

        invalidate();
    }


    private void drawGamePaused(Canvas canvas) {
        drawBackground(canvas);
        if(backPressed)
            canvas.drawBitmap(backPressedBitmap, null, topLeft, null);
        else
            canvas.drawBitmap(backBitmap, null, topLeft, null);
        if(pausePressed)
            canvas.drawBitmap(unpausePressedBitmap, null, topRight, null);
        else
            canvas.drawBitmap(unpauseBitmap, null, topRight, null);
        canvas.drawText(getContext().getString(R.string.score) + player.getScore(), 100, 80, go_scorePaint);
        canvas.drawText(getContext().getString(R.string.coins) + coinsCollected, 800, 80, go_scorePaint);

        player.draw(canvas, screenOffset, yOffset, scalingFactor);

        invalidate();
    }


    private void drawGameOver(Canvas canvas) {
        drawBackground(canvas);
        if(backPressed)
            canvas.drawBitmap(backPressedBitmap, null, topLeft, null);
        else
            canvas.drawBitmap(backBitmap, null, topLeft, null);
        if(restartPressed)
            canvas.drawBitmap(restartPressedBitmap, null, topRight, null);
        else
            canvas.drawBitmap(restartBitmap, null, topRight, null);
        canvas.drawText("Score: " + player.getScore(), 100, 100, go_scorePaint);
        canvas.drawText("Coins: " + coinsCollected, 800, 100, go_scorePaint);
        player.draw(canvas, screenOffset, yOffset, scalingFactor);

        invalidate();
    }


    public void drawBackground(Canvas canvas) {
        canvas.drawBitmap(backgroundBitmap, null, new Rect(0, 0, screenWidth, screenHeight), null);
        canvas.drawBitmap(groundBitmap, null, new Rect(0, ground, screenWidth, screenHeight), null);

        for(Lava l : lava) {
            l.draw(canvas, screenOffset, yOffset, scalingFactor);
        }

        for(Flag f : flags) {
            f.draw(canvas, screenOffset, yOffset, scalingFactor);
        }

        for(Coin c : coins) {
            // Check, if a coin got captured
            if(c.getRect().contains(player.getGlobalCenterX(), player.getGlobalCenterY())) {
                coinsCollected++;
                c.moveBy(-screenWidth, 0); // This deletes the coin
            }
            c.draw(canvas, screenOffset, yOffset, scalingFactor);
        }
    }

    private void generateLava() {
        lava.add(new Lava(lavaBitmap, randomIntBetween(200, 500), randomIntBetween(80, 150)));
        int LAVA_NUMBER = 20;
        for(int i = 1; i < LAVA_NUMBER; i++) {
            addLava();
        }
    }

    private void addLava() {
        lava.add(new Lava(lavaBitmap,lava.get(lava.size()-1).getRight() + randomIntBetween(30, 300), randomIntBetween(80, 150)));
    }

    private void generateCoins() {
        coins.add(new Coin(coinOneBitmap, randomIntBetween(500, 2000), ground - randomIntBetween(50, 450)));
        int COIN_NUMBER = 10;
        for (int i = 1; i < COIN_NUMBER; i++)
            addCoin();
    }

    private void addCoin() {
        coins.add(new Coin(coinOneBitmap, coins.get(coins.size()-1).getRight() + randomIntBetween(500, 2000), ground - randomIntBetween(50, 450)));
    }


    private void checkRanking() {
        if(ranking != 0 && player.getScore() > highScores[ranking-1])
            ranking--;
    }


    private void checkHighscore() {
        if(finalScore > highScores[4]) { // New entry
            Utility.saveScore(getContext(), ranking, finalScore, skin);
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        if(event.getAction() == MotionEvent.ACTION_UP) {

            // Un/Pause
            if(topRight.contains(x, y)) {
                if(pausePressed && status == GameStatus.RUNNING)
                    pause();
                else if(pausePressed && status == GameStatus.PAUSED)
                    resume();
                else if(restartPressed && status == GameStatus.FINISHED)
                    restart();
            }

            // Back to MainActivity
            if(topLeft.contains(x, y) && backPressed && (status == GameStatus.PAUSED || status == GameStatus.FINISHED)) {
                backToMainActivity();
                return true;
            }

            pausePressed = false;
            restartPressed = false;
            backPressed = false;
            return true;
        }

        if(event.getAction() == MotionEvent.ACTION_DOWN) {

            // Un/Pause
            if(topRight.contains(x, y)) {
                if(status == GameStatus.RUNNING)
                    pausePressed = true;
                else if(status == GameStatus.PAUSED)
                    pausePressed = true;
                else if(status == GameStatus.FINISHED)
                    restartPressed = true;
                return true;
            }

            if(topLeft.contains(x, y) && (status == GameStatus.PAUSED || status == GameStatus.FINISHED)) {
                backPressed = true;
                return true;
            }

            // Move Left/Right
            if(x < (float) screenWidth / 2)
                player.changeDir(- Player.MAX_SPEED);
            else
                player.changeDir(Player.MAX_SPEED);
            return true;
        }

        if(event.getAction() == MotionEvent.ACTION_MOVE && !topLeft.contains(x, y) && !topRight.contains(x, y)) {
            // Move Left/Right
            if(x < (float) screenWidth / 2)
                player.changeDir(- Player.MAX_SPEED);
            else
                player.changeDir(Player.MAX_SPEED);
            return true;
        }

        return false;
    }


    public int randomIntBetween(int low, int high) {
        return low + new Random().nextInt(high - low);
    }

    public int getScreenOffset() {
        return screenOffset;
    }

    public void setScreenOffset(double posX) {
        posX -= (screenWidth / scalingFactor) / 2;
        screenOffset = screenOffset > posX ? screenOffset : (int) posX;

        // Lava to the left of the screen can be removed
        if(lava.get(0).getRight() < screenOffset) {
            lava.remove(0);
            addLava();
        }

        // Coin to the left of the screen can be removed
        if(coins.get(0).getRight() < screenOffset) {
            coins.remove(0);
            addCoin();
        }
    }

    public static int getGround() {
        return ground;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public void pause() {
        status = GameStatus.PAUSED;
        player.pausePlayTime();
    }

    public void resume() {
        status = GameStatus.RUNNING;
        player.resumePlayTime();
    }
}

