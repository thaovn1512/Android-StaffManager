package com.example.giuaky;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DaysViewHolder extends RecyclerView.ViewHolder {

    TextView txtDayItem;
    View lineSelected;

    public DaysViewHolder(@NonNull View itemView) {
        super(itemView);

        txtDayItem = itemView.findViewById(R.id.txt_day_item);
        lineSelected = itemView.findViewById(R.id.line_day_selected_item);
        lineSelected = itemView.findViewById(R.id.line_day_selected_item);

    }

}
