package com.example.gerard.afinal;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.example.gerard.afinal.Account.ProfileFragment;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<HomePage.Event> mDataset;
    //private List<Bitmap> imageList;
    private static int counter = 0;
    private StorageReference mStorageRef;
    private Bitmap bitmap;
    private static int clickedPosition;
    private Context context;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        // each data item is just a string in this case
        public TextView title;
        public TextView location;
        public TextView date;
        public TextView times;
        public ImageView img;
        public TextView username;
        private static ItemClickListener itemClickListener;
        public MyViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.textTitle);
            location = (TextView) v.findViewById(R.id.textLocation);
            date = (TextView) v.findViewById(R.id.textDate);
            img = (ImageView) v.findViewById(R.id.listImage);
            username = (TextView) v.findViewById(R.id.textView11);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);


            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_LONG).show();
                }
            });
        }

        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }



        @Override
        public void onClick(View view) {
            itemClickListener.onItemClick(view, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            itemClickListener.onItemClick(view,getAdapterPosition(),true);
            return true;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<HomePage.Event> myDataset, Context context) {
        mDataset = myDataset;
        mStorageRef = FirebaseStorage.getInstance().getReference();
        this.context = context;
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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if(!mDataset.isEmpty()) {
            String newDateFormat;
            if(mDataset.get(position).getDate() != null) {
                Date date = new Date();
                String dateIn = mDataset.get(position).getDate();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    date = formatter.parse(dateIn);
                }
                catch (ParseException e){

                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("LLLL");
                newDateFormat = dateFormat.format(date);
                String day  = (String) DateFormat.format("dd", date);
                newDateFormat = day + " " + newDateFormat;
                holder.date.setText(newDateFormat);
            }
            else {
                holder.date.setVisibility(View.GONE);
            }
            if(mDataset.get(position).getTitle() != null)
                holder.title.setText(mDataset.get(position).getTitle());
            else
                holder.title.setVisibility(View.GONE);

            if(mDataset.get(position).getLocation() != null)
                holder.location.setText(mDataset.get(position).getLocation());
            else
                holder.location.setVisibility(View.GONE);

            if(mDataset.get(position).getCreatorID() != null){
                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Map<String, String> value = (Map<String, String>) dataSnapshot.getValue();
                        String user = value.get("username");
                        String id = dataSnapshot.getKey();
                        if(user != null && id.equals(mDataset.get(position).getCreatorID())){
                            holder.username.setText("Created by " + user);
                        }

                        holder.username.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bundle bundle = new Bundle();
                                bundle.putString("ID", mDataset.get(position).getCreatorID());

                                ProfileFragment profileFragment = new ProfileFragment();
                                profileFragment.setArguments(bundle);
                                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, profileFragment)
                                        .addToBackStack(null).commit();

                            }
                        });

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

           // holder.times.setText(mDataset.get(position).getTime());
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

            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClick(View view, int position, boolean isLongClick) {
                    if(isLongClick) {
                        Bundle bundle = new Bundle();
                        bundle.putString("title", mDataset.get(position).getTitle());
                        bundle.putString("location", mDataset.get(position).getLocation());
                        bundle.putString("date", mDataset.get(position).getDate());
                        bundle.putString("description", mDataset.get(position).getDescription());
                        bundle.putString("time", mDataset.get(position).getTime());
                        bundle.putString("url", mDataset.get(position).getURL());
                        bundle.putString("category", mDataset.get(position).getCategory());
                        bundle.putString("ID", mDataset.get(position).getID());


                        Fragment fragment = new EventFragment();
                        fragment.setArguments(bundle);
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fragment)
                                .addToBackStack(null).commit();
                    }
                    else {
                        Bundle bundle = new Bundle();
                        bundle.putString("title", mDataset.get(position).getTitle());
                        bundle.putString("location", mDataset.get(position).getLocation());
                        bundle.putString("date", mDataset.get(position).getDate());
                        bundle.putString("description", mDataset.get(position).getDescription());
                        bundle.putString("time", mDataset.get(position).getTime());
                        bundle.putString("url", mDataset.get(position).getURL());
                        bundle.putString("category", mDataset.get(position).getCategory());
                        bundle.putString("ID", mDataset.get(position).getID());

                        Fragment fragment = new EventFragment();
                        fragment.setArguments(bundle);
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fragment)
                                .addToBackStack(null).commit();
                    }

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
