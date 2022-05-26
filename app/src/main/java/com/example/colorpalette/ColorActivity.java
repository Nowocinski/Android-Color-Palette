package com.example.colorpalette;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ColorActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    private static final String LOG_TAG = "Testowy@" + MainActivity.class.getSimpleName();
    public static final String OLD_COLOR_KEY = "old_color", COLOR_IN_HEX_KEY = "color_in_hex";
    private static final String BLUE = "blue", RED = "red", GREEN = "green";
    private ActionBar actionBar;
    private int red, green, blue;
    private Random random = new Random();
    private String oldColor;

    @BindView(R.id.colorLinearLayout)
    LinearLayoutCompat colorLinearLayout;
    @BindView(R.id.seekBarRed)
    SeekBar seekBarRed;
    @BindView(R.id.seekBarGreen)
    SeekBar seekBarGreen;
    @BindView(R.id.seekBarBlue)
    SeekBar seekBarBlue;
    @BindView(R.id.generateButton)
    Button generateButton;
    @BindView(R.id.redLabel)
    TextView redLabel;
    @BindView(R.id.greenLabel)
    TextView greenLabel;
    @BindView(R.id.blueLabel)
    TextView blueLabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);
        this.actionBar = getSupportActionBar();
        this.actionBar.setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        this.seekBarRed.setOnSeekBarChangeListener(this);
        this.seekBarGreen.setOnSeekBarChangeListener(this);
        this.seekBarBlue.setOnSeekBarChangeListener(this);

        Intent intent = getIntent();
        this.oldColor = intent.getStringExtra(this.OLD_COLOR_KEY);

        if (this.oldColor != null) {
            int color = Color.parseColor(this.oldColor);
            this.red = Color.red(color);
            this.green = Color.green(color);
            this.blue = Color.blue(color);

            this.updateSeekBars();
            this.updateBackgroundColor();

            this.generateButton.setVisibility(View.GONE);
            this.actionBar.setTitle(R.string.edit_color);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.generateButton)
    public void generate() {
        this.red = random.nextInt(256);
        this.green = random.nextInt(256);
        this.blue = random.nextInt(256);

        this.updateSeekBars();

        updateBackgroundColor();
    }

    private void updateSeekBars() {
        this.seekBarRed.setProgress(this.red);
        this.seekBarGreen.setProgress(this.green);
        this.seekBarBlue.setProgress(this.blue);
    }

    @OnClick(R.id.saveButton)
    public void save() {
        Intent data = new Intent();
        data.putExtra(this.COLOR_IN_HEX_KEY, String.format("#%02X%02X%02X", this.red, this.green, this.blue));
        if (this.oldColor != null) {
            data.putExtra(this.OLD_COLOR_KEY, this.oldColor);
            data.putExtra("requestCode", MainActivity.REQUEST_CODE_EDIT);
            setResult(RESULT_OK, data);
            finish();
        }
        data.putExtra("requestCode", MainActivity.REQUEST_CODE_CREATED);
        setResult(RESULT_OK, data);
        finish();
    }

    private void updateBackgroundColor() {
        int color = Color.rgb(this.red, this.green, this.blue);
        int textColor = MainActivity.getTextColorFromColor(color);
        this.redLabel.setTextColor(textColor);
        this.greenLabel.setTextColor(textColor);
        this.blueLabel.setTextColor(textColor);
        this.colorLinearLayout.setBackgroundColor(color);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seekBarRed:
                this.red = progress;
                break;
            case R.id.seekBarGreen:
                this.green = progress;
                break;
            case R.id.seekBarBlue:
                this.blue = progress;
                break;
        }

        this.updateBackgroundColor();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(this.RED, this.red);
        outState.putInt(this.GREEN, this.green);
        outState.putInt(this.BLUE, this.blue);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.red = savedInstanceState.getInt(this.RED, 0);
        this.green = savedInstanceState.getInt(this.GREEN, 0);
        this.blue = savedInstanceState.getInt(this.BLUE, 0);
    }
}