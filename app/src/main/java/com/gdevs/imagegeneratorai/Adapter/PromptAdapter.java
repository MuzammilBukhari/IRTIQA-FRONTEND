package com.gdevs.imagegeneratorai.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gdevs.imagegeneratorai.Model.Item;
import com.gdevs.imagegeneratorai.Model.Prompt;
import com.gdevs.imagegeneratorai.R;
import com.gdevs.imagegeneratorai.Utils.PrefManager;

import java.util.List;

public class PromptAdapter extends RecyclerView.Adapter<PromptAdapter.ItemViewHolder> {
    private List<Prompt> itemList;
    private Context context;
    private PrefManager prefManager;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Prompt obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public PromptAdapter(List<Prompt> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
        prefManager = new PrefManager(context);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prompt, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Prompt item = itemList.get(position);
        holder.promptText.setText(item.getPrompts());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, item, position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView promptText;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            promptText = itemView.findViewById(R.id.promptText);
        }
    }
}
