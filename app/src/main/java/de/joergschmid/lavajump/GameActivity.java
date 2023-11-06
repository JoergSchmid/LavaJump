package de.joergschmid.lavajump;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.util.Log;


public class GameActivity extends AppCompatActivity {

    private final String LOG_TAG = GameActivity.class.getSimpleName();

    private GameSurface gameSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(GameActivity.class.getSimpleName(), "onCreate() called");
        super.onCreate(savedInstanceState);

        gameSurface = new GameSurface(this.getApplicationContext());
        setContentView(gameSurface);
    }

    @Override
    protected void onResume() {
        Log.v(LOG_TAG, "onResume() called");
        super.onResume();

        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );

        gameSurface.resume();
    }

    @Override
    protected void onPause() {
        Log.v(GameActivity.class.getSimpleName(), "onPause() called");
        super.onPause();

        gameSurface.pause();
    }

    @Override
    protected void onStop() {
        Log.v(GameActivity.class.getSimpleName(), "onStop() called");
        super.onStop();
        Thread.currentThread().interrupt();
        this.finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View view = getWindow().getDecorView();
        if(hasFocus) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            );
        }
    }
}
