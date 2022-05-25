package com.example.colorpalette;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorViewHolder> {
    private final LayoutInflater layoutInflater;
    private List<String> colors = new ArrayList<>();
    private IColorClickedListener colorClickedListener;

    public ColorAdapter(LayoutInflater layoutInflater) {

        this.layoutInflater = layoutInflater;
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
    }

    public void remove(int position) {
        this.colors.remove(position);
    }

    public void clicked(int position) {
        if (this.colorClickedListener != null) {
            this.colorClickedListener.onColorClicked(colors.get(position));
        }
    }

    public void setColorClickedListener(IColorClickedListener colorClickedListener) {
        this.colorClickedListener = colorClickedListener;
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