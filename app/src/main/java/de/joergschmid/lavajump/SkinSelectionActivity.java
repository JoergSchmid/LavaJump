package de.joergschmid.lavajump;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SkinSelectionActivity extends AppCompatActivity {

    private final String LOG_TAG = SkinSelectionActivity.class.getSimpleName();

    SharedPreferences sharedPrefs;
    Context context;

    private TableLayout layout_buySkin;
    private SelectedSkinView selectedSkinView;
    private RelativeLayout.LayoutParams layoutParams;

    private List<Skin> skins = new ArrayList<>();
    private ImageButton[] skinButtons;

    private TextView tv_coins;

    private ImageView[] imageSelected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        sharedPrefs = context.getSharedPreferences(context.getString(R.string.preferences_key), Context.MODE_PRIVATE);

        selectedSkinView = new SelectedSkinView(getApplicationContext());

        setContentView(R.layout.activity_skin_selection);

        layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        addContentView(selectedSkinView, layoutParams);

        registerImageSelections();
        registerBackButtonListener();
        registerSkinButtonListeners();

        tv_coins = findViewById(R.id.tv_coins_skin_selection);
        layout_buySkin = findViewById(R.id.layout_buy_skin);
    }

    @Override
    protected void onResume() {
        super.onResume();

        tv_coins.setText(String.valueOf(sharedPrefs.getInt(getString(R.string.coins_key), 0)));

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
    private void registerBackButtonListener() {
        final ImageButton backButton = findViewById(R.id.button_back_activity_skin_selection);

        backButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    backButton.setImageDrawable(getResources().getDrawable(R.drawable.back_highscore));
                    Intent intent = new Intent(SkinSelectionActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    backButton.setImageDrawable(getResources().getDrawable(R.drawable.back_highscore_pressed));
                }
                return true;

            }
        });
    }


    @SuppressLint("ClickableViewAccessibility")
    private void registerSkinButtonListeners() {

        skins.clear();
        skins.add(new Skin("red"));
        skins.add(new Skin("yellow"));
        skins.add(new Skin("green"));
        skins.add(new Skin("blue"));
        skins.add(new Skin("pink"));
        skins.add(new Skin("orange"));
        skins.add(new Skin("turquoise"));
        skins.add(new Skin("purple"));

        skinButtons = new ImageButton[8];
        skinButtons[0] = findViewById(R.id.button_skin_red);
        skinButtons[1] = findViewById(R.id.button_skin_yellow);
        skinButtons[2] = findViewById(R.id.button_skin_green);
        skinButtons[3] = findViewById(R.id.button_skin_blue);
        skinButtons[4] = findViewById(R.id.button_skin_pink);
        skinButtons[5] = findViewById(R.id.button_skin_orange);
        skinButtons[6] = findViewById(R.id.button_skin_turquoise);
        skinButtons[7] = findViewById(R.id.button_skin_purple);

        for(int i = 0; i < skinButtons.length; i++) {
            final int I = i;

            skinButtons[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(!checkIfBought(skins.get(I).getName()))
                        return true;
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        selectedSkinView.setSkin(skins.get(I).getName());
                        selectedSkinView.setPosition(event.getX() + skinButtons[I].getLeft(), event.getY() + skinButtons[I].getTop());
                        selectedSkinView.setVisibility(View.VISIBLE);
                    } else if(event.getAction() == MotionEvent.ACTION_MOVE) {
                        selectedSkinView.setPosition(event.getX() + skinButtons[I].getLeft(), event.getY() + skinButtons[I].getTop());
                    } else if(event.getAction() == MotionEvent.ACTION_UP) {
                        setImageSelection(skins.get(I).getName(), (int) event.getX() + skinButtons[I].getLeft(), (int) event.getY() + skinButtons[I].getTop());
                        selectedSkinView.setVisibility(View.INVISIBLE);
                    }
                    return true;
                }
            });
        }
    }

    private void setImageSelection(String skin, int x, int y) {
        if(!Utility.isSkinBought(context, skin))
            return;

        // Check, if selected skin is already on the list
        String[] skinSelection = Utility.loadSelectedSkins(context);
        for(int i = 0; i < 4; i++)
            if(skinSelection[i].equals(skin))
                return;

            // Check, which spot was chosen by the player
        for(int i = 0; i < 4; i++) {
            if(getRect(imageSelected[i]).contains(x, y)) {
                imageSelected[i].setImageBitmap(Utility.getBitmap(context,skin));
                Utility.saveSelectedSkin(context, skin, i);
                Log.v(LOG_TAG, "SharedPrefs: " + sharedPrefs.getAll());
                break;
            }
        }
    }

    private Rect getRect(ImageView imageView) {
        return new Rect(imageView.getLeft(), imageView.getTop(), imageView.getRight(), imageView.getBottom());
    }

    private void registerImageSelections() {
        imageSelected = new ImageView[4];
        imageSelected[0] = findViewById(R.id.skin_selected_0);
        imageSelected[1] = findViewById(R.id.skin_selected_1);
        imageSelected[2] = findViewById(R.id.skin_selected_2);
        imageSelected[3] = findViewById(R.id.skin_selected_3);

        String[] skins = Utility.loadSelectedSkins(context);

        for(int i = 0; i < 4; i++) {
            imageSelected[i].setImageBitmap(Utility.getBitmap(context, skins[i]));
        }
    }

    @SuppressLint("SetTextI18n")
    private boolean checkIfBought(String skin) {
        if(Utility.isSkinBought(context, skin))
            return true;

        final String SKIN = skin;
        final TextView tv_buySkin = findViewById(R.id.tv_buy_skin);
        Button b_yes = findViewById(R.id.button_buy_skin_yes);
        Button b_no = findViewById(R.id.button_buy_skin_no);

        if(getCost(skin) > Utility.loadCoins(context)) {
            tv_buySkin.setText(getString(R.string.not_enough_coins) + getCost(skin));
            b_yes.setText(R.string.ok);
            b_no.setVisibility(View.INVISIBLE);
        } else {
            tv_buySkin.setText(getString(R.string.buy_1) + Skin.getLocalisedName(context, skin) + getString(R.string.buy_2) + getCost(skin) + getString(R.string.buy_3));
            b_yes.setText(R.string.yes);
            b_no.setVisibility(View.VISIBLE);
        }

        layout_buySkin.setVisibility(View.VISIBLE);

        b_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utility.loadCoins(context) >= getCost(SKIN)) {
                    Utility.saveSkinPurchase(context, SKIN);
                    Utility.removeCoins(context, getCost(SKIN));
                    tv_coins.setText(String.valueOf(Utility.loadCoins(context)));
                    layout_buySkin.setVisibility(View.INVISIBLE);
                } else {
                    layout_buySkin.setVisibility(View.INVISIBLE);
                }
            }
        });

        b_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_buySkin.setVisibility(View.INVISIBLE);
            }
        });
        return false;
    }

    private int getCost(String skin) {
        /*switch(skin) {
            case "pink":
                return 50;
            case "orange":
                return 50;
            default:
                return 100;
        }*/
        return 50;
    }
}
