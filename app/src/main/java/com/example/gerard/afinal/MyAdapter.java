package com.example.gerard.afinal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<HomePage.Event> mDataset;
    //private List<Bitmap> imageList;
    private static int counter = 0;
    private StorageReference mStorageRef;
    private Bitmap bitmap;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public TextView location;
        public TextView date;
        public ImageView img;
        public MyViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.textTitle);
            location = (TextView) v.findViewById(R.id.textLocation);
            date = (TextView) v.findViewById(R.id.textDate);
            img = (ImageView) v.findViewById(R.id.listImage);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<HomePage.Event> myDataset) {
        mDataset = myDataset;
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);

        return new MyViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if(!mDataset.isEmpty()) {
            holder.title.setText(mDataset.get(position).getTitle());
            holder.location.setText(mDataset.get(position).getLocation());
            holder.date.setText(mDataset.get(position).getDate());
            String url = mDataset.get(position).getURL();

            final StorageReference islandRef = mStorageRef.child(url);

            islandRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String imageURL = uri.toString();
                    Glide.with(getApplicationContext()).load(imageURL).into(holder.img);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
