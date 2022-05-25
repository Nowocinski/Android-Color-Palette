package com.example.colorpalette;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colorpalette.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "Testowy@" + MainActivity.class.getSimpleName();
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    ActivityResultLauncher<Intent> intentLaunch;
    RecyclerView colorRecyclerView;
    private ColorAdapter colorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        this.intentLaunch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        String colorInHex = result.getData().getStringExtra("color_in_hex");

                        Snackbar.make(findViewById(R.id.fab), getString(R.string.new_color_created, colorInHex), Snackbar.LENGTH_LONG)
                                //.setAction("Action", null)
                                .show();
                        this.colorAdapter.add(colorInHex);
                    }
                }
        );
        this.colorRecyclerView = findViewById(R.id.colorRecyclerView);
        this.colorAdapter = new ColorAdapter(getLayoutInflater());
        this.colorRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.colorRecyclerView.setAdapter(this.colorAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear) {
            return true;
        } else if (id == R.id.action_add) {
            addColor();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addColor() {
        Intent intent = new Intent(MainActivity.this, ColorActivity.class);
        this.intentLaunch.launch(intent);
    }
}