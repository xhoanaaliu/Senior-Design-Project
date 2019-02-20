package com.example.gerard.afinal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsDesignHolder>  {
    private Context context;



    public SettingsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public SettingsDesignHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_settings_design,viewGroup,false);
        return new SettingsDesignHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsDesignHolder settingsDesignHolder, int i) {
     
    }

    @Override
    public int getItemCount() {
        return 0;
    }



    public class SettingsDesignHolder extends RecyclerView.ViewHolder{
        private TextView textViewUsername,textViewAccount,textViewNotifications,textViewContact,textViewRate,textViewComment;
        private ImageView imageViewAccount,imageViewNotifications,imageViewContact,imageViewRate,imageViewComment;
        private Switch switchNotifications;
        public SettingsDesignHolder(View view){
         super(view);
         textViewUsername=view.findViewById(R.id.textViewUserName);
         textViewAccount=view.findViewById(R.id.textViewAccount);
         textViewNotifications=view.findViewById(R.id.textViewNotifications);
         textViewContact=view.findViewById(R.id.textViewContact);
         textViewRate=view.findViewById(R.id.textViewRate);
         textViewComment=view.findViewById(R.id.textViewComment);

         imageViewAccount=view.findViewById(R.id.imageViewAccount);
         imageViewNotifications=view.findViewById(R.id.imageViewAccount);
         imageViewContact=view.findViewById(R.id.imageViewAccount);
         imageViewRate=view.findViewById(R.id.imageViewAccount);
         imageViewComment=view.findViewById(R.id.imageViewAccount);


        }

    }



    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
