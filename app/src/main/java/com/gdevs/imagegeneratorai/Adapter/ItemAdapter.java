package com.gdevs.imagegeneratorai.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gdevs.imagegeneratorai.Model.Item;
import com.gdevs.imagegeneratorai.R;
import com.gdevs.imagegeneratorai.Utils.PrefManager;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<Item> itemList;
    private Context context;
    private int mypos = 0;
    private PrefManager prefManager;

    public ItemAdapter(List<Item> itemList,Context context) {
        this.itemList = itemList;
        this.context = context;
        prefManager = new PrefManager(context);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_style, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Item item = itemList.get(position);
        holder.nameTextView.setText(item.getName());

        int resourceId = getResourceId(context, item.getName());
        holder.imageView.setImageResource(resourceId);


        if (mypos == position){
            holder.itenCheck.setVisibility(View.VISIBLE);
            prefManager.setString("type",itemList.get(mypos).getName());
        }else {
            holder.itenCheck.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mypos = position;
                if (mypos == position){
                    holder.itenCheck.setVisibility(View.VISIBLE);
                }else {
                    holder.itenCheck.setVisibility(View.GONE);
                }

                notifyDataSetChanged();
                prefManager.setString("type",item.getName());
            }
        });

    }

    private static int getResourceId(Context context, String imageName) {
        String modifiedImageName = "img_"+imageName.toLowerCase().replace(" ", "_");
        return context.getResources().getIdentifier(
                modifiedImageName, "drawable", context.getPackageName());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView itenCheck;
        TextView nameTextView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            itenCheck = itemView.findViewById(R.id.itenCheck);
        }
    }
}
