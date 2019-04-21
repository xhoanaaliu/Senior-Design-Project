package com.example.gerard.afinal;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<HomePage.Event> mDataset;
    private List<Bitmap> imageList;
    private static int counter = 0;

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
    public MyAdapter(List<HomePage.Event> myDataset, List<Bitmap> image) {
        mDataset = myDataset;
        imageList = image;
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
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(!imageList.isEmpty())
            holder.img.setImageBitmap(imageList.get(0));
        if(!mDataset.isEmpty()) {
            holder.title.setText(mDataset.get(position).getTitle());
            holder.location.setText(mDataset.get(position).getLocation());
            holder.date.setText(mDataset.get(position).getDate());
            Log.d("SIZEEEEEEEE", "" + mDataset.size());
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
