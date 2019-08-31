package com.example.asus.agents;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private Context mContext;
    private List<Items> mData;

    public ProductAdapter(Context context, List<Items> data){
        mContext = context;
        mData = data;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_product,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.productName.setText(mData.get(position).getmProductName());
        //holder.thumbImage.setImageResource(mData.get(position).getmImageURL());
        Picasso.get().load(mData.get(position).getmImageURL()).fit().centerInside().into(holder.thumbImage);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView productName;
        ImageView thumbImage;

        public MyViewHolder(View itemView) {
            super(itemView);

            productName = (TextView)itemView.findViewById(R.id.product_name);
            thumbImage = (ImageView)itemView.findViewById(R.id.product_image);
        }
    }
}
