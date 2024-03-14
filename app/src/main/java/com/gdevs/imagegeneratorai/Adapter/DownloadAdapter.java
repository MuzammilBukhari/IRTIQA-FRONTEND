package com.gdevs.imagegeneratorai.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.gdevs.imagegeneratorai.Model.DataModel;
import com.gdevs.imagegeneratorai.R;

import java.io.File;
import java.util.ArrayList;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {
    private Activity activity;
    private File file;
    ArrayList<DataModel> mData;

    public DownloadAdapter(Activity paramActivity, ArrayList<DataModel> paramArrayList) {
        this.mData = paramArrayList;
        this.activity = paramActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final DataModel jpast = (DataModel) this.mData.get(position);
        this.file = new File(jpast.getFilePath());
        if (!this.file.isDirectory()) {

            Glide.with(this.activity).load(this.file).apply(new RequestOptions().placeholder(R.color.black).error(android.R.color.black).optionalTransform(new RoundedCorners(1))).into(holder.imagevi);
        }
        holder.imagevi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a content URI using FileProvider
                Uri imageUri = FileProvider.getUriForFile(activity, activity.getPackageName()+".fileprovider", new File(jpast.getFilePath()));

                // Create an intent to view the image in the gallery
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(imageUri, "image/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Grant read permission

                // Verify that there's an app available to handle this intent
                if (intent.resolveActivity(activity.getPackageManager()) != null) {
                    activity.startActivity(intent);
                } else {
                    // Handle the case where no suitable app is installed to view images
                    Toast.makeText(activity, "No app available to view images", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder
            extends RecyclerView.ViewHolder {
        private RelativeLayout cardView;
        private ImageView imagePlayer;
        private ImageView imagevi, shareIV, deleteIV;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imagevi = ((ImageView) itemView.findViewById(R.id.imageView));
        }
    }
}
