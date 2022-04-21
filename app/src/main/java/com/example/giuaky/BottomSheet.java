package com.example.giuaky;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.w3c.dom.Text;

public class BottomSheet extends BottomSheetDialogFragment {

    private String staffID;
    private StaffLongClickItemListener listener;

    BottomSheet(String staffID, StaffLongClickItemListener listener){
        this.staffID = staffID;
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.staff_long_click_bottom_sheet, container, false);

        TextView btnRemove = v.findViewById(R.id.btn_bottom_sheet_remove);
        TextView btnView = v.findViewById(R.id.btn_bottom_sheet_view);
        TextView btnEdit = v.findViewById(R.id.btn_bottom_sheet_edit);

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onStaffClick(staffID, StaffLongClickItemListener.STAFF_LONG_CLICK_REMOVE);
                dismiss();
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onStaffClick(staffID, StaffLongClickItemListener.STAFF_LONG_CLICK_VIEW);
                dismiss();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onStaffClick(staffID, StaffLongClickItemListener.STAFF_LONG_CLICK_EDIT);
                dismiss();
            }
        });

        return v;

    }
}
