package com.example.colorpalette;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.NavUtils;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SeekBar;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ColorActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private static final String BLUE = "blue", RED = "red", GREEN = "green", LOG_TAG = "SPRAWDZENIE";
    private ActionBar actionBar;
    private int red, green, blue;
    private Random random = new Random();

    @BindView(R.id.colorLinearLayout)
    LinearLayoutCompat colorLinearLayout;
    @BindView(R.id.seekBarRed)
    SeekBar seekBarRed;
    @BindView(R.id.seekBarGreen)
    SeekBar seekBarGreen;
    @BindView(R.id.seekBarBlue)
    SeekBar seekBarBlue;

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

        this.seekBarRed.setProgress(this.red);
        this.seekBarGreen.setProgress(this.green);
        this.seekBarBlue.setProgress(this.blue);

        updateBackgroundColor();
    }

    private void updateBackgroundColor() {
        int color = Color.rgb(this.red, this.green, this.blue);
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