package com.example.gerard.afinal.Account;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.gerard.afinal.R;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountDesignHolder>  {
    private Context context;


    public AccountAdapter(Context context) {
        this.context = context;
    }
    public class AccountDesignHolder extends RecyclerView.ViewHolder{
        TextView textViewUser,textViewEmail,textViewPassword,textViewLogOut;
        ImageView imageViewLogOut;
        Switch switchNotifications;
        public AccountDesignHolder(View view){
            super(view);
            textViewUser=view.findViewById(R.id.textViewUser);
            textViewEmail=view.findViewById(R.id.textViewEmail);
            textViewPassword=view.findViewById(R.id.textViewPassword);
            textViewLogOut=view.findViewById(R.id.textViewLogOut);

            imageViewLogOut=view.findViewById(R.id.imageViewLogOut);



        }


    }

    @NonNull
    @Override
    public AccountDesignHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_account_design,viewGroup,false);
        return new AccountDesignHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final AccountDesignHolder settingsDesignHolder, int i) {

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