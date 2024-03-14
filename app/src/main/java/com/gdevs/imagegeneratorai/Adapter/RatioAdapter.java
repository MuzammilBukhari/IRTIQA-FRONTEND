package com.gdevs.imagegeneratorai.Adapter;

import static com.gdevs.imagegeneratorai.R.color.colorAccent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gdevs.imagegeneratorai.Model.RatioModel;
import com.gdevs.imagegeneratorai.R;
import com.gdevs.imagegeneratorai.Utils.PrefManager;

import java.util.List;

public class RatioAdapter extends RecyclerView.Adapter<RatioAdapter.RatioViewHolder> {

    private List<RatioModel> ratioList;
    private Context context;

    private int clickedPosition = -1;
    private PrefManager prefManager;

    public RatioAdapter(List<RatioModel> ratioList, Context context) {
        this.ratioList = ratioList;
        this.context = context;
        prefManager = new PrefManager(context);
    }

    @NonNull
    @Override
    public RatioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ratio, parent, false);
        return new RatioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatioViewHolder holder, @SuppressLint("RecyclerView") int position) {
        RatioModel ratio = ratioList.get(position);
        holder.ivIcon.setImageResource(ratio.getIconResourceId());
        holder.tvRatio.setText(ratio.getRatioText());

        // Set default colors
        holder.ivIcon.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorAccent)));
        holder.tvRatio.setTextColor(context.getResources().getColor(R.color.colorAccent));
        holder.llRatio.setBackgroundResource(R.drawable.rounder_corner_border_2);

        // Check if the current item is selected
        if (clickedPosition == position) {
            // Set colors for the selected item
            holder.ivIcon.setImageTintList(ColorStateList.valueOf(Color.WHITE));
            holder.tvRatio.setTextColor(Color.WHITE);
            holder.llRatio.setBackgroundResource(R.drawable.rounder_corner);
        }

        // Set click listener
        holder.llRatio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update the clicked position
                clickedPosition = position;
                prefManager.setString("SIZE",ratio.getRatioText());
                // Notify adapter about the data change
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return ratioList.size();
    }

    public static class RatioViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvRatio;

        LinearLayout llRatio;

        public RatioViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvRatio = itemView.findViewById(R.id.tv_ratio);
            llRatio = itemView.findViewById(R.id.llRatio);
        }
    }
}

