package com.example.smartdesk.ui.seat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.smartdesk.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{

    Context context;
    ArrayList<Item> list;

    public RecyclerViewAdapter(Context context, ArrayList<Item> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.image1.setImageResource(list.get(position).image1);
        holder.image2.setImageResource(list.get(position).image2);
        holder.image3.setImageResource(list.get(position).image3);
        holder.image4.setImageResource(list.get(position).image4);
        holder.image5.setImageResource(list.get(position).image5);
        holder.image6.setImageResource(list.get(position).image6);
        holder.name1.setText(list.get(position).name1);
        holder.name2.setText(list.get(position).name2);
        holder.name3.setText(list.get(position).name3);
        holder.name4.setText(list.get(position).name4);
        holder.name5.setText(list.get(position).name5);
        holder.name6.setText(list.get(position).name6);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.seat_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView image1;
        ImageView image2;
        ImageView image3;
        ImageView image4;
        ImageView image5;
        ImageView image6;
        TextView name1;
        TextView name2;
        TextView name3;
        TextView name4;
        TextView name5;
        TextView name6;

        public MyViewHolder(View itemView) {
            super(itemView);
            image1 = itemView.findViewById(R.id.seat_frame1);
            image2 = itemView.findViewById(R.id.seat_frame2);
            image3 = itemView.findViewById(R.id.seat_frame3);
            image4 = itemView.findViewById(R.id.seat_frame4);
            image5 = itemView.findViewById(R.id.seat_frame5);
            image6 = itemView.findViewById(R.id.seat_frame6);
            name1 = itemView.findViewById(R.id.seat_name1);
            name2 = itemView.findViewById(R.id.seat_name2);
            name3 = itemView.findViewById(R.id.seat_name3);
            name4 = itemView.findViewById(R.id.seat_name4);
            name5 = itemView.findViewById(R.id.seat_name5);
            name6 = itemView.findViewById(R.id.seat_name6);
        }
    }

}
