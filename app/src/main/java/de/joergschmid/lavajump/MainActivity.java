package de.joergschmid.lavajump;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPrefs;

    private ImageButton startButton;
    private ImageButton highscoreButton;
    private ImageButton quitButton;
    private ImageButton playerSelectionButton;
    private RelativeLayout playerSelectionView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPrefs = getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);

        TextView version = findViewById(R.id.tv_version_1_activity_main);
        version.setText(BuildConfig.VERSION_NAME);
        TextView version2 = findViewById(R.id.tv_version_2_activity_main);
        version2.setText(BuildConfig.VERSION_NAME);

        registerStartButtonListener();
        registerHighscoreButtonListener();
        registerCloseButtonListener();
        registerCoinButtonListener();

        playerSelectionView = findViewById(R.id.player_selection_view);
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerPlayerSelectionButtonListener();
        registerPlayerSelectionViewListener();

        //Screen settings, mainly for hiding the navigation bar. Always set both in onResume() and in onWindowFocusChanged()
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );

        TextView coins = findViewById(R.id.coins_activity_main);
        coins.setText(String.valueOf(Utility.loadCoins(getApplicationContext()))); // Doesn´t work...

        String skin = sharedPrefs.getString(getString(R.string.skin_key), "blue");

        playerSelectionButton.setImageBitmap(Utility.getBitmap(getApplicationContext(), skin));

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(getString(R.string.last_version_key), BuildConfig.VERSION_CODE);
        editor.apply();
    }

    //Also in onResume() for initial hiding of the navigation bar!
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
    private void registerStartButtonListener() {
        startButton = findViewById(R.id.button_start_activity_main);
        startButton.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_UP) {
                startButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.button_play, null));
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else if(event.getAction() == MotionEvent.ACTION_DOWN) {
                startButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.button_play_pressed, null));
            }
            return true;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void registerHighscoreButtonListener() {
        highscoreButton = findViewById(R.id.button_highscore_activity_main);
        highscoreButton.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_UP) {
                highscoreButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.button_highscore, null));
                Intent intent = new Intent(MainActivity.this, HighscoreActivity.class);
                startActivity(intent);
            } else if(event.getAction() == MotionEvent.ACTION_DOWN) {
                highscoreButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.button_highscore_pressed, null));
            }
            return true;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void registerCloseButtonListener() {
        quitButton = findViewById(R.id.button_close_activity_main);
        quitButton.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_UP) {
                finish();
            } else if(event.getAction() == MotionEvent.ACTION_DOWN) {
                quitButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.button_quit_pressed, null));
            } else {
                quitButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.button_quit, null));
            }
            return true;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void registerPlayerSelectionButtonListener() {
        playerSelectionButton = findViewById(R.id.button_player_selection_activity_main);
        playerSelectionButton.setOnLongClickListener(v -> {
            playerSelectionButton.setClickable(false);
            playerSelectionView.setVisibility(View.VISIBLE);
            return true;
        });
        playerSelectionButton.setOnClickListener(v -> {
            String skin = sharedPrefs.getString(getString(R.string.skin_key), "blue");
            SharedPreferences.Editor editor = sharedPrefs.edit();
            String[] skins = Utility.loadSelectedSkins(getApplicationContext());
            for(int i = 0; i < 4; i++) { // Maybe control for no matches
                if(skin.equals(skins[i])) {
                    editor.putString(getString(R.string.skin_key), skins[(i+1)%4]);
                    playerSelectionButton.setImageBitmap(Utility.getBitmap(getApplicationContext(), skins[(i+1)%4]));
                    break;
                } else if(i == 3) {
                    editor.putString(getString(R.string.skin_key), skins[0]);
                    playerSelectionButton.setImageBitmap(Utility.getBitmap(getApplicationContext(), skins[0]));
                }
            }
            editor.apply();
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void registerPlayerSelectionViewListener() {
        ImageButton[] button_skin = new ImageButton[4];
        button_skin[0] = findViewById(R.id.button_skin_0);
        button_skin[1] = findViewById(R.id.button_skin_1);
        button_skin[2] = findViewById(R.id.button_skin_2);
        button_skin[3] = findViewById(R.id.button_skin_3);

        final String[] skins = Utility.loadSelectedSkins(getApplicationContext());

        for(int i = 0; i < 4; i++) {
            final int n = i; // For access in onTouchListener
            button_skin[i].setImageBitmap(Utility.getBitmap(getApplicationContext(),skins[i]));
            button_skin[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putString(getString(R.string.skin_key), skins[n]);
                    editor.apply();
                    playerSelectionButton.setImageBitmap(Utility.getBitmap(getApplicationContext(), skins[n]));
                    playerSelectionButton.setClickable(true);
                    playerSelectionView.setVisibility(View.INVISIBLE);
                    return true;
                }
            });
        }
    }

    private void registerCoinButtonListener() {
        ImageButton coinButton = findViewById(R.id.button_coins_activity_main);
        coinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SkinSelectionActivity.class);
                startActivity(intent);
            }
        });
    }

    public void openChangelog(View v) {
        TextView vs1 = findViewById(R.id.tv_version_1_activity_main);
        TextView vs2 = findViewById(R.id.tv_version_2_activity_main);
        TextView cl = findViewById(R.id.tv_changelog_activity_main);
        vs1.setVisibility(View.INVISIBLE);
        vs2.setVisibility(View.VISIBLE);
        cl.setVisibility(View.VISIBLE);
        setVersionTextColor();
    }

    public void closeChangelog(View v) {
        TextView vs1 = findViewById(R.id.tv_version_1_activity_main);
        TextView vs2 = findViewById(R.id.tv_version_2_activity_main);
        TextView cl = findViewById(R.id.tv_changelog_activity_main);
        vs1.setVisibility(View.VISIBLE);
        vs2.setVisibility(View.INVISIBLE);
        cl.setVisibility(View.INVISIBLE);
    }

    private boolean isNewVersion() {
        return sharedPrefs.getInt(getString(R.string.last_version_key), 0) != BuildConfig.VERSION_CODE;
    }

    private void setVersionTextColor() {
        TextView vs = findViewById(R.id.tv_version_1_activity_main);
        if (isNewVersion()) {
            vs.setTextColor(Color.RED);
            vs.setHighlightColor(Color.RED);
        } else {
            vs.setTextColor(Color.BLACK);
        }
    }
}
