package com.example.colorpalette;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorViewHolder> {
    public static final String COLORS_KEY = "colors";
    private final LayoutInflater layoutInflater;
    private final SharedPreferences sharedPreferences;
    private List<String> colors = new ArrayList<>();
    private IColorClickedListener colorClickedListener;

    public ColorAdapter(LayoutInflater layoutInflater, SharedPreferences sharedPreferences) {

        this.layoutInflater = layoutInflater;
        this.sharedPreferences = sharedPreferences;

        String colorsJSON = this.sharedPreferences.getString(this.COLORS_KEY, "[]");
        try {
            JSONArray jsonArray = new JSONArray(colorsJSON);
            for (int i = 0; i < jsonArray.length(); i++) {
                this.colors.add(i, jsonArray.getString(i));
            }
            notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = this.layoutInflater.inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
        return new ColorViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        String colorInHex = this.colors.get(position);
        holder.setColor(colorInHex);
    }

    @Override
    public int getItemCount() {
        return this.colors.size();
    }

    public void add(String color) {
        this.colors.add(color);
        notifyItemInserted(this.colors.size() - 1);

        this.storePreferences();
    }

    private void storePreferences() {
        try {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < this.colors.size(); i++) {
                jsonArray.put(i, this.colors.get(i));
            }
            SharedPreferences.Editor editor = this.sharedPreferences.edit();
            editor.putString(this.COLORS_KEY, jsonArray.toString());
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void remove(int position) {
        this.colors.remove(position);
        notifyItemRemoved(position);
        this.storePreferences();
    }

    public void clicked(int position) {
        if (this.colorClickedListener != null) {
            this.colorClickedListener.onColorClicked(colors.get(position));
        }
    }

    public void setColorClickedListener(IColorClickedListener colorClickedListener) {
        this.colorClickedListener = colorClickedListener;
    }

    public void replace(String oldColor, String colorInHex) {
        int indexOf = this.colors.indexOf(oldColor);
        this.colors.set(indexOf, colorInHex);
        notifyItemChanged(indexOf);
        this.storePreferences();
    }

    public void clear() {
        this.colors.clear();
        notifyDataSetChanged();
        this.sharedPreferences.edit().clear().apply();
    }

    public interface IColorClickedListener {
        void onColorClicked(String colorInHex);
    }
}

class ColorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private String color;
    private TextView textView;
    private final ColorAdapter colorAdapter;

    public ColorViewHolder(@NonNull View itemView, ColorAdapter colorAdapter) {
        super(itemView);
        this.textView = (TextView) itemView;
        this.textView.setOnClickListener(this);
        this.colorAdapter = colorAdapter;
    }

    public void setColor(String color) {
        this.color = color;
        this.textView.setText(color);
        this.textView.setBackgroundColor(Color.parseColor(color));
    }

    public String getColor() {
        return color;
    }

    @Override
    public void onClick(View v) {
        this.colorAdapter.clicked(getAdapterPosition());
    }
}