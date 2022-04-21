package com.example.giuaky;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class StaffDaysRecyclerViewAdapter extends RecyclerView.Adapter<DaysViewHolder> {

    private ArrayList<Day> days;
    private Context context;
    private LayoutInflater mLayoutInflater;

    private ItemClickInterface listener;

    private int selectedPosition = 0;

    public StaffDaysRecyclerViewAdapter(Context context, ArrayList<Day> days, ItemClickInterface listener){
        this.context = context;
        this.days = days;
        this.listener = listener;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public DaysViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View recyclerViewItem = mLayoutInflater.inflate(R.layout.day_item, parent, false);

        recyclerViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleRecyclerItemClick((RecyclerView) parent, view);
                notifyItemChanged(selectedPosition);
                selectedPosition = ((RecyclerView) parent).getChildLayoutPosition(view);
                notifyItemChanged(selectedPosition);
            }
        });

        return new DaysViewHolder(recyclerViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull DaysViewHolder holder, int position) {

        Day day = this.days.get(position);
        holder.txtDayItem.setText(day.getDay());

        if(position==selectedPosition){
            holder.txtDayItem.setTextColor(Color.parseColor("#669782"));
            holder.lineSelected.setVisibility(View.VISIBLE);
        }
        else{
            holder.txtDayItem.setTextColor(Color.parseColor("#9c9c9c"));
            holder.lineSelected.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return this.days.size();
    }

    private void handleRecyclerItemClick(RecyclerView recyclerView, View itemView) {
        int itemPosition = recyclerView.getChildLayoutPosition(itemView);
        Day day  = this.days.get(itemPosition);

        listener.onDayClick(day.getId());
    }
}
