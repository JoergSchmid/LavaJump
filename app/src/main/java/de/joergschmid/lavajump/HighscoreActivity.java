package de.joergschmid.lavajump;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class HighscoreActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_highscore);

        registerBackButtonListener();

        int[] highScores = Utility.loadScore(this);
        String[] hs_skins = Utility.loadScoreSkins(this);

        TextView[] scores = new TextView[5];
        scores[0] = findViewById(R.id.hs_first);
        scores[1] = findViewById(R.id.hs_second);
        scores[2] = findViewById(R.id.hs_third);
        scores[3] = findViewById(R.id.hs_fourth);
        scores[4] = findViewById(R.id.hs_fifth);

        ImageView[] images = new ImageView[5];
        images[0] = findViewById(R.id.hs_first_skin);
        images[1] = findViewById(R.id.hs_second_skin);
        images[2] = findViewById(R.id.hs_third_skin);
        images[3] = findViewById(R.id.hs_fourth_skin);
        images[4] = findViewById(R.id.hs_fifth_skin);

        for(int i = 0; i < 5; i++) {
            if(highScores[i] > 0) {
                scores[i].setText(String.valueOf(highScores[i]));
                images[i].setImageBitmap(Utility.getBitmap(getApplicationContext(), hs_skins[i]));
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
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


    @SuppressLint("ClickableViewAccessibility")
    public void registerBackButtonListener() {
        final ImageButton backButton = findViewById(R.id.button_back_activity_highscore);

        backButton.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_UP) {
                backButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.back_btn, null));
                Intent intent = new Intent(HighscoreActivity.this, MainActivity.class);
                startActivity(intent);
            } else if(event.getAction() == MotionEvent.ACTION_DOWN) {
                backButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.back_btn_pressed, null));
            }
            return true;

        });
    }
}
