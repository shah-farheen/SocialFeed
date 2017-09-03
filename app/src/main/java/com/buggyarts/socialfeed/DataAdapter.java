package com.buggyarts.socialfeed;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buggyarts.socialfeed.models.DataModel;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by farheen on 9/2/17
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {

    private Gson gson;
    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<DataModel> dataList;

    private static final int VIEW_TYPE_DATE = 0;
    private static final int VIEW_TYPE_SINGLE = 1;
    private static final int VIEW_TYPE_DOUBLE = 2;

    public DataAdapter(Context context, ArrayList<DataModel> dataList){
        inflater = LayoutInflater.from(context);
        this.mContext = context;
        this.dataList = dataList;
        gson = new Gson();
    }

    @Override
    public int getItemViewType(int position) {
        if(dataList.get(position).isDate()){
            return VIEW_TYPE_DATE;
        }
        else if(dataList.get(position).getImageUrl() == null || dataList.get(position).getText() == null){
            return VIEW_TYPE_SINGLE;
        }
        else {
            return VIEW_TYPE_DOUBLE;
        }
    }
    
    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_DATE){
            return new DataViewHolder(inflater.inflate(R.layout.data_adapter_date, parent, false), viewType);
        }
        else if(viewType == VIEW_TYPE_SINGLE){
            return new DataViewHolder(inflater.inflate(R.layout.data_adapter_single, parent, false), viewType);
        }
        else {
            return new DataViewHolder(inflater.inflate(R.layout.data_adapter, parent, false), viewType);
        }
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, final int position) {
        if(dataList.get(position).isDate()){
            holder.textDate.setText(String.valueOf(dataList.get(position).getTime()));
        }
        else {
            final DataModel currentItem = dataList.get(position);
            if(currentItem.getImageUrl() == null){
                holder.imageData.setVisibility(View.GONE);
            }
            else {
                holder.imageData.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(currentItem.getImageUrl())
                        .into(holder.imageData);
            }
            if(currentItem.getText() == null){
                holder.textText.setVisibility(View.GONE);
            }
            else {
                holder.textText.setVisibility(View.VISIBLE);
                holder.textText.setText(currentItem.getText());
            }

            holder.textTitle.setText(currentItem.getTitle());
            holder.textName.setText(String.format("From %s", currentItem.getName()));
            if(currentItem.isLiked()){
                holder.buttonLike.setText(mContext.getString(R.string.unlike));
                holder.buttonLike.setTextColor(Color.WHITE);
                holder.buttonLike.setBackgroundResource(R.drawable.unlike_background);
            }
            else {
                holder.buttonLike.setText(mContext.getString(R.string.like));
                holder.buttonLike.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                holder.buttonLike.setBackgroundResource(R.drawable.like_background);
            }

            final int pos = holder.getAdapterPosition();
            holder.buttonLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentItem.setLiked(!currentItem.isLiked());
                    Log.e("DataAdapter", currentItem.toString());
                    notifyDataSetChanged();
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, DetailsActivity.class);
                    intent.putExtra("DATA", gson.toJson(currentItem, DataModel.class));
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    class DataViewHolder extends RecyclerView.ViewHolder{

        TextView textTitle;
        ImageView imageData;
        TextView textText;
        TextView buttonLike;
        TextView textName;
        TextView textDate;
        View itemView;

        DataViewHolder(View itemView, int viewType) {
            super(itemView);
            this.itemView = itemView;
            if(viewType == VIEW_TYPE_DATE){
                textDate = itemView.findViewById(R.id.text_date);
            }
            else {
                textTitle = itemView.findViewById(R.id.text_title);
                imageData = itemView.findViewById(R.id.image_data);
                textText = itemView.findViewById(R.id.text_text);
                buttonLike = itemView.findViewById(R.id.button_like);
                textName = itemView.findViewById(R.id.text_name);
            }
        }
    }
}
