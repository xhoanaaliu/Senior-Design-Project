package com.example.gerard.afinal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsDesignHolder>  {
    private Context context;
    private CardView account;
    private CardView comment;
    private CardView rate;
    private CardView contact;


    public SettingsAdapter(Context context) {
        this.context = context;
    }
    public class SettingsDesignHolder extends RecyclerView.ViewHolder{
         TextView textViewUserName,textViewAccount,textViewNotifications,textViewContact,textViewRate,textViewComment;
         ImageView imageViewAccount,imageViewNotifications,imageViewContact,imageViewRate,imageViewComment;
         Switch switchNotifications;
        public SettingsDesignHolder(View view){
            super(view);
            textViewUserName=view.findViewById(R.id.textViewUserName);
            textViewAccount=view.findViewById(R.id.textViewAccount);
            textViewNotifications=view.findViewById(R.id.textViewEmail);
            textViewContact=view.findViewById(R.id.textViewContact);
            textViewRate=view.findViewById(R.id.textViewRate);
            textViewComment=view.findViewById(R.id.textViewComment);
            imageViewAccount=view.findViewById(R.id.imageViewAccount);
            imageViewNotifications=view.findViewById(R.id.imageViewAccount);
            imageViewContact=view.findViewById(R.id.imageViewAccount);
            imageViewRate=view.findViewById(R.id.imageViewAccount);
            imageViewComment=view.findViewById(R.id.imageViewAccount);
             account = view.findViewById(R.id.card_view_account);
             comment=view.findViewById(R.id.card_view_comment);
             rate = view.findViewById(R.id.card_view_rate);
             contact=view.findViewById(R.id.card_view_contact);
             account.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     AppCompatActivity activity = (AppCompatActivity) v.getContext();
                     AccountFragment afr = new AccountFragment();
                     activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,afr,"Account").addToBackStack(null).commit();
                 }
             });
             comment.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     AppCompatActivity activity = (AppCompatActivity) v.getContext();
                     CommentFragment cfr = new CommentFragment();
                     activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,cfr,"Comment").addToBackStack(null).commit();
                 }
             });
             rate.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                 }
             });
             contact.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     AppCompatActivity activity = (AppCompatActivity) v.getContext();
                     ContactFragment contact = new ContactFragment();
                     activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,contact,"Contact").addToBackStack(null).commit();
                 }
             });
        }


    }

    @NonNull
    @Override
    public SettingsDesignHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_settings_design,viewGroup,false);
        return new SettingsDesignHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final SettingsDesignHolder settingsDesignHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }









    public Context getContext() {

        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
