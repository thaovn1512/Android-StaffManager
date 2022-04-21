package com.example.giuaky;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StaffViewHolder extends RecyclerView.ViewHolder {

    TextView txtStaffName, txtIDStaff, txtFactory, txtDay;
    ImageView imgStaffAva;

    public StaffViewHolder(@NonNull View itemView) {
        super(itemView);

        txtStaffName = itemView.findViewById(R.id.staff_item_name);
        txtIDStaff = itemView.findViewById(R.id.txt_id_staff);
        txtFactory = itemView.findViewById(R.id.txt_factory);
        txtDay = itemView.findViewById(R.id.txt_number_days);
        imgStaffAva = itemView.findViewById(R.id.staff_item_ava);

    }
}
