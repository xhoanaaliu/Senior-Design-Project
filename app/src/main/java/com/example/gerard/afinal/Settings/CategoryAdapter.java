package com.example.gerard.afinal.Settings;

import android.app.FragmentManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gerard.afinal.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends  RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private ArrayList<Category> categoryList = new ArrayList<Category>();
    private Context context;
    private FragmentManager fragmentManager;
    public LayoutInflater layoutinflater;

    public CategoryAdapter(Context context){
        this.context = context;
        layoutinflater=LayoutInflater.from(context);
    }
    public void setcastlist(ArrayList<Category> categoryList){
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryName, categoryName1;
        public ImageView categoryPicture, categoryPicture1;
        public LinearLayout relativeLayout, layoutCategory, layoutCategory1;
        public Context context;
        public LinearLayout linearLayout;

        //public FragmentManager fragmentManager;
        public MyViewHolder(View view) {
            super(view);
            categoryName = (TextView) view.findViewById(R.id.categoryName);
            categoryName1 = (TextView) view.findViewById(R.id.categoryName1);
            categoryPicture = (ImageView) view.findViewById(R.id.categoryPicture);
            categoryPicture1 = (ImageView) view.findViewById(R.id.categoryPicture1);
            relativeLayout = (LinearLayout) view.findViewById(R.id.itemLayout);
            layoutCategory = (LinearLayout) view.findViewById(R.id.layout_category);
            layoutCategory1 = (LinearLayout) view.findViewById(R.id.layout_category1);
            context = view.getContext();
            linearLayout = (LinearLayout) itemView.findViewById(R.id.itemLayout);

            //fragmentManager = context.getFragmentManager();
        }
    }

    public CategoryAdapter(ArrayList<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Log.d("test", "onBindViewHolder size " + categoryList.size()+"position "+position);
        final Category category = categoryList.get(position);
        //context = holder.context;

        holder.categoryName.setText(categoryList.get(0).getGetCategoryName());
        holder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                //showRemoveOrUpdateDialog();
                //Log.d("test", "setOnLongClickListener size " + userList.size()+", position "+position);
                //EventBus.getDefault().post(new RemoveOrUpdateUserFromUserAdapterToHomeEvent(position, user));
                //EventBus.getDefault().post(new UpdateUserEvent(userList.get(position),position));
                return false;
            }
        });
        holder.layoutCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //asd
            }
        });

        holder.layoutCategory1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sdasd
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}