package com.example.gerard.afinal;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyCustomListAdapter extends ArrayAdapter<EventInfo> {

    Context mCtx;
    int resource;
    ArrayList<EventInfo> eventInfoList;
    private DatabaseReference ref2,eventRef;
    FirebaseUser user;
    private String userID;
    ArrayList<EventInfo> eventFragmentArrayList;
    private StorageReference mStorageRef;
    DatabaseReference userRef;
   public MyCustomListAdapter(Context mCtx, int resource, ArrayList<EventInfo> eventFragmentArrayList){
       super(mCtx,resource,eventFragmentArrayList);
       this.mCtx=mCtx;
       this.resource=resource;
       this.eventFragmentArrayList=eventFragmentArrayList;
   }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.display_event,null);
        final TextView textViewCategory = view.findViewById(R.id.textViewCategory);
        final TextView textViewDate = view.findViewById(R.id.textViewDate);
        final TextView textViewDescription = view.findViewById(R.id.textViewDescription);
        final TextView textViewLocation = view.findViewById(R.id.textViewLocation);
        final TextView textViewTime = view.findViewById(R.id.textViewTime);
        final TextView textViewTitle = view.findViewById(R.id.textViewTitle);
        final ImageView imageViewEvent = view.findViewById(R.id.imageViewEvent);
        ref2 = FirebaseDatabase.getInstance().getReference("GoingTo");
        eventRef = FirebaseDatabase.getInstance().getReference("Event");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID=user.getUid();
         EventInfo e1 = eventFragmentArrayList.get(position);
         textViewCategory.setText("Category : " + e1.getCategory());
        textViewDate.setText("Date : " + e1.getDate());
        textViewDescription.setText("Description : " + e1.getDescription());
        final StorageReference islandRef = mStorageRef.child(e1.getImageName());
        islandRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageURL = uri.toString();
                Glide.with(getContext()).load(imageURL).into(imageViewEvent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@android.support.annotation.NonNull Exception exception) {
                // Handle any errors
            }
        });

        textViewLocation.setText("Location : " + e1.getLocation());
        textViewTime.setText("Time : " + e1.getTime());
        textViewTitle.setText("Title : " + e1.getTitle());

        return view;
    }

}
