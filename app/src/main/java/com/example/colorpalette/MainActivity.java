package com.example.colorpalette;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colorpalette.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity implements ColorAdapter.IColorClickedListener {
    private static final String OLD_COLOR_KEY = "old_color";
    public static final int REQUEST_CODE_CREATED = 1, REQUEST_CODE_EDIT = 2;
    public static final String LOG_TAG = "Testowy@" + MainActivity.class.getSimpleName();
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private ActivityResultLauncher<Intent> intentLaunch;
    private RecyclerView colorRecyclerView;
    private ColorAdapter colorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        /*this.intentLaunch = registerForActivityResult(
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
        );*/

        this.intentLaunch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent intent = result.getData();

                            if (intent.getIntExtra("requestCode", 0) == REQUEST_CODE_CREATED) {
                                String colorInHex = result.getData().getStringExtra("color_in_hex");

                                Snackbar.make(findViewById(R.id.fab), getString(R.string.new_color_created, colorInHex), Snackbar.LENGTH_LONG)
                                        //.setAction("Action", null)
                                        .show();
                                colorAdapter.add(colorInHex);
                            } else if(intent.getIntExtra("requestCode", 0) == REQUEST_CODE_EDIT) {
                                String colorInHex = intent.getStringExtra(ColorActivity.COLOR_IN_HEX_KEY);
                                String oldColor = intent.getStringExtra(ColorActivity.OLD_COLOR_KEY);

                                colorAdapter.replace(oldColor, colorInHex);
                            }
                        }
                    }
                });

        this.colorRecyclerView = findViewById(R.id.colorRecyclerView);
        this.colorAdapter = new ColorAdapter(getLayoutInflater());
        this.colorAdapter.setColorClickedListener(this);
        this.colorRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.colorRecyclerView.setAdapter(this.colorAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // ColorViewHolder colorViewHolder = (ColorViewHolder) viewHolder;
                int position = viewHolder.getAdapterPosition();
                colorAdapter.remove(position);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(this.colorRecyclerView);
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
            this.colorAdapter.clear();
            return true;
        } else if (id == R.id.action_add) {
            addColor();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addColor() {
        Intent intent = new Intent(MainActivity.this, ColorActivity.class);
        intent.putExtra("requestCode", this.REQUEST_CODE_CREATED);
        this.intentLaunch.launch(intent);
    }

    @Override
    public void onColorClicked(String colorInHex) {
        Intent intent = new Intent(this, ColorActivity.class);
        intent.putExtra(this.OLD_COLOR_KEY, colorInHex);
        this.intentLaunch.launch(intent);
    }
}